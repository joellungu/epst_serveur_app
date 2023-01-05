package org.epst.controlleurs;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.epst.beans.Piecejointe;
import org.epst.beans.Plainte;
import org.epst.models.ModelPlainte;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresMetier;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
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
    @Inject
    PalmaresMetier palmaresMetier;
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

    @Path("palmares/")
    @POST()
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savetPlainte(byte[] piecejointe) {
        //
        Thread th = new Thread() {
            public void run() {
                String s = new String(piecejointe, StandardCharsets.UTF_8);
                try {
                    analyse(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("piecejointe: "+piecejointe.length+"// type ");
            }
        };
        //
        th.start();
        //
        return Response.status(Response.Status.CREATED).entity("Ok").build();
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
                                                .put("Email", "numeriquedgc@gmail.com")
                                                .put("Name", "Pierre Museghe")))
                                .put(Emailv31.Message.SUBJECT, "Violence basé sur le genre")
                                .put(Emailv31.Message.TEXTPART, "Contenu:\n"+message)
                                .put(Emailv31.Message.HTMLPART, "<h3>Voici le lien du fichier<br><h4>\""+message+"\"</h4><br><a href=\"https://epst.herokuapp.com/piecejointe/"+from+"\">Lire la piece jointe</a>!</h3>")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }
    //
    public void analyse(String e) throws IOException {
        //Reader in = new FileReader(fileLocation);
        int c = 0;
        StringReader srd = new StringReader(e);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(srd);
        for (CSVRecord record : records) {
            String col1 = record.get(1);
            String col2 = record.get(2);
            String col3 = record.get(3);
            String col4 = record.get(4);
            String col5 = record.get(5);
            String col6 = record.get(6);
            String col7 = record.get(7);
            String col8 = record.get(8);
            String col9 = record.get(9);
            String col10 = record.get(10);
            String col11 = record.get(11);
            String col12 = record.get(12);
            String col13 = record.get(13);
            String col14 = record.get(14);
            String col15 = "2021-2022";
            //String col16 = record.get(15);
            Palmares p = new Palmares();
            p.setNomprovince(col1);
            p.setCodeprovince(col2);
            p.setNomcentre(col3);
            p.setCodecentre(Integer.parseInt(col4));
            p.setOption(col5);
            p.setCodeoption(Integer.parseInt(col6));
            p.setNomecole(col7);
            p.setCodeecole(Integer.parseInt(col8));
            p.setOrdreecole(Integer.parseInt(col9));
            p.setCodegestion(Integer.parseInt(col10));
            p.setCodecandidat(col11);
            p.setNomcandidat(col12);
            p.setSexe(col13);
            p.setNote(col14);
            p.setAnneescolaire(col15);
            //
            palmaresMetier.saveDemande(p);
            //
            System.out.println("col1: "+record.toString()+"");
            System.out.println("conte: "+c+"");
            c++;
            //System.out.println("col1: "+col1+" -- "+col2+" -- "+col3+" -- "+col4+" -- "+col5+" -- "+col6+" -- "+col7
              //      +" -- "+col8+" -- "+col9+" -- "+col10+" -- "+col11+" -- "+col12+" -- "+col13+" -- "+col14+" -- "+col15+" -- ");
        }
    }

}
