package org.epst.online;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.Classe;

import java.time.LocalDateTime;
import java.util.List;

@Path("/online/schedules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OnlineScheduleResource {

    public static class CreateScheduleRequest {
        public String classId;
        public String title;
        public SessionAudience audience;
        public LocalDateTime startsAt;
        public LocalDateTime endsAt;
        public String createdByMatricule;
    }

    @POST
    @Transactional
    public Response create(CreateScheduleRequest request) {
        if (request == null || request.classId == null || request.startsAt == null || request.endsAt == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

        Classe resolvedClass = ClassLabelUtil.resolveClass(request.classId);
        if (resolvedClass == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Class not recognized").build();
        }

        OnlineSchedule schedule = new OnlineSchedule();
        schedule.classId = resolvedClass.id.toString();
        schedule.title = request.title != null ? request.title : "Cours";
        schedule.audience = request.audience != null ? request.audience : SessionAudience.STUDENT;
        schedule.startsAt = request.startsAt;
        schedule.endsAt = request.endsAt;
        schedule.createdByMatricule = request.createdByMatricule;
        schedule.createdAt = LocalDateTime.now();
        schedule.persist();

        return Response.status(Response.Status.CREATED).entity(schedule).build();
    }

    @GET
    @Path("/class/{classId}")
    public Response listByClass(@PathParam("classId") String classId, @QueryParam("audience") SessionAudience audience) {
        Classe resolvedClass = ClassLabelUtil.resolveClass(classId);
        if (resolvedClass == null) {
            return Response.ok(List.of()).build();
        }
        String resolvedClassId = resolvedClass.id.toString();
        List<OnlineSchedule> schedules;
        if (audience != null) {
            schedules = OnlineSchedule.list("classId = ?1 and audience = ?2 order by startsAt", resolvedClassId, audience);
        } else {
            schedules = OnlineSchedule.list("classId = ?1 order by startsAt", resolvedClassId);
        }
        return Response.ok(schedules).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        OnlineSchedule schedule = OnlineSchedule.findById(id);
        if (schedule == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(schedule).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = OnlineSchedule.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
