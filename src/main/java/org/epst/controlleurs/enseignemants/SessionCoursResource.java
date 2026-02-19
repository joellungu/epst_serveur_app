package org.epst.controlleurs.enseignemants;


import org.epst.models.EnseignementDirect.SessionCours;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/sessioncours")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionCoursResource {

    @GET
    public List<SessionCours> listAll() {
        return SessionCours.listAll();
    }

    @GET
    @Path("/{id}")
    public SessionCours get(@PathParam("id") Long id) {
        return SessionCours.findById(id);
    }

    @POST
    @Transactional
    public Response create(SessionCours entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public SessionCours update(@PathParam("id") Long id, SessionCours updated) {
        SessionCours entity = SessionCours.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        SessionCours entity = SessionCours.findById(id);
        if (entity != null) entity.delete();
    }
}
