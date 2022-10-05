package org.epst.controlleurs;

import org.epst.models.Cours.Cours;
import org.epst.models.Cours.Video;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Path("coure")
public class CoursController {

    /*
    @Path("ajouter")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ajouterClasse(Cours cours) {
        cours.persist();
        return Response.ok(Cours.listAll()).build();
    }
    */
    /*
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(Cours.listAll()).build();
    }
    */

/*
    @Path("update")
    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCours(Cours cours) {
        Cours c = Cours.findById(cours.id);
        if(c == null){
            return Response.serverError().build();
        }
        c.setClasse(cours.getClasse());
        c.setMatiere(c.getMatiere());
        return Response.ok(c).build();
    }*/

    @Path("ajoutervideo")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void ajouterVideo(Video video) {
        //
        //video.persist();
        //
    }


    @Path("lire/{classe}/{matiere}/{notion}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream lireVideo(@PathParam("classe") String classe,
                                 @PathParam("matiere") String matiere,
                                 @PathParam("notion") String notion) {
        File video = new File("C:\\Users\\Pierre\\Downloads\\Koffi Olomide - Ligablo IGF (Clip Officiel).mp4");
        try {
            FileInputStream fis = new FileInputStream(video);
            return fis;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
