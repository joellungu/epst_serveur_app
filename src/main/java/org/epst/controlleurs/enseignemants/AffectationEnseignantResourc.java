package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.AffectationEnseignant;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/affectationenseignant")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AffectationEnseignantResourc {
    @GET
    public List<AffectationEnseignant> listAll() {
        return AffectationEnseignant.listAll();
    }

    @GET
    @Path("/{id}")
    public AffectationEnseignant get(@PathParam("id") Long id) {
        return AffectationEnseignant.findById(id);
    }

    @POST
    @Transactional
    public Response create(AffectationEnseignant entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public AffectationEnseignant update(@PathParam("id") Long id, AffectationEnseignant updated) {
        AffectationEnseignant entity = AffectationEnseignant.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        AffectationEnseignant entity = AffectationEnseignant.findById(id);
        if (entity != null) entity.delete();
    }
}
