package org.epst.controlleurs;

import org.epst.models.annonces.Annonce;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("annonce")
public class AnnonceController {

    @Path("all")
    @GET
    @Transactional
    public Response getAllAnnonces() {
        //
        List<Annonce> annonces = Annonce.listAll();
        //
        List<Long> annonceIds = new LinkedList<>();
        //
        annonces.forEach(an -> {
            annonceIds.add(an.id);
        });
        //
        return Response.ok().entity(annonceIds).build();
    }

    //
    @POST
    @Transactional
    public Response saveAnnonce(Annonce annonce) {
        //
        annonce.persist();
        //
        return Response.ok(annonce.id).build();
    }

    @PUT
    @Transactional
    public Response updateAnnonce(@QueryParam("id") Long id, byte[] image) {
        //
        Annonce annonce = Annonce.findById(id);
        annonce.image = image;
        annonce.persist();
        //
        return Response.ok().build();
    }
    //
    @DELETE
    @Transactional
    public Response deleteAnnonce(@QueryParam("id") Long id) {
        //
        Annonce.deleteById(id);
        //
        return Response.ok().build();
    }
}
