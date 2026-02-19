package org.epst.controlleurs;

import org.epst.models.annonces.Annonce;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @Path("image")
    @GET
    @Transactional
    public Response getImage(@QueryParam("id") Long id) {
        //
        Annonce annonce = Annonce.findById(id);
        //
        return Response.ok().entity(annonce.image).build();
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
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
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
