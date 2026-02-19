package org.epst.controlleurs.enseignemants;


import org.epst.models.EnseignementDirect.Evaluation;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/evaluation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {

    @GET
    public List<Evaluation> listAll() {
        return Evaluation.listAll();
    }

    @GET
    @Path("/{id}")
    public Evaluation get(@PathParam("id") Long id) {
        return Evaluation.findById(id);
    }

    @POST
    @Transactional
    public Response create(Evaluation entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Evaluation update(@PathParam("id") Long id, Evaluation updated) {
        Evaluation entity = Evaluation.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Evaluation entity = Evaluation.findById(id);
        if (entity != null) entity.delete();
    }
}
