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
import java.util.HashMap;
import java.util.List;

@Path("cours")
public class CoursController {


    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(Cours.listAll()).build();
    }

    @Path("allcours")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response all(@QueryParam("categorie") String categorie, @QueryParam("cls") int cls,
                        @QueryParam("typeFormation") String typeFormation) {
        //
        HashMap params = new HashMap();
        params.put("categorie", categorie);
        params.put("classe", cls);
        params.put("propriete", typeFormation);
        //
        List<Cours> l = Cours.listAll();
        l.forEach((c)->{
            System.out.println("Cours: "+c.classe);
            System.out.println("Cours: "+c.categorie);
        });
        //
        List<Cours> coursList = Cours.find("categorie =: categorie and classe =: classe and propriete =: propriete", params).list();
        //
        coursList.forEach(cours -> {
            cours.data = new byte[0];
        });
        //
        return Response.ok(coursList).build();
    }

    //
    @Path("notion")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notion(
            @QueryParam("cours") String cours,
            @QueryParam("categorie") String categorie,
            @QueryParam("banche") String banche,
            //@QueryParam("type") String type,
            @QueryParam("classe") int classe
    ) {
        //
        HashMap params = new HashMap();
        params.put("cours", cours);
        params.put("categorie", categorie);
        params.put("banche", banche);
        //params.put("type", type);
        params.put("classe", classe);
        //
        List<Cours> courss = Cours.find("cours =: cours and categorie =: categorie and banche =: banche and " +
                "classe =: classe", params).list();
        //
        courss.forEach((c)->{
            c.data = new byte[0];
        });
        //
        if(cours != null){
            return Response.ok(courss).build();
        }else{
            return Response.status(404).build();
        }
        //

    }


    @Path("checkcours")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkcours(
            @QueryParam("cours") String cours,
            @QueryParam("categorie") String categorie,
            @QueryParam("banche") String banche,
            @QueryParam("type") String type,
            @QueryParam("notion") String notion,
            @QueryParam("classe") int classe,
            @QueryParam("propriete") String propriete
                               ) {
        //
        HashMap params = new HashMap();
        params.put("cours", cours);
        params.put("categorie", categorie);
        params.put("banche", banche);
        params.put("type", type);
        params.put("notion", notion);
        params.put("classe", classe);
        params.put("propriete", propriete);
        //
        Cours cour = (Cours) Cours.find("cours =: cours and categorie =: categorie and banche =: banche and " +
                "type =: type and notion =: notion and classe =: classe", params).firstResult();
        //
        //
        if(cours != null){
            return Response.ok(cour.id).build();
        }else{
            return Response.status(404).build();
        }
        //

    }

    @Path("coursDataPdf.pdf")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response coursData(@QueryParam("id") Long id) {
        //
        Cours cours = Cours.findById(id);
        //
        return Response.ok(cours.data).build();
    }


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
        //c.setClasse(cours.getClasse());
        //c.setMatiere(c.getMatiere());
        return Response.ok(c).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ajouterCours(Cours cours) {
        cours.persist();
        return Response.ok(cours.id).build();
    }

    @Path("media")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response ajouterMedia(@QueryParam("id") Long id, byte[] data) {
        Cours cours = Cours.findById(id);
        if(cours != null){
            cours.data = data;
            cours.persist();
        }
        return Response.ok().build();
    }



    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteClasse(@QueryParam("id") Long id) {
        //
        Cours.deleteById(id);
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
