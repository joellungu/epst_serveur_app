package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.Travail;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/travail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TravailResource {

    @GET
    public List<Travail> listAll() {
        return Travail.listAll();
    }

    @GET
    @Path("/{id}")
    public Travail get(@PathParam("id") Long id) {
        return Travail.findById(id);
    }

    @POST
    @Transactional
    public Response create(Travail entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Travail update(@PathParam("id") Long id, Travail updated) {
        Travail entity = Travail.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Travail entity = Travail.findById(id);
        if (entity != null) entity.delete();
    }
}
