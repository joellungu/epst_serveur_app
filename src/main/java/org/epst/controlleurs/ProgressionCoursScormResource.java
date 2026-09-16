package org.epst.controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.Agent.Agent;
import org.epst.models.scorm.ProgressionCoursScorm;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/scorm-progressions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProgressionCoursScormResource {

    private final ObjectMapper objectMapper;

    @ConfigProperty(name = "school.server.base-url", defaultValue = "http://localhost:9090")
    String schoolServerBaseUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ProgressionCoursScormResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @GET
    @Transactional
    public List<ProgressionCoursScorm> getAll() {
        return ProgressionCoursScorm.listAll();
    }

    @GET
    @Transactional
    @Path("/list")
    public List<ProgressionCoursScorm> list(
            @QueryParam("numeroIdentifiant") String numeroIdentifiant,
            @QueryParam("cle") String cle,
            @QueryParam("courseId") String courseId
    ) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder("1 = 1");

        if (numeroIdentifiant != null && !numeroIdentifiant.isBlank()) {
            query.append(" and numeroIdentifiant = :numeroIdentifiant");
            params.put("numeroIdentifiant", numeroIdentifiant);
        }
        if (cle != null && !cle.isBlank()) {
            query.append(" and cle = :cle");
            params.put("cle", cle);
        }
        if (courseId != null && !courseId.isBlank()) {
            query.append(" and courseId = :courseId");
            params.put("courseId", courseId);
        }

        query.append(" order by coalesce(clientCreatedAt, synchronizedAt) desc, id desc");
        return ProgressionCoursScorm.find(query.toString(), params).list();
    }

    @GET
    @Path("/{id}")
    public ProgressionCoursScorm get(@PathParam("id") Long id) {
        ProgressionCoursScorm progression = ProgressionCoursScorm.findById(id);
        if (progression == null) {
            throw new NotFoundException();
        }
        return progression;
    }

    @GET
    @Path("/latest")
    @Transactional
    public List<ProgressionCoursScorm> latest(
            @QueryParam("numeroIdentifiant") String numeroIdentifiant,
            @QueryParam("cle") String cle
    ) {
        if (isBlank(numeroIdentifiant) || isBlank(cle)) {
            throw new jakarta.ws.rs.BadRequestException("numeroIdentifiant et cle sont obligatoires");
        }

        List<ProgressionCoursScorm> all = ProgressionCoursScorm.find(
                "numeroIdentifiant = ?1 and cle = ?2 order by coalesce(clientCreatedAt, synchronizedAt) desc, id desc",
                numeroIdentifiant,
                cle
        ).list();

        Map<String, ProgressionCoursScorm> latestByCourse = new LinkedHashMap<>();
        for (ProgressionCoursScorm progression : all) {
            latestByCourse.putIfAbsent(progression.courseId, progression);
        }
        return new ArrayList<>(latestByCourse.values());
    }

    @GET
    @Path("/resume")
    @Transactional
    public List<ScormCourseResumeResponse> resume(
            @QueryParam("numeroIdentifiant") String numeroIdentifiant,
            @QueryParam("cle") String cle
    ) {
        if (isBlank(numeroIdentifiant) || isBlank(cle)) {
            throw new jakarta.ws.rs.BadRequestException("numeroIdentifiant et cle sont obligatoires");
        }

        List<ProgressionCoursScorm> all = ProgressionCoursScorm.find(
                "numeroIdentifiant = ?1 and cle = ?2 order by coalesce(clientCreatedAt, synchronizedAt) desc, id desc",
                numeroIdentifiant,
                cle
        ).list();

        Map<String, ScormCourseResumeResponse> resumes = new LinkedHashMap<>();
        for (ProgressionCoursScorm progression : all) {
            ScormCourseResumeResponse resume = resumes.computeIfAbsent(
                    progression.courseId,
                    courseId -> ScormCourseResumeResponse.fromLatest(progression)
            );
            resume.totalAvancees++;
            if (progression.nombreInteractions != null && progression.nombreInteractions > resume.nombreInteractions) {
                resume.nombreInteractions = progression.nombreInteractions;
            }
        }

        return new ArrayList<>(resumes.values());
    }

    @GET
    @Path("/course-details/{courseId}")
    @Transactional
    public List<ScormCourseTeacherDetailResponse> courseDetails(@PathParam("courseId") String courseId) {
        if (isBlank(courseId)) {
            throw new jakarta.ws.rs.BadRequestException("courseId est obligatoire");
        }

        List<ProgressionCoursScorm> all = ProgressionCoursScorm.find(
                "courseId = ?1 order by coalesce(clientCreatedAt, synchronizedAt) desc, id desc",
                courseId
        ).list();

        Map<String, ScormCourseTeacherDetailResponse> details = new LinkedHashMap<>();
        for (ProgressionCoursScorm progression : all) {
            String key = progression.numeroIdentifiant + "::" + safe(progression.cle);
            ScormCourseTeacherDetailResponse detail = details.computeIfAbsent(
                    key,
                    ignored -> ScormCourseTeacherDetailResponse.fromLatest(progression)
            );
            detail.totalSynchronisations++;
            if (progression.nombreInteractions != null && progression.nombreInteractions > detail.nombreInteractions) {
                detail.nombreInteractions = progression.nombreInteractions;
            }
        }

        for (ScormCourseTeacherDetailResponse detail : details.values()) {
            Agent agent = Agent.find("matricule", detail.numeroIdentifiant).firstResult();
            if (agent == null) {
                agent = Agent.find("numero", detail.numeroIdentifiant).firstResult();
            }
            if (agent != null) {
                detail.agentId = agent.id;
                detail.nom = agent.nom;
                detail.postnom = agent.postnom;
                detail.prenom = agent.prenom;
                detail.nomComplet = buildNomComplet(agent);
                detail.numero = agent.numero;
                detail.email = agent.email;
                detail.province = agent.province;
                detail.district = agent.district;
                detail.role = agent.role;
                detail.typeApprenant = "ENSEIGNANT";
            } else {
                // Pas un agent/enseignant : on tente un élève côté Smart-Kelasi
                // (les IDs élèves viennent de Smart-Kelasi, pas de la table Agent).
                JsonNode eleve = fetchEleveNode(detail.numeroIdentifiant);
                if (eleve != null) {
                    detail.nom = textOrNull(eleve, "nom");
                    detail.postnom = textOrNull(eleve, "postnom");
                    detail.prenom = textOrNull(eleve, "prenom");
                    detail.nomComplet = buildNomComplet(
                            detail.nom, detail.postnom, detail.prenom,
                            detail.numeroIdentifiant);
                    detail.classe = textOrNull(eleve, "classe");
                    detail.cleEcole = textOrNull(eleve, "cleEcole");
                    detail.typeApprenant = "ELEVE";
                } else {
                    detail.nomComplet = detail.numeroIdentifiant;
                    detail.typeApprenant = "INCONNU";
                }
            }
        }

        return new ArrayList<>(details.values());
    }

    @POST
    @Path("/sync")
    @Transactional
    public Response sync(SyncScormProgressionRequest request) {
        validate(request);
        ProgressionCoursScorm progression = findByClientSyncId(request.clientSyncId);
        boolean created = progression == null;

        if (created) {
            progression = new ProgressionCoursScorm();
        }

        apply(request, progression);

        if (created) {
            progression.persist();
        }

        return Response.ok(SyncScormProgressionResponse.from(progression, created)).build();
    }

    @POST
    @Path("/sync/bulk")
    @Transactional
    public Response syncBulk(List<SyncScormProgressionRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new jakarta.ws.rs.BadRequestException("La liste des progressions est vide");
        }

        List<SyncScormProgressionResponse> responses = new ArrayList<>();
        for (SyncScormProgressionRequest request : requests) {
            validate(request);
            ProgressionCoursScorm progression = findByClientSyncId(request.clientSyncId);
            boolean created = progression == null;

            if (created) {
                progression = new ProgressionCoursScorm();
            }

            apply(request, progression);

            if (created) {
                progression.persist();
            }

            responses.add(SyncScormProgressionResponse.from(progression, created));
        }

        BulkSyncScormProgressionResponse response = new BulkSyncScormProgressionResponse();
        response.synchronizedCount = responses.size();
        response.items = responses;
        return Response.ok(response).build();
    }

    private ProgressionCoursScorm findByClientSyncId(String clientSyncId) {
        return ProgressionCoursScorm.find("clientSyncId", clientSyncId).firstResult();
    }

    private void validate(SyncScormProgressionRequest request) {
        if (request == null) {
            throw new jakarta.ws.rs.BadRequestException("Le corps de la requete est obligatoire");
        }
        if (isBlank(request.numeroIdentifiant)) {
            throw new jakarta.ws.rs.BadRequestException("numeroIdentifiant est obligatoire");
        }
        if (isBlank(request.cle)) {
            throw new jakarta.ws.rs.BadRequestException("cle est obligatoire");
        }
        if (isBlank(request.courseId)) {
            throw new jakarta.ws.rs.BadRequestException("courseId est obligatoire");
        }
        if (isBlank(request.clientSyncId)) {
            throw new jakarta.ws.rs.BadRequestException("clientSyncId est obligatoire pour eviter les doublons");
        }
    }

    private void apply(SyncScormProgressionRequest request, ProgressionCoursScorm progression) {
        progression.numeroIdentifiant = request.numeroIdentifiant;
        progression.cle = request.cle;
        progression.courseId = request.courseId;
        progression.courseTitle = request.courseTitle;
        progression.clientSyncId = request.clientSyncId;
        progression.sequenceNumber = request.sequenceNumber;
        progression.typeAvancee = request.typeAvancee;
        progression.derniereAction = request.derniereAction;
        progression.lessonStatus = request.lessonStatus;
        progression.progressPercent = request.progressPercent;
        progression.scoreRaw = request.scoreRaw;
        progression.scoreMin = request.scoreMin;
        progression.scoreMax = request.scoreMax;
        progression.lessonLocation = request.lessonLocation;
        progression.suspendData = request.suspendData;
        progression.sessionTime = request.sessionTime;
        progression.totalTime = request.totalTime;
        progression.tempsPasseSecondes = request.tempsPasseSecondes;
        progression.nombreInteractions = request.interactions == null ? 0 : request.interactions.size();
        progression.interactionsJson = toJson(request.interactions);
        progression.donneesScormJson = toJson(request.donneesScorm);
        progression.clientCreatedAt = request.clientCreatedAt;
        progression.synchronizedAt = LocalDateTime.now();
        progression.statutSynchronisation = "SYNCHRONISE";
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new jakarta.ws.rs.BadRequestException("Format JSON invalide", e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String buildNomComplet(Agent agent) {
        return buildNomComplet(agent.nom, agent.postnom, agent.prenom, agent.matricule);
    }

    private String buildNomComplet(String nom, String postnom, String prenom, String fallback) {
        StringBuilder builder = new StringBuilder();
        if (nom != null && !nom.isBlank()) {
            builder.append(nom.trim());
        }
        if (postnom != null && !postnom.isBlank()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(postnom.trim());
        }
        if (prenom != null && !prenom.isBlank()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(prenom.trim());
        }
        return builder.length() == 0 ? (fallback == null ? "" : fallback) : builder.toString();
    }

    private JsonNode fetchEleveNode(String numeroIdentifiant) {
        if (isBlank(numeroIdentifiant)) return null;
        try {
            String base = schoolServerBaseUrl.endsWith("/") ? schoolServerBaseUrl : schoolServerBaseUrl + "/";
            URI uri = URI.create(base + "eleve/verify/" + numeroIdentifiant);
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return null;
            return objectMapper.readTree(response.body());
        } catch (Exception e) {
            return null;
        }
    }

    private String textOrNull(JsonNode node, String field) {
        if (node == null || !node.hasNonNull(field)) return null;
        String value = node.get(field).asText();
        return (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) ? null : value.trim();
    }

    public static class SyncScormProgressionRequest {
        public String numeroIdentifiant;
        public String cle;
        public String courseId;
        public String courseTitle;
        public String clientSyncId;
        public Long sequenceNumber;
        public String typeAvancee;
        public String derniereAction;
        public String lessonStatus;
        public Double progressPercent;
        public Double scoreRaw;
        public Double scoreMin;
        public Double scoreMax;
        public String lessonLocation;
        public String suspendData;
        public String sessionTime;
        public String totalTime;
        public Long tempsPasseSecondes;
        public LocalDateTime clientCreatedAt;
        public Map<String, Object> donneesScorm;
        public List<ScormInteractionRequest> interactions;
    }

    public static class ScormInteractionRequest {
        public Integer interactionIndex;
        public String interactionId;
        public String interactionType;
        public String studentResponse;
        public String correctResponse;
        public String result;
        public String latency;
        public Double weighting;
    }

    public static class SyncScormProgressionResponse {
        public Long id;
        public String clientSyncId;
        public String courseId;
        public Long sequenceNumber;
        public String lessonStatus;
        public Double progressPercent;
        public String statutSynchronisation;
        public boolean created;
        public LocalDateTime synchronizedAt;

        static SyncScormProgressionResponse from(ProgressionCoursScorm progression, boolean created) {
            SyncScormProgressionResponse response = new SyncScormProgressionResponse();
            response.id = progression.id;
            response.clientSyncId = progression.clientSyncId;
            response.courseId = progression.courseId;
            response.sequenceNumber = progression.sequenceNumber;
            response.lessonStatus = progression.lessonStatus;
            response.progressPercent = progression.progressPercent;
            response.statutSynchronisation = progression.statutSynchronisation;
            response.created = created;
            response.synchronizedAt = progression.synchronizedAt;
            return response;
        }
    }

    public static class BulkSyncScormProgressionResponse {
        public int synchronizedCount;
        public List<SyncScormProgressionResponse> items;
    }

    public static class ScormCourseResumeResponse {
        public String numeroIdentifiant;
        public String cle;
        public String courseId;
        public String courseTitle;
        public String lessonStatus;
        public Double progressPercent;
        public Double scoreRaw;
        public Double scoreMin;
        public Double scoreMax;
        public String lessonLocation;
        public String sessionTime;
        public String totalTime;
        public Long tempsPasseSecondes;
        public Integer nombreInteractions;
        public int totalAvancees;
        public LocalDateTime derniereSynchronisation;

        static ScormCourseResumeResponse fromLatest(ProgressionCoursScorm progression) {
            ScormCourseResumeResponse response = new ScormCourseResumeResponse();
            response.numeroIdentifiant = progression.numeroIdentifiant;
            response.cle = progression.cle;
            response.courseId = progression.courseId;
            response.courseTitle = progression.courseTitle;
            response.lessonStatus = progression.lessonStatus;
            response.progressPercent = progression.progressPercent;
            response.scoreRaw = progression.scoreRaw;
            response.scoreMin = progression.scoreMin;
            response.scoreMax = progression.scoreMax;
            response.lessonLocation = progression.lessonLocation;
            response.sessionTime = progression.sessionTime;
            response.totalTime = progression.totalTime;
            response.tempsPasseSecondes = progression.tempsPasseSecondes;
            response.nombreInteractions = progression.nombreInteractions == null ? 0 : progression.nombreInteractions;
            response.derniereSynchronisation = progression.synchronizedAt;
            return response;
        }
    }

    public static class ScormCourseTeacherDetailResponse {
        public Long agentId;
        public String numeroIdentifiant;
        public String cle;
        public String nom;
        public String postnom;
        public String prenom;
        public String nomComplet;
        public String numero;
        public String email;
        public String province;
        public String district;
        public Integer role;
        public String classe;
        public String cleEcole;
        public String typeApprenant;
        public String courseId;
        public String courseTitle;
        public String lessonStatus;
        public Double progressPercent;
        public Double scoreRaw;
        public Double scoreMin;
        public Double scoreMax;
        public String lessonLocation;
        public String sessionTime;
        public String totalTime;
        public Long tempsPasseSecondes;
        public Integer nombreInteractions;
        public int totalSynchronisations;
        public LocalDateTime derniereSynchronisation;

        static ScormCourseTeacherDetailResponse fromLatest(ProgressionCoursScorm progression) {
            ScormCourseTeacherDetailResponse response = new ScormCourseTeacherDetailResponse();
            response.numeroIdentifiant = progression.numeroIdentifiant;
            response.cle = progression.cle;
            response.courseId = progression.courseId;
            response.courseTitle = progression.courseTitle;
            response.lessonStatus = progression.lessonStatus;
            response.progressPercent = progression.progressPercent;
            response.scoreRaw = progression.scoreRaw;
            response.scoreMin = progression.scoreMin;
            response.scoreMax = progression.scoreMax;
            response.lessonLocation = progression.lessonLocation;
            response.sessionTime = progression.sessionTime;
            response.totalTime = progression.totalTime;
            response.tempsPasseSecondes = progression.tempsPasseSecondes;
            response.nombreInteractions = progression.nombreInteractions == null ? 0 : progression.nombreInteractions;
            response.derniereSynchronisation = progression.synchronizedAt;
            return response;
        }
    }
}
