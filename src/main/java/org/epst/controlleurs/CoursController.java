package org.epst.controlleurs;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Link;
import org.epst.models.ClasseModel;
import org.epst.models.Cours.Cours;
import org.epst.models.Cours.Video;
import org.epst.services.BibliothequeStorageService;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Path("cours")
public class CoursController {

    private static final String MEDIA_READY = "READY";
    private static final String MEDIA_PENDING = "PENDING";

    @Inject
    BibliothequeStorageService storage;

    public static class BibliothequeUploadRequest {
        public String fileName;
        public String contentType;
        public Long size;
    }

    public static class BibliothequeConfirmRequest {
        public String objectKey;
    }

    private class CoursClasse {
        public String cours;
        public Long idCours;
        public UUID idClasse;
    }

    @GET
    @Path("one")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCoursById(@QueryParam("id") Long id){
        //
        Cours cours = Cours.findById(id);
        //
        return Response.ok(cours).build();
    }


    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response all() {
        List<Cours> coursList = Cours.listAll();
        coursList.removeIf(cours -> !hasAvailableMedia(cours));
        return Response.ok(coursList).build();
    }

    @Path("allcours")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response all(@QueryParam("idClasse") UUID idClasse,
                        @QueryParam("typeFormation") String typeFormation) {
        //
        HashMap params = new HashMap();
        params.put("idClasse", idClasse);
        params.put("propriete", typeFormation);
        List<Cours> coursList = Cours.find("idClasse =: idClasse and propriete =: propriete", params).list();
        coursList.removeIf(cours -> !hasAvailableMedia(cours));
        //
        return Response.ok(coursList).build();
    }

    //

    @Path("checkcours")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkcours(
            @QueryParam("cours") String cours,
            @QueryParam("cycle") String cycle,
            @QueryParam("banche") String banche,
            @QueryParam("type") String type,
            @QueryParam("notion") String notion,
            @QueryParam("classe") int classe,
            @QueryParam("propriete") String propriete
                               ) {
        //
        HashMap params = new HashMap();
        params.put("cours", cours);
        params.put("cycle", cycle);
        params.put("banche", banche);
        params.put("type", type);
        params.put("notion", notion);
        params.put("cls", classe);
        //params.put("propriete", propriete);
        //
        System.out.println("cours =: "+cours+" and cycle =: "+cycle+" and banche =: "+banche+" and " +
                "type =: "+type+" and notion =: "+notion+" and classe =: "+classe);
        //
        List<Cours> courss = Cours.listAll();

        List<Cours> co = courss.stream()
                .filter(c -> params.get("cours") == null ||
                        c.cours.toLowerCase().equalsIgnoreCase(params.get("cours").toString()))
                .filter(c -> params.get("cycle") == null ||
                        c.cycle.toLowerCase().equalsIgnoreCase(params.get("cycle").toString()))
                .filter(c -> params.get("banche") == null ||
                        c.banche.equalsIgnoreCase(params.get("banche").toString()))
                .filter(c -> params.get("type") == null ||
                        c.type.equalsIgnoreCase(params.get("type").toString()))
                .filter(c -> params.get("notion") == null ||
                        c.notion.equalsIgnoreCase(params.get("notion").toString()))
                .filter(c -> params.get("cls") == null ||
                        c.cls == (int) params.get("cls"))
                .toList();
        //
        System.out.println("Data: "+co.get(0).id);
        //
        return Response.ok(co.get(0).id).build();
        //
    }

    @Path("coursDataPdf.pdf")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response coursData(@QueryParam("id") Long id) {
        Cours cours = Cours.findById(id);
        return mediaResponse(cours, true);
    }

    @Path("media")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response media(@QueryParam("id") Long id) {
        Cours cours = Cours.findById(id);
        return mediaResponse(cours, false);
    }


    @Path("update")
    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCours(Cours cours) {
        Cours c = Cours.findById(cours.id);
        if(c == null){
            return Response.serverError().build();
        }
        //c.setClasse(cours.getClasse());
        //c.setMatiere(c.getMatiere());
        return Response.ok(c).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ajouterCours(Cours cours) {
        cours.data = null;
        cours.mediaStorageKey = null;
        cours.mediaOriginalName = null;
        cours.mediaContentType = null;
        cours.mediaSize = null;
        cours.mediaStorageStatus = MEDIA_PENDING;
        cours.persist();
        return Response.ok(cours.id).build();
    }

    @Path("{id}/media/upload-url")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBibliothequeUploadUrl(
            @PathParam("id") Long id,
            BibliothequeUploadRequest request
    ) {
        Cours cours = Cours.findById(id);
        if (cours == null) {
            return error(Response.Status.NOT_FOUND, "Le support de bibliotheque est introuvable.");
        }
        if (request == null || request.fileName == null || request.fileName.isBlank()) {
            return error(Response.Status.BAD_REQUEST, "Le nom du fichier est obligatoire.");
        }
        if (request.size == null || request.size <= 0) {
            return error(Response.Status.BAD_REQUEST, "La taille du fichier est obligatoire.");
        }
        if (request.size > storage.maxFileSizeBytes()) {
            return error(Response.Status.REQUEST_ENTITY_TOO_LARGE,
                    "Le fichier depasse la taille maximale autorisee.");
        }
        if (!storage.isConfigured()) {
            return error(Response.Status.SERVICE_UNAVAILABLE,
                    "Bucketeer n'est pas configure sur l'application serveur.");
        }

        String contentType = request.contentType == null || request.contentType.isBlank()
                ? mapMediaType(cours.type)
                : request.contentType.trim();
        String previousKey = cours.mediaStorageKey;
        try {
            BibliothequeStorageService.UploadTarget target =
                    storage.createUploadTarget(id, request.fileName, contentType);
            cours.mediaStorageKey = target.objectKey();
            cours.mediaOriginalName = request.fileName;
            cours.mediaContentType = contentType;
            cours.mediaSize = request.size;
            cours.mediaStorageStatus = MEDIA_PENDING;
            cours.data = null;
            if (previousKey != null && !previousKey.equals(target.objectKey())) {
                storage.deleteQuietly(previousKey);
            }
            return Response.ok(target).build();
        } catch (RuntimeException e) {
            return storageError(e);
        }
    }

    @Path("{id}/media/confirm")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirmBibliothequeUpload(
            @PathParam("id") Long id,
            BibliothequeConfirmRequest request
    ) {
        Cours cours = Cours.findById(id);
        if (cours == null) {
            return error(Response.Status.NOT_FOUND, "Le support de bibliotheque est introuvable.");
        }
        if (request == null
                || !storage.isKeyForCourse(id, request.objectKey)
                || !Objects.equals(cours.mediaStorageKey, request.objectKey)) {
            return error(Response.Status.BAD_REQUEST, "La reference du fichier est invalide.");
        }

        try {
            BibliothequeStorageService.StoredObject object = storage.headObject(request.objectKey);
            if (object.size() <= 0) {
                return error(Response.Status.BAD_REQUEST, "Le fichier envoye est vide.");
            }
            if (cours.mediaSize != null && !cours.mediaSize.equals(object.size())) {
                return error(Response.Status.CONFLICT,
                        "La taille recue par Bucketeer ne correspond pas au fichier selectionne.");
            }
            cours.mediaSize = object.size();
            if (object.contentType() != null && !object.contentType().isBlank()) {
                cours.mediaContentType = object.contentType();
            }
            cours.mediaStorageStatus = MEDIA_READY;
            cours.data = null;
            return Response.ok(Map.of(
                    "id", cours.id,
                    "objectKey", cours.mediaStorageKey,
                    "size", cours.mediaSize,
                    "status", cours.mediaStorageStatus
            )).build();
        } catch (RuntimeException e) {
            return storageError(e);
        }
    }

    @Path("media")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response ajouterMedia(
            @QueryParam("id") Long id,
            @HeaderParam(HttpHeaders.CONTENT_LENGTH) Long contentLength,
            @HeaderParam(HttpHeaders.CONTENT_TYPE) String contentType,
            InputStream data
    ) {
        Cours cours = Cours.findById(id);
        if (cours == null) {
            return error(Response.Status.NOT_FOUND, "Le support de bibliotheque est introuvable.");
        }
        if (contentLength == null || contentLength <= 0) {
            return error(Response.Status.BAD_REQUEST, "La taille du fichier est obligatoire.");
        }
        if (contentLength > storage.maxFileSizeBytes()) {
            return error(Response.Status.REQUEST_ENTITY_TOO_LARGE,
                    "Le fichier depasse la taille maximale autorisee.");
        }
        if (!storage.isConfigured()) {
            return error(Response.Status.SERVICE_UNAVAILABLE,
                    "Bucketeer n'est pas configure sur l'application serveur.");
        }

        String previousKey = cours.mediaStorageKey;
        String originalName = defaultMediaFileName(cours);
        String effectiveContentType = contentType == null
                || contentType.isBlank()
                || MediaType.APPLICATION_OCTET_STREAM.equalsIgnoreCase(contentType)
                ? mapMediaType(cours.type)
                : contentType;
        String key = storage.createObjectKey(id, originalName);
        try {
            storage.upload(key, data, contentLength, effectiveContentType);
            cours.data = null;
            cours.mediaStorageKey = key;
            cours.mediaOriginalName = originalName;
            cours.mediaContentType = effectiveContentType;
            cours.mediaSize = contentLength;
            cours.mediaStorageStatus = MEDIA_READY;
            if (previousKey != null && !previousKey.equals(key)) {
                storage.deleteQuietly(previousKey);
            }
            return Response.ok(Map.of("id", cours.id, "status", MEDIA_READY)).build();
        } catch (RuntimeException e) {
            storage.deleteQuietly(key);
            return storageError(e);
        }
    }



    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteClasse(@QueryParam("id") Long id) {
        Cours cours = Cours.findById(id);
        if (cours == null) {
            return;
        }
        String storageKey = cours.mediaStorageKey;
        cours.delete();
        storage.deleteQuietly(storageKey);
    }

    @Path("lire/{classe}/{matiere}/{notion}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream lireVideo(@PathParam("classe") String classe,
                                 @PathParam("matiere") String matiere,
                                 @PathParam("notion") String notion) {
        File video = new File("C:\\Users\\Pierre\\Downloads\\Koffi Olomide - Ligablo IGF (Clip Officiel).mp4");
        try {
            FileInputStream fis = new FileInputStream(video);
            return fis;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String mapMediaType(String type) {
        if (type == null) return MediaType.APPLICATION_OCTET_STREAM;
        String t = type.toLowerCase();
        return switch (t) {
            case "pdf" -> "application/pdf";
            case "mp4" -> "video/mp4";
            case "mov" -> "video/quicktime";
            case "avi" -> "video/x-msvideo";
            case "mkv" -> "video/x-matroska";
            case "mp3" -> "audio/mpeg";
            case "wav" -> "audio/wav";
            case "aac" -> "audio/aac";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "zip" -> "application/zip";
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    private Response mediaResponse(Cours cours, boolean inline) {
        if (cours == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (MEDIA_READY.equals(cours.mediaStorageStatus)
                && cours.mediaStorageKey != null
                && !cours.mediaStorageKey.isBlank()) {
            String contentType = cours.mediaContentType == null || cours.mediaContentType.isBlank()
                    ? mapMediaType(cours.type)
                    : cours.mediaContentType;
            String fileName = cours.mediaOriginalName == null || cours.mediaOriginalName.isBlank()
                    ? defaultMediaFileName(cours)
                    : cours.mediaOriginalName;
            try {
                return Response.temporaryRedirect(
                                storage.createDownloadUri(
                                        cours.mediaStorageKey,
                                        fileName,
                                        contentType,
                                        inline
                                )
                        )
                        .header(HttpHeaders.CACHE_CONTROL, "no-store")
                        .build();
            } catch (RuntimeException e) {
                return storageError(e);
            }
        }

        if (cours.data == null || cours.data.length == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        String contentType = mapMediaType(cours.type);
        String disposition = inline ? "inline" : "attachment";
        return Response.ok(cours.data, contentType)
                .header("Content-Disposition",
                        disposition + "; filename=\"" + defaultMediaFileName(cours) + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, cours.data.length)
                .build();
    }

    private boolean hasAvailableMedia(Cours cours) {
        return cours != null && (
                (MEDIA_READY.equals(cours.mediaStorageStatus)
                        && cours.mediaStorageKey != null
                        && !cours.mediaStorageKey.isBlank())
                        || (cours.data != null && cours.data.length > 0)
        );
    }

    private String defaultMediaFileName(Cours cours) {
        String extension = cours.type == null || cours.type.isBlank() ? "bin" : cours.type;
        return "cours_" + cours.id + "." + extension.replace(".", "");
    }

    private Response storageError(RuntimeException exception) {
        return error(Response.Status.BAD_GATEWAY,
                "Le stockage Bucketeer est momentanement indisponible: " + exception.getMessage());
    }

    private Response error(Response.Status status, String message) {
        return Response.status(status).entity(Map.of("message", message)).build();
    }

}
