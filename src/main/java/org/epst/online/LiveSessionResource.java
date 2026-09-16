package org.epst.online;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.Agent.Agent;
import org.epst.models.Classe;
import org.epst.models.InspecteurCours;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import jakarta.persistence.LockModeType;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Path("/online/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LiveSessionResource {
    @ConfigProperty(name = "school.server.base-url", defaultValue = "http://localhost:9090")
    String schoolServerBaseUrl;

    @ConfigProperty(name = "online.live-session-expiration-hours", defaultValue = "4")
    long liveSessionExpirationHours;

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static class StartSessionRequest {
        public String classId;
        public List<String> classIds;
        public String title;
        public String hostMatricule;
        public OnlineRole hostRole;
        public String zegoRoomId;
        public Boolean recordingEnabled;
        public Integer maxParticipants;
        public SessionAudience audience;
    }

    public static class JoinSessionRequest {
        public Long sessionId;
        public String matricule;
        public String displayName;
        public OnlineRole role;
        public InspectorFocus inspectorFocus;
    }

    public static class TeacherAccessRequest {
        public String accessKey;
        public String classId;
        public String matricule;
        public String displayName;
    }

    public static class EndSessionRequest {
        public Long sessionId;
        public String endedByMatricule;
        public OnlineRole endedByRole;
    }

    @POST
    @Path("/start")
    @Transactional
    public Response startSession(StartSessionRequest request) {
        if (request == null || request.hostMatricule == null || request.hostRole == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }
        expireStaleLiveSessions();

        if (!isInspectorRole(request.hostRole)) {
            Agent host = Agent.find("matricule", request.hostMatricule).firstResult();
            if (request.hostRole != OnlineRole.ADMIN || host == null || !OnlineRoleMapper.isAdminRole(host.role)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Host not allowed").build();
            }
        }

        List<Classe> resolvedClasses = resolveRequestedClasses(request);
        if (resolvedClasses.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Class not recognized").build();
        }
        Classe primaryClass = resolvedClasses.get(0);
        String resolvedClassId = primaryClass.id.toString();

        if (isInspectorRole(request.hostRole)) {
            Agent agent = Agent.find("matricule", request.hostMatricule).firstResult();
            if (agent == null || !isInspectorRoleAllowed(agent, request.hostRole)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Inspector not recognized").build();
            }
            for (Classe resolvedClass : resolvedClasses) {
                if (!isInspectorAssignedToClass(agent.id, resolvedClass.id)) {
                    return Response.status(Response.Status.FORBIDDEN).entity("Inspector not assigned to class").build();
                }
            }
        }

        LiveSession session = new LiveSession();
        session.classId = resolvedClassId;
        session.title = request.title != null ? request.title : "Session";
        session.createdByMatricule = request.hostMatricule;
        session.hostMatricule = request.hostMatricule;
        session.startedAt = LocalDateTime.now();
        session.status = LiveSession.SessionStatus.LIVE;
        session.accessKey = generateAccessKey();
        session.zegoRoomId = request.zegoRoomId != null
                ? request.zegoRoomId
                : "live-" + session.accessKey.toLowerCase(Locale.ROOT) + "-" + System.currentTimeMillis();
        session.recordingEnabled = request.recordingEnabled != null && request.recordingEnabled;
        session.maxParticipants = request.maxParticipants != null && request.maxParticipants > 0
                ? request.maxParticipants
                : 40;

        SessionAudience requestedAudience = request.audience != null ? request.audience : SessionAudience.STUDENT;
        if (request.hostRole == OnlineRole.INSPECTOR_STUDENT) {
            session.audience = SessionAudience.STUDENT;
        } else if (request.hostRole == OnlineRole.INSPECTOR_TEACHER) {
            session.audience = SessionAudience.TEACHER;
        } else if (request.hostRole == OnlineRole.INSPECTOR_STREAMING) {
            // L'inspecteur vidéo streaming (rôle 21) peut diffuser pour
            // les élèves, les enseignants ou les deux (BOTH).
            session.audience = requestedAudience;
        } else {
            session.audience = requestedAudience;
        }

        for (Classe resolvedClass : resolvedClasses) {
            if (hasActiveSessionForClass(resolvedClass.id.toString(), session.audience)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Class already has an active session for this audience")
                        .build();
            }
        }

        session.persist();
        for (Classe resolvedClass : resolvedClasses) {
            LiveSessionClass sessionClass = new LiveSessionClass();
            sessionClass.sessionId = session.id;
            sessionClass.classId = resolvedClass.id.toString();
            sessionClass.classLabel = ClassLabelUtil.buildLabel(resolvedClass);
            sessionClass.persist();
        }

        HashMap<String, Object> response = new HashMap<>();
        response.put("session", session);
        response.put("accessKey", session.accessKey);
        response.put("zegoRoomId", session.zegoRoomId);
        response.put("classes", LiveSessionClass.list("sessionId", session.id));
        return Response.ok(response).build();
    }

    @POST
    @Path("/teacher/access")
    @Transactional
    public Response teacherAccess(TeacherAccessRequest request) {
        if (request == null || isBlank(request.accessKey) || isBlank(request.classId)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }
        expireStaleLiveSessions();

        LiveSession session = LiveSession.find(
                "accessKey = ?1 and status = ?2 and audience <> ?3",
                normalizeAccessKey(request.accessKey),
                LiveSession.SessionStatus.LIVE,
                SessionAudience.STUDENT
        ).firstResult();
        if (session == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Live session not found").build();
        }

        LiveSessionClass allowedClass = findAllowedClass(session.id, request.classId);
        if (allowedClass == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Class not allowed for this live").build();
        }

        if (!isBlank(request.matricule)) {
            SessionParticipant participant = new SessionParticipant();
            participant.sessionId = session.id;
            participant.matricule = request.matricule;
            participant.displayName = !isBlank(request.displayName) ? request.displayName : request.matricule;
            participant.role = OnlineRole.TEACHER;
            participant.persist();
        }

        HashMap<String, Object> response = new HashMap<>();
        response.put("sessionId", session.id);
        response.put("accessKey", session.accessKey);
        response.put("zegoRoomId", session.zegoRoomId);
        response.put("classId", allowedClass.classId);
        response.put("classLabel", allowedClass.classLabel);
        return Response.ok(response).build();
    }

    @POST
    @Path("/student/access")
    @Transactional
    public Response studentAccess(TeacherAccessRequest request) {
        if (request == null || isBlank(request.accessKey) || isBlank(request.classId) || isBlank(request.matricule)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }
        expireStaleLiveSessions();

        // Même principe que teacher/access mais pour les élèves :
        // on accepte les sessions STUDENT ou BOTH (audience <> TEACHER).
        LiveSession session = LiveSession.find(
                "accessKey = ?1 and status = ?2 and audience <> ?3",
                normalizeAccessKey(request.accessKey),
                LiveSession.SessionStatus.LIVE,
                SessionAudience.TEACHER
        ).firstResult();
        if (session == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Live session not found").build();
        }

        LiveSessionClass allowedClass = findAllowedClass(session.id, request.classId);
        if (allowedClass == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Class not allowed for this live").build();
        }

        VerificationResult verification = verifyStudent(request.matricule);
        if (!verification.ok) {
            return Response.status(Response.Status.FORBIDDEN).entity("Student not recognized").build();
        }
        LiveSessionClass schoolClass = findAllowedClass(session.id, verification.classe);
        if (schoolClass == null || !schoolClass.classId.equals(allowedClass.classId)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Student not in class").build();
        }
        // Serialize admission so reconnects cannot consume extra seats.
        session = LiveSession.findById(session.id, LockModeType.PESSIMISTIC_WRITE);
        if (session.status != LiveSession.SessionStatus.LIVE) {
            return Response.status(Response.Status.CONFLICT).entity("Session is not live").build();
        }
        SessionParticipant existing = SessionParticipant.find(
                "sessionId = ?1 and matricule = ?2 and role = ?3 and status = ?4",
                session.id, request.matricule.trim(), OnlineRole.STUDENT,
                SessionParticipant.ParticipantStatus.JOINED).firstResult();
        if (existing == null) {
            long count = SessionParticipant.count("sessionId = ?1 and status = ?2", session.id,
                    SessionParticipant.ParticipantStatus.JOINED);
            if (count >= session.maxParticipants) {
                return Response.status(Response.Status.CONFLICT).entity("Class is full").build();
            }
            SessionParticipant participant = new SessionParticipant();
            participant.sessionId = session.id;
            participant.matricule = request.matricule.trim();
            participant.displayName = !isBlank(request.displayName) ? request.displayName : request.matricule;
            participant.role = OnlineRole.STUDENT;
            participant.persist();
        }

        HashMap<String, Object> response = new HashMap<>();
        response.put("sessionId", session.id);
        response.put("accessKey", session.accessKey);
        response.put("zegoRoomId", session.zegoRoomId);
        response.put("classId", allowedClass.classId);
        response.put("classLabel", allowedClass.classLabel);
        return Response.ok(response).build();
    }

    @GET
    @Path("/live")
    @Transactional
    public Response listLiveSessions() {
        expireStaleLiveSessions();
        List<LiveSession> sessions = LiveSession.list(
                "status = ?1 order by startedAt desc",
                LiveSession.SessionStatus.LIVE);
        return Response.ok(sessions).build();
    }

    @GET
    @Path("/recent")
    public Response listRecentSessions(@QueryParam("limit") Integer limit) {
        int max = (limit == null || limit <= 0 || limit > 200) ? 100 : limit;
        List<LiveSession> sessions = LiveSession.find(
                "order by startedAt desc").page(0, max).list();
        return Response.ok(sessions).build();
    }

    @POST
    @Path("/join")
    @Transactional
    public Response joinSession(JoinSessionRequest request) {
        if (request == null || request.sessionId == null || request.matricule == null || request.role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }
        expireStaleLiveSessions();

        LiveSession session = LiveSession.findById(request.sessionId);
        if (session == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Session not found").build();
        }

        if (session.status != LiveSession.SessionStatus.LIVE) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Session is not live").build();
        }

        Classe sessionClass = ClassLabelUtil.resolveClass(session.classId);

        if (isInspectorRole(request.role)) {
            Agent agent = Agent.find("matricule", request.matricule).firstResult();
            if (agent == null || !isInspectorRoleAllowed(agent, request.role)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Inspector not recognized").build();
            }
            if (sessionClass != null && !isInspectorAssignedToClass(agent.id, sessionClass.id)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Inspector not assigned to class").build();
            }
        }

        if (request.role == OnlineRole.STUDENT) {
            VerificationResult result = verifyStudent(request.matricule);
            if (!result.ok) {
                return Response.status(Response.Status.FORBIDDEN).entity("Student not recognized").build();
            }
            if (sessionClass != null && result.classe != null && !ClassLabelUtil.matchesLabel(sessionClass, result.classe)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Student not in class").build();
            }
        }

        if (request.role == OnlineRole.TEACHER) {
            if (!verifyTeacher(request.matricule)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Teacher not recognized").build();
            }
        }

        if (!isAudienceAllowed(session.audience, request.role)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Audience mismatch for this session").build();
        }

        long currentParticipants = SessionParticipant.count(
                "sessionId = ?1 and status = ?2",
                request.sessionId,
                SessionParticipant.ParticipantStatus.JOINED
        );
        if (currentParticipants >= session.maxParticipants) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Class is full").build();
        }

        SessionParticipant participant = new SessionParticipant();
        participant.sessionId = request.sessionId;
        participant.matricule = request.matricule;
        participant.displayName = request.displayName != null ? request.displayName : request.matricule;
        participant.role = request.role;
        if (isInspectorRole(request.role)) {
            if (request.inspectorFocus != null) {
                participant.inspectorFocus = request.inspectorFocus;
            } else if (request.role == OnlineRole.INSPECTOR_TEACHER) {
                participant.inspectorFocus = InspectorFocus.TEACHERS;
            } else {
                participant.inspectorFocus = InspectorFocus.STUDENTS;
            }
        }
        participant.persist();

        HashMap<String, Object> response = new HashMap<>();
        response.put("session", session);
        response.put("participant", participant);
        response.put("zegoRoomId", session.zegoRoomId);
        response.put("zegoUserId", request.matricule);
        response.put("audience", session.audience);
        return Response.ok(response).build();
    }

    @POST
    @Path("/end")
    @Transactional
    public Response endSession(EndSessionRequest request) {
        if (request == null || request.sessionId == null || request.endedByMatricule == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

        LiveSession session = LiveSession.findById(request.sessionId);
        if (session == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Session not found").build();
        }

        Agent actor = Agent.find("matricule", request.endedByMatricule).firstResult();
        if (actor == null || (!OnlineRoleMapper.isAdminRole(actor.role)
                && !request.endedByMatricule.equals(session.hostMatricule))) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the host or an administrator can end this live").build();
        }

        session.status = LiveSession.SessionStatus.ENDED;
        session.endedAt = LocalDateTime.now();
        SessionParticipant.update("status = ?1, leftAt = ?2 where sessionId = ?3 and status = ?4",
                SessionParticipant.ParticipantStatus.LEFT, session.endedAt, session.id,
                SessionParticipant.ParticipantStatus.JOINED);

        return Response.ok(session).build();
    }

    @GET
    @Path("/{id}")
    public Response getSession(@PathParam("id") Long id) {
        LiveSession session = LiveSession.findById(id);
        if (session == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(session).build();
    }

    @GET
    @Path("/class/{classId}")
    public Response getSessionsByClass(@PathParam("classId") String classId) {
        Classe resolvedClass = ClassLabelUtil.resolveClass(classId);
        if (resolvedClass == null) {
            return Response.ok(List.of()).build();
        }
        List<LiveSessionClass> mappings = LiveSessionClass.list("classId", resolvedClass.id.toString());
        if (mappings.isEmpty()) {
            return Response.ok(LiveSession.list("classId", resolvedClass.id.toString())).build();
        }
        List<LiveSession> sessions = new ArrayList<>();
        for (LiveSessionClass mapping : mappings) {
            LiveSession session = LiveSession.findById(mapping.sessionId);
            if (session != null) {
                sessions.add(session);
            }
        }
        return Response.ok(sessions).build();
    }

    @GET
    @Path("/{id}/participants")
    public Response getParticipants(@PathParam("id") Long id) {
        List<SessionParticipant> participants = SessionParticipant.list("sessionId", id);
        return Response.ok(participants).build();
    }

    @POST
    @Path("/leave")
    @Transactional
    public Response leaveSession(JoinSessionRequest request) {
        if (request == null || request.sessionId == null || request.matricule == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

        SessionParticipant participant = SessionParticipant.find(
                "sessionId = ?1 and matricule = ?2 and status = ?3",
                request.sessionId,
                request.matricule,
                SessionParticipant.ParticipantStatus.JOINED
        ).firstResult();
        if (participant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        participant.status = SessionParticipant.ParticipantStatus.LEFT;
        participant.leftAt = LocalDateTime.now();
        return Response.ok(participant).build();
    }

    private List<Classe> resolveRequestedClasses(StartSessionRequest request) {
        List<String> requestedIds = new ArrayList<>();
        if (request.classIds != null) {
            requestedIds.addAll(request.classIds);
        }
        if (!isBlank(request.classId) && requestedIds.isEmpty()) {
            requestedIds.add(request.classId);
        }

        List<Classe> classes = new ArrayList<>();
        for (String requestedId : requestedIds) {
            Classe resolvedClass = ClassLabelUtil.resolveClass(requestedId);
            if (resolvedClass == null) {
                continue;
            }
            boolean exists = false;
            for (Classe classe : classes) {
                if (classe.id.equals(resolvedClass.id)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                classes.add(resolvedClass);
            }
        }
        return classes;
    }

    private boolean hasActiveSessionForClass(String classId, SessionAudience audience) {
        List<LiveSessionClass> mappings = LiveSessionClass.list("classId", classId);
        for (LiveSessionClass mapping : mappings) {
            LiveSession session = LiveSession.findById(mapping.sessionId);
            if (session != null && session.status == LiveSession.SessionStatus.LIVE && session.audience == audience) {
                return true;
            }
        }
        return LiveSession.count(
                "classId = ?1 and status = ?2 and audience = ?3",
                classId,
                LiveSession.SessionStatus.LIVE,
                audience
        ) > 0;
    }

    private void expireStaleLiveSessions() {
        if (liveSessionExpirationHours <= 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusHours(liveSessionExpirationHours);
        List<LiveSession> staleSessions = LiveSession.list(
                "status = ?1 and startedAt < ?2",
                LiveSession.SessionStatus.LIVE,
                cutoff
        );
        for (LiveSession staleSession : staleSessions) {
            staleSession.status = LiveSession.SessionStatus.ENDED;
            staleSession.endedAt = now;

            List<SessionParticipant> participants = SessionParticipant.list(
                    "sessionId = ?1 and status = ?2",
                    staleSession.id,
                    SessionParticipant.ParticipantStatus.JOINED
            );
            for (SessionParticipant participant : participants) {
                participant.status = SessionParticipant.ParticipantStatus.LEFT;
                participant.leftAt = now;
            }
        }
    }

    private LiveSessionClass findAllowedClass(Long sessionId, String classIdOrLabel) {
        Classe resolvedClass = ClassLabelUtil.resolveClass(classIdOrLabel);
        String resolvedClassId = resolvedClass == null ? null : resolvedClass.id.toString();
        List<LiveSessionClass> mappings = LiveSessionClass.list("sessionId", sessionId);
        for (LiveSessionClass mapping : mappings) {
            if (resolvedClassId != null && resolvedClassId.equals(mapping.classId)) {
                return mapping;
            }
            if (ClassLabelUtil.matchesLoose(mapping.classLabel, classIdOrLabel)) {
                return mapping;
            }
        }
        return null;
    }

    private String generateAccessKey() {
        String key;
        do {
            key = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        } while (LiveSession.count("accessKey = ?1 and status = ?2", key, LiveSession.SessionStatus.LIVE) > 0);
        return key;
    }

    private String normalizeAccessKey(String accessKey) {
        return accessKey == null ? "" : accessKey.trim().toUpperCase(Locale.ROOT);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static class VerificationResult {
        final boolean ok;
        final String classe;

        VerificationResult(boolean ok, String classe) {
            this.ok = ok;
            this.classe = classe;
        }
    }

    private VerificationResult verifyStudent(String numeroIdentifiant) {
        try {
            URI uri = buildSchoolUri("eleve/verify/" + URLEncoder.encode(numeroIdentifiant.trim(), StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(15)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                if (response.statusCode() >= 500) throw new ServiceUnavailableException("School service unavailable");
                return new VerificationResult(false, null);
            }
            JsonNode node = objectMapper.readTree(response.body());
            String classe = node.hasNonNull("classe") ? node.get("classe").asText() : null;
            boolean registered = node.hasNonNull("numeroIdentifiant") && node.hasNonNull("cleEcole")
                    && !node.get("cleEcole").asText().isBlank() && !isBlank(classe);
            return new VerificationResult(registered, classe);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceUnavailableException(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("School service unavailable").build(), e);
        } catch (IOException e) {
            throw new ServiceUnavailableException(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("School service unavailable").build(), e);
        }
    }

    private boolean verifyTeacher(String numeroIdentifiant) {
        try {
            URI uri = buildSchoolUri("enseignant/verify/" + numeroIdentifiant);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private URI buildSchoolUri(String path) {
        String base = schoolServerBaseUrl.endsWith("/") ? schoolServerBaseUrl : schoolServerBaseUrl + "/";
        return URI.create(base + path);
    }

    private boolean isInspectorRoleAllowed(Agent agent, OnlineRole role) {
        if (role == OnlineRole.INSPECTOR_STUDENT) {
            return agent.role == 19;
        }
        if (role == OnlineRole.INSPECTOR_TEACHER) {
            return agent.role == 20;
        }
        if (role == OnlineRole.INSPECTOR_STREAMING) {
            // Rôle 21 = Inspecteur vidéo streaming (cours en ligne).
            return agent.role == 21;
        }
        return OnlineRoleMapper.isInspectorRole(agent.role);
    }

    private boolean isInspectorAssignedToClass(Long inspectorId, UUID classId) {
        if (inspectorId == null || classId == null) {
            return false;
        }
        List<InspecteurCours> assignments = InspecteurCours.list("idInspecteur", inspectorId);
        for (InspecteurCours assignment : assignments) {
            if (assignment.classe != null && assignment.classe.contains(classId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInspectorRole(OnlineRole role) {
        return role == OnlineRole.INSPECTOR || role == OnlineRole.INSPECTOR_STUDENT || role == OnlineRole.INSPECTOR_TEACHER
                || role == OnlineRole.INSPECTOR_STREAMING;
    }

    private boolean isAudienceAllowed(SessionAudience audience, OnlineRole role) {
        if (role == OnlineRole.STUDENT) {
            return audience != SessionAudience.TEACHER;
        }
        if (role == OnlineRole.TEACHER) {
            return audience != SessionAudience.STUDENT;
        }
        return true;
    }
}
