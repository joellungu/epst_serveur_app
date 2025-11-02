package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.CoursDirect;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/courss")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoursResource {

    @GET
    public List<CoursDirect> listAll() {
        return CoursDirect.listAll();
    }

    @GET
    @Path("/{id}")
    public CoursDirect get(@PathParam("id") Long id) {
        return CoursDirect.findById(id);
    }

    @POST
    @Transactional
    public Response create(CoursDirect entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public CoursDirect update(@PathParam("id") Long id, CoursDirect updated) {
        CoursDirect entity = CoursDirect.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        CoursDirect entity = CoursDirect.findById(id);
        if (entity != null) entity.delete();
    }
}
