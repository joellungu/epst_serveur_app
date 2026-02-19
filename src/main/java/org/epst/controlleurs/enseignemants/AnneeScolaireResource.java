package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.AnneeScolaire;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/anneescolaire")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnneeScolaireResource {

    @GET
    public List<AnneeScolaire> listAll() {
        return AnneeScolaire.listAll();
    }

    @GET
    @Path("/{id}")
    public AnneeScolaire get(@PathParam("id") Long id) {
        return AnneeScolaire.findById(id);
    }

    @POST
    @Transactional
    public Response create(AnneeScolaire entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public AnneeScolaire update(@PathParam("id") Long id, AnneeScolaire updated) {
        AnneeScolaire entity = AnneeScolaire.findById(id);
        if (entity == null) throw new NotFoundException();
        // TODO: Mettre à jour les champs nécessaires
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        AnneeScolaire entity = AnneeScolaire.findById(id);
        if (entity != null) entity.delete();
    }
}
