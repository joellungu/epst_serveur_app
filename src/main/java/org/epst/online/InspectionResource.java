package org.epst.online;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.Agent.Agent;

@Path("/online/inspections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InspectionResource {

    public static class CreateInspectionRequest {
        public Long sessionId;
        public Long classId;
        public String inspectorMatricule;
        public InspectorFocus inspectorFocus;
        public String observedTeacherMatricule;
        public Integer score;
        public String notes;
    }

    @POST
    @Transactional
    public Response create(CreateInspectionRequest request) {
        if (request == null || request.sessionId == null || request.inspectorMatricule == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

        Agent inspector = Agent.find("matricule", request.inspectorMatricule).firstResult();
        if (inspector == null || !OnlineRoleMapper.isInspectorRole(inspector.role)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Inspector not recognized").build();
        }

        InspectionReport report = new InspectionReport();
        report.sessionId = request.sessionId;
        report.classId = request.classId;
        report.inspectorMatricule = request.inspectorMatricule;
        report.inspectorRole = inspector.role;
        report.inspectorFocus = request.inspectorFocus != null ? request.inspectorFocus : InspectorFocus.STUDENTS;
        report.observedTeacherMatricule = request.observedTeacherMatricule;
        report.score = request.score != null ? request.score : 0;
        report.notes = request.notes;
        report.persist();

        return Response.status(Response.Status.CREATED).entity(report).build();
    }

    @GET
    @Path("/session/{sessionId}")
    public Response listBySession(@PathParam("sessionId") Long sessionId) {
        return Response.ok(InspectionReport.list("sessionId", sessionId)).build();
    }

    @GET
    @Path("/inspector/{matricule}")
    public Response listByInspector(@PathParam("matricule") String matricule) {
        return Response.ok(InspectionReport.list("inspectorMatricule", matricule)).build();
    }
}
