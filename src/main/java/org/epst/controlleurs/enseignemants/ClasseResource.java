package org.epst.controlleurs.enseignemants;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/classe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClasseResource {

    /*
    @GET
    public List<Classe> listAll() {
        return Classe.listAll();
    }

    @GET
    @Path("/{id}")
    public Classe get(@PathParam("id") Long id) {
        return Classe.findById(id);
    }

    @POST
    @Transactional
    public Response create(Classe entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Classe update(@PathParam("id") Long id, Classe updated) {
        Classe entity = Classe.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Classe entity = Classe.findById(id);
        if (entity != null) entity.delete();
    }
     */
}
