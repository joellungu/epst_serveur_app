package org.epst.controlleurs;

import java.util.List;
import java.util.Random;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.epst.beans.Piecejointe;
import org.epst.beans.Plainte;
import org.epst.models.ModelPlainte;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/piecejointe")
public class PiecejointeControlleur {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    ModelPlainte modelPlainte = new ModelPlainte();
    //
    
    @Path("/all/{piecejointe_id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Piecejointe> getAllPiecejointe(@PathParam("piecejointe_id") Long piecejointe_id) {
        //
        List<Piecejointe> listeU = modelPlainte.getAllPiecejointe(piecejointe_id);
        //listeU.forEach((u)->{
        //  System.out.println("Element nom: "+u.nom);
        //});
        //
        //Todo todo = new Todo();
        //todo.setSummary("Application JSON Todo Summary");
        //todo.setDescription("Application JSON Todo Description");
        //
        //Todo todo2 = new Todo();
        //todo2.setSummary("Application JSON ");
        //todo2.setDescription("Application JSON ");

        return listeU;//Arrays.asList(todo,todo2);
    }

    @Path("/{piecejointe_id}")
    @GET()
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("piecejointe_id") String piecejointe_id) {
        //
        String[] l = piecejointe_id.split(".");
        Long id = Long.parseLong(l[0]);
        byte[] piece = modelPlainte.getPieceJointe(id);
        //listeU.forEach((u)->{
        //  System.out.println("Element nom: "+u.nom);
        //});
        //
        //Todo todo = new Todo();
        //todo.setSummary("Application JSON Todo Summary");
        //todo.setDescription("Application JSON Todo Description");
        //
        //Todo todo2 = new Todo();
        //todo2.setSummary("Application JSON ");
        //todo2.setDescription("Application JSON ");
        return piece;
    }


    @Path("/{piecejointe_id}/{type}")
    @POST()
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savetPlainte(@PathParam("piecejointe_id") Long piecejointe_id, @PathParam("type") String type, byte[] piecejointe) {
        Long id = getId();
        int t = modelPlainte.savePiecejointe(id,piecejointe_id, type, piecejointe);
        //
        System.out.println("piecejointe_id: "+piecejointe_id+"// type "+type+"");
        System.out.println("votre element piece jointe_______: "+
        piecejointe_id+":\n__:"+
        type+":\n__:"
        );
        //
        //
            Thread th = new Thread() {
                public void run() {
                    //
                    System.out.println("Id: "+piecejointe_id);
                    //
                    Plainte u = modelPlainte.getPlainteById(piecejointe_id);
                    //
                    System.out.println("Message: "+u.getMessage());
                    //
                    if(u.getId_tiquet().equals("1")) {
                        try {
                            send("" + id + "." + type, u.getMessage());
                            System.out.println("Email envoyé");
                        } catch (MailjetException e) {
                            System.out.println("Email non envoyé");
                            throw new RuntimeException(e);
                        } catch (MailjetSocketTimeoutException e) {
                            System.out.println("Email non envoyé");
                            throw new RuntimeException(e);
                        }
                        //
                    }else{
                        System.out.println("La condition est faut!");
                    }
                }
            };
            //
            th.start();
            //
        ObjectNode json = mapper.createObjectNode();
        //
        //json.put("status", "ok");
        json.put("save", t);
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    public void send(String from, String message) throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        //
        //StringBuilder message = new StringBuilder();
        //
        client = new MailjetClient("6f319c7eabca73a75926580bf1291102",
                "7f4ef3362f04f20e9fcbbdaf5fea596e", new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "mmuseghe@gmail.com")
                                        .put("Name", "Pierre Museghe"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "mmuseghe@gmail.com")
                                                .put("Name", "Pierre Museghe")))
                                .put(Emailv31.Message.SUBJECT, "Violence basé sur le genre")
                                .put(Emailv31.Message.TEXTPART, "Contenu:\n"+message)
                                .put(Emailv31.Message.HTMLPART, "<h3>Voici le lien du fichier<br><h4>\""+message+"\"</h4><br><a href=\"http://192.168.1.67:8080/piecejointe/"+from+"\">Lire la piece jointe</a>!</h3>")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }
}
