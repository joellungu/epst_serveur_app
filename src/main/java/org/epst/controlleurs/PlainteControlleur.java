package org.epst.controlleurs;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.epst.beans.NoteTraitementBean;
import org.epst.beans.Plainte;
import org.epst.models.ModelNoteTraitement;
import org.epst.models.ModelPlainte;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
//import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/plainte")
public class PlainteControlleur {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    //ModelPlainte modelPlainte = new ModelPlainte();
    
    @Path("/{id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Plainte getPlainte(@PathParam("id") Long id) {
        Plainte u = Plainte.findById(id);
        //Todo todo = new Todo();
        //todo.setSummary(id);
        //todo.setDescription("Application JSON Todo Description");
        return u;
    }

    @Path("/all/{statut}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Plainte> getAllPlaintes(@PathParam("statut") int statut) {
        //
        System.out.println("Le id_statut: "+statut);
        List<Plainte> listeU = new LinkedList<>();
        try{
            listeU = Plainte.listAll();
            listeU = listeU.stream().filter((r)-> r.id_statut == statut ).collect(Collectors.toList());
            //("id_statut",statut);
            listeU.forEach((e)->{System.out.println("le id vaut: "+e.id);});
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }

        return listeU;//Arrays.asList(todo,todo2);
    }

    @Path("/all")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Plainte> getAllPlaintess() {
        //
        //System.out.println("Le id_statut: "+statut);
        List<Plainte> listeU = new LinkedList<>();
        try{
            listeU = Plainte.listAll();
            //listeU = listeU.stream().filter((r)-> r.id_statut == statut ).collect(Collectors.toList());
            //("id_statut",statut);
            //listeU.forEach((e)->{System.out.println("le id vaut: "+e.id);});
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }

        return listeU;//Arrays.asList(todo,todo2);
    }


    @Path("/reference/{reference}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Plainte> getAllPlaintesR(@PathParam("reference") String reference) {
        //
        List<Plainte> listeU = Plainte.list("reference",reference);

        return listeU;//Arrays.asList(todo,todo2);
    }

    //@Path("")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response savetPlainte(Plainte plainte) {
        plainte.persist();

        System.out.println("votre element: "+
                plainte.getTelephone()+":\n__:"+
                plainte.getDate()+":\n__:"+
                plainte.getEmail()+":\n__:"+
                plainte.getEnvoyeur()+":\n__:"
        );
        //

        ObjectNode json = mapper.createObjectNode();
        json.put("save", plainte.id);
        
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updatePlainte(Plainte plainte) {
        Plainte plainte1 = Plainte.findById(plainte.id);
        if(plainte1 == null){
            return Response.serverError().build();
        }
        /*
        String envoyeur,
        String telephone,
        String email,
        String destinateur,
        String id_tiquet,
        String message,
        String id_statut,
        String piecejointe_id,
        String reference,
        String date,
        String province
         */
        //
        plainte1.envoyeur = plainte.envoyeur;
        plainte1.telephone = plainte.telephone;
        plainte1.email = plainte.email;
        plainte1.destinateur = plainte.destinateur;
        plainte1.id_tiquet = plainte.id_tiquet;
        plainte1.message = plainte.message;
        plainte1.id_statut = plainte.id_statut;
        plainte1.piecejointe_id = plainte.piecejointe_id;
        plainte1.reference = plainte.reference;
        plainte1.date = plainte.date;
        plainte1.province = plainte.province;
        //
        ObjectNode json = mapper.createObjectNode();
        //
        json.put("mettre à jour", plainte1.id);
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @Path("/noter")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response noterMa(NoteTraitementBean noteTraitementBean){//
        noteTraitementBean.persist();
        ObjectNode json = mapper.createObjectNode();
        json.put("status", "v");
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

}
/*
"<h3>Voici le lien du fichier<br><br><a href=\"https://www.mailjet.com/\">Mailjet</a>!</h3><h4>"+message+"</h4>"
<video width="320" height="240" autoplay><source src="movie.mp4" type="https://flutter.github.io/assets-for-api-docs/assets/videos/bee.mp4"></video>
 */