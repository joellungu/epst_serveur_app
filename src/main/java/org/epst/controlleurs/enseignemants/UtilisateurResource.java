package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.Utilisateur;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/utilisateur")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    @GET
    public List<Utilisateur> listAll() {
        return Utilisateur.listAll();
    }

    @GET
    @Path("/{id}")
    public Utilisateur get(@PathParam("id") Long id) {
        return Utilisateur.findById(id);
    }

    @POST
    @Transactional
    public Response create(Utilisateur entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Utilisateur update(@PathParam("id") Long id, Utilisateur updated) {
        Utilisateur entity = Utilisateur.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Utilisateur entity = Utilisateur.findById(id);
        if (entity != null) entity.delete();
    }
}
