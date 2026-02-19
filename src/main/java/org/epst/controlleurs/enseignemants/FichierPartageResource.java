package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.FichierPartage;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/fichierpartage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FichierPartageResource {

    @GET
    public List<FichierPartage> listAll() {
        return FichierPartage.listAll();
    }

    @GET
    @Path("/{id}")
    public FichierPartage get(@PathParam("id") Long id) {
        return FichierPartage.findById(id);
    }

    @POST
    @Transactional
    public Response create(FichierPartage entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public FichierPartage update(@PathParam("id") Long id, FichierPartage updated) {
        FichierPartage entity = FichierPartage.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        FichierPartage entity = FichierPartage.findById(id);
        if (entity != null) entity.delete();
    }
}
