package org.epst.controlleurs;

import jakarta.ws.rs.core.Link;
import org.epst.models.ClasseModel;
import org.epst.models.Cours.Cours;
import org.epst.models.Cours.Video;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Path("cours")
public class CoursController {

    private class CoursClasse {
        public String cours;
        public Long idCours;
        public Long idClasse;
    }

    @GET
    @Path("one")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCoursById(@QueryParam("id") Long idClasse){
        //
        System.out.println("Le id: "+idClasse);
        List<CoursClasse> coursClasses = new LinkedList<>();
        List<Cours> cours = Cours.find("idClasse", idClasse).list();
        //
        if(cours == null){
            return Response.status(405).build();
        }
        CoursClasse coursClasse = new CoursClasse();
        //
        cours.forEach((c) -> {
            CoursClasse cc = new CoursClasse();
            cc.cours = c.cours;
            cc.idCours = c.id;
            cc.idClasse = c.idClasse;
            coursClasses.add(cc);
        });
        //
        return Response.ok(coursClasses).build();
    }

    @GET
    @Path("classe")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCoursById(@QueryParam("classe") String classe, @QueryParam("niveau") String niveau){
        //
        System.out.println("Le id: "+classe);
        List<CoursClasse> coursClasses = new LinkedList<>();
        HashMap params = new HashMap();
        params.put("cls", classe);
        params.put("categorie", niveau);
        //
        List<Cours> cours = Cours.find("cls =: cls and categorie =: categorie", params).list();
        //
        if(cours == null){
            return Response.status(405).build();
        }
        CoursClasse coursClasse = new CoursClasse();
        //
        cours.forEach((c) -> {
            CoursClasse cc = new CoursClasse();
            cc.cours = c.cours;
            cc.idCours = c.id;
            cc.idClasse = c.idClasse;
            coursClasses.add(cc);
        });
        //
        return Response.ok(coursClasses).build();
    }


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
    public Response all(@QueryParam("categorie") String categorie, @QueryParam("idClasse") Long idClasse,
                        @QueryParam("typeFormation") String typeFormation) {
        //
        HashMap params = new HashMap();
        params.put("categorie", categorie);
        params.put("idClasse", idClasse);
        params.put("propriete", typeFormation);
        //
        System.out.println("categorie: "+categorie);
        //
        List<Cours> l = Cours.listAll();
        //
        l.forEach((c)->{
            System.out.println("Cours: "+c.idClasse);
            System.out.println("Cours: "+c.categorie);
        });
        //
        List<Cours> coursList = Cours.find("categorie =: categorie and idClasse =: idClasse and propriete =: propriete", params).list();
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
        params.put("cls", classe);
        //params.put("propriete", propriete);
        //
        System.out.println("cours =: "+cours+" and categorie =: "+categorie+" and banche =: "+banche+" and " +
                "type =: "+type+" and notion =: "+notion+" and classe =: "+classe);
        //
        List<Cours> courss = Cours.listAll();

        List<Cours> co = courss.stream()
                .filter(c -> params.get("cours") == null ||
                        c.cours.toLowerCase().equalsIgnoreCase(params.get("cours").toString()))
                .filter(c -> params.get("categorie") == null ||
                        c.categorie.toLowerCase().equalsIgnoreCase(params.get("categorie").toString()))
                .filter(c -> params.get("banche") == null ||
                        c.banche.equalsIgnoreCase(params.get("banche").toString()))
                .filter(c -> params.get("type") == null ||
                        c.type.equalsIgnoreCase(params.get("type").toString()))
                .filter(c -> params.get("notion") == null ||
                        c.notion.equalsIgnoreCase(params.get("notion").toString()))
                .filter(c -> params.get("cls") == null ||
                        c.cls == (int) params.get("cls"))
                .toList();
        //
        System.out.println("Data: "+co.get(0).id);
        //
        return Response.ok(co.get(0).id).build();
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

    public String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

}
