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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Path("/online/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LiveSessionResource {
    @ConfigProperty(name = "school.server.base-url", defaultValue = "http://localhost:9090")
    String schoolServerBaseUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static class StartSessionRequest {
        public String classId;
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

    public static class EndSessionRequest {
        public Long sessionId;
        public String endedByMatricule;
        public OnlineRole endedByRole;
    }

    @POST
    @Path("/start")
    @Transactional
    public Response startSession(StartSessionRequest request) {
        if (request == null || request.classId == null || request.hostMatricule == null || request.hostRole == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

        Classe resolvedClass = ClassLabelUtil.resolveClass(request.classId);
        if (resolvedClass == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Class not recognized").build();
        }
        String resolvedClassId = resolvedClass.id.toString();

        if (isInspectorRole(request.hostRole)) {
            Agent agent = Agent.find("matricule", request.hostMatricule).firstResult();
            if (agent == null || !isInspectorRoleAllowed(agent, request.hostRole)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Inspector not recognized").build();
            }
            if (!isInspectorAssignedToClass(agent.id, resolvedClass.id)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Inspector not assigned to class").build();
            }
        }

        LiveSession session = new LiveSession();
        session.classId = resolvedClassId;
        session.title = request.title != null ? request.title : "Session";
        session.createdByMatricule = request.hostMatricule;
        session.hostMatricule = request.hostMatricule;
        session.startedAt = LocalDateTime.now();
        session.status = LiveSession.SessionStatus.LIVE;
        session.zegoRoomId = request.zegoRoomId != null
                ? request.zegoRoomId
                : "class-" + resolvedClassId + "-" + System.currentTimeMillis();
        session.recordingEnabled = request.recordingEnabled != null && request.recordingEnabled;
        session.maxParticipants = request.maxParticipants != null && request.maxParticipants > 0
                ? request.maxParticipants
                : 40;

        SessionAudience requestedAudience = request.audience != null ? request.audience : SessionAudience.STUDENT;
        if (request.hostRole == OnlineRole.INSPECTOR_STUDENT) {
            session.audience = SessionAudience.STUDENT;
        } else if (request.hostRole == OnlineRole.INSPECTOR_TEACHER) {
            session.audience = SessionAudience.TEACHER;
        } else {
            session.audience = requestedAudience;
        }

        long existingLive = LiveSession.count(
                "classId = ?1 and status = ?2 and audience = ?3",
                resolvedClassId,
                LiveSession.SessionStatus.LIVE,
                session.audience
        );
        if (existingLive > 0) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Class already has an active session for this audience")
                    .build();
        }

        session.persist();

        return Response.ok(session).build();
    }

    @POST
    @Path("/join")
    @Transactional
    public Response joinSession(JoinSessionRequest request) {
        if (request == null || request.sessionId == null || request.matricule == null || request.role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

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

        session.status = LiveSession.SessionStatus.ENDED;
        session.endedAt = LocalDateTime.now();

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
        return Response.ok(LiveSession.list("classId", resolvedClass.id.toString())).build();
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
            URI uri = buildSchoolUri("eleve/verify/" + numeroIdentifiant);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return new VerificationResult(false, null);
            }
            JsonNode node = objectMapper.readTree(response.body());
            String classe = node.hasNonNull("classe") ? node.get("classe").asText() : null;
            return new VerificationResult(true, classe);
        } catch (IOException | InterruptedException e) {
            return new VerificationResult(false, null);
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
        return role == OnlineRole.INSPECTOR || role == OnlineRole.INSPECTOR_STUDENT || role == OnlineRole.INSPECTOR_TEACHER;
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
