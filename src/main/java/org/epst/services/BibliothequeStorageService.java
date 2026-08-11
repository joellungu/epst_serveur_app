package org.epst.services;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.InputStream;
import java.net.URI;
import java.text.Normalizer;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class BibliothequeStorageService {

    private static final Logger LOG = Logger.getLogger(BibliothequeStorageService.class);
    private static final String KEY_PREFIX = "bibliotheque/cours/";

    @ConfigProperty(name = "bibliotheque.storage.s3.access-key-id", defaultValue = "")
    String accessKeyId;

    @ConfigProperty(name = "bibliotheque.storage.s3.secret-access-key", defaultValue = "")
    String secretAccessKey;

    @ConfigProperty(name = "bibliotheque.storage.s3.region", defaultValue = "")
    String region;

    @ConfigProperty(name = "bibliotheque.storage.s3.bucket", defaultValue = "")
    String bucket;

    @ConfigProperty(name = "bibliotheque.storage.upload-url-duration-minutes", defaultValue = "30")
    long uploadUrlDurationMinutes;

    @ConfigProperty(name = "bibliotheque.storage.download-url-duration-minutes", defaultValue = "15")
    long downloadUrlDurationMinutes;

    @ConfigProperty(name = "bibliotheque.storage.max-file-size-bytes", defaultValue = "536870912")
    long maxFileSizeBytes;

    private volatile S3Presigner presigner;
    private volatile S3Client client;

    public boolean isConfigured() {
        return !isBlank(accessKeyId)
                && !isBlank(secretAccessKey)
                && !isBlank(region)
                && !isBlank(bucket);
    }

    public long maxFileSizeBytes() {
        return maxFileSizeBytes;
    }

    public UploadTarget createUploadTarget(Long coursId, String originalName, String contentType) {
        ensureClients();

        String key = createObjectKey(coursId, originalName);
        String normalizedContentType = normalizeContentType(contentType);
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(normalizedContentType)
                .build();
        Duration duration = Duration.ofMinutes(Math.max(1, uploadUrlDurationMinutes));
        PresignedPutObjectRequest signed = presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(duration)
                        .putObjectRequest(objectRequest)
                        .build()
        );

        Map<String, String> headers = new LinkedHashMap<>();
        signed.signedHeaders().forEach((name, values) -> {
            // Le client HTTP positionne lui-meme Host a partir de l'URL.
            if (!"host".equalsIgnoreCase(name)) {
                headers.put(name, String.join(",", values));
            }
        });

        return new UploadTarget(
                signed.url().toExternalForm(),
                key,
                headers,
                Instant.now().plus(duration)
        );
    }

    public String createObjectKey(Long coursId, String originalName) {
        String safeName = sanitizeFileName(originalName);
        return KEY_PREFIX + coursId + "/" + UUID.randomUUID() + "-" + safeName;
    }

    public boolean isKeyForCourse(Long coursId, String key) {
        return key != null && key.startsWith(KEY_PREFIX + coursId + "/");
    }

    public StoredObject headObject(String key) {
        ensureClients();
        HeadObjectResponse response = client.headObject(
                HeadObjectRequest.builder().bucket(bucket).key(key).build()
        );
        return new StoredObject(response.contentLength(), response.contentType(), response.eTag());
    }

    public void upload(String key, InputStream input, long contentLength, String contentType) {
        ensureClients();
        if (contentLength <= 0) {
            throw new IllegalArgumentException("La taille du fichier est obligatoire.");
        }
        client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(normalizeContentType(contentType))
                        .build(),
                RequestBody.fromInputStream(input, contentLength)
        );
    }

    public URI createDownloadUri(String key, String fileName, String contentType, boolean inline) {
        ensureClients();
        String disposition = (inline ? "inline" : "attachment")
                + "; filename=\"" + sanitizeFileName(fileName) + "\"";
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentDisposition(disposition)
                .responseContentType(normalizeContentType(contentType))
                .build();
        Duration duration = Duration.ofMinutes(Math.max(1, downloadUrlDurationMinutes));
        return URI.create(presigner.presignGetObject(
                        GetObjectPresignRequest.builder()
                                .signatureDuration(duration)
                                .getObjectRequest(objectRequest)
                                .build()
                )
                .url()
                .toExternalForm());
    }

    public void deleteQuietly(String key) {
        if (isBlank(key) || !isConfigured()) {
            return;
        }
        try {
            ensureClients();
            client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        } catch (RuntimeException e) {
            LOG.warnf(e, "Impossible de supprimer le media Bucketeer %s", key);
        }
    }

    private synchronized void ensureClients() {
        if (client != null && presigner != null) {
            return;
        }
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Bucketeer n'est pas configure sur cette application serveur."
            );
        }

        StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
        );
        Region awsRegion = Region.of(region);
        presigner = S3Presigner.builder()
                .credentialsProvider(credentials)
                .region(awsRegion)
                .build();
        client = S3Client.builder()
                .credentialsProvider(credentials)
                .region(awsRegion)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }

    private String normalizeContentType(String contentType) {
        return isBlank(contentType) ? "application/octet-stream" : contentType.trim();
    }

    private String sanitizeFileName(String fileName) {
        String value = isBlank(fileName) ? "media.bin" : fileName.trim();
        value = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^A-Za-z0-9._-]", "_")
                .replaceAll("_+", "_");
        if (value.length() > 120) {
            value = value.substring(value.length() - 120);
        }
        return value.isBlank() ? "media.bin" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    @PreDestroy
    void close() {
        if (client != null) {
            client.close();
        }
        if (presigner != null) {
            presigner.close();
        }
    }

    public record UploadTarget(
            String uploadUrl,
            String objectKey,
            Map<String, String> headers,
            Instant expiresAt
    ) {
    }

    public record StoredObject(long size, String contentType, String eTag) {
    }
}
