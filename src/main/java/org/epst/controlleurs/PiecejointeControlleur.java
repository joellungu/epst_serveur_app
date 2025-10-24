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
import org.epst.models.PieceJointe;
import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresMetier;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.inject.Inject;
import jakarta.transaction.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/piecejointe")
public class PiecejointeControlleur {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    ModelPlainte modelPlainte = new ModelPlainte();
    //@Inject
    //PalmaresMetier palmaresMetier;
    //
    @Inject
    TransactionManager tm;
    
    @Path("/all/{piecejointe_id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<PieceJointe> getAllPiecejointe(@PathParam("piecejointe_id") Long piecejointe_id) {
        //
        List<PieceJointe> listeU = PieceJointe.list("piecejointe_id",piecejointe_id);

        return listeU;//Arrays.asList(todo,todo2);
    }

    @Path("/{piecejointe_id}")
    @GET()
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPiecejointe(@PathParam("piecejointe_id") String piecejointe_id) {
        //
        try {
            String[] l = piecejointe_id.split("\\.");
            System.out.println(l[0]+" valeur:  "+piecejointe_id);
            Long id = Long.parseLong(l[0]);//
            PieceJointe pieceJointe = PieceJointe.findById(id);
            //byte[] piece = pieceJointe.donne;
            //
            //modelPlainte.getPieceJointe(id);
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
            return Response.ok(pieceJointe.donne).build();
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
            return Response.status(405,"Erreur du à: "+ex.getMessage()).build();
        }
    }


    @Path("/{piecejointe_id}/{type}")
    @POST()
    @Transactional
    //@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savetPlainte(@PathParam("piecejointe_id") long piecejointe_id,
                                 @PathParam("type") String type,
                                 byte[] fichier) {
        PieceJointe pieceJointe = new PieceJointe();
        pieceJointe.type = type;
        pieceJointe.piecejointe_id = piecejointe_id;
        pieceJointe.donne = fichier;
        //
        pieceJointe.persist();
        //
        //
            Thread th = new Thread() {
                public void run() {
                    //

                    //@Inject UserTransaction transaction;
                    //
                    System.out.println("Id: "+pieceJointe.piecejointe_id);
                    //
                    try {
                        tm.begin();
                            //
                            //Plainte u = modelPlainte.getPlainteById(pieceJointe.piecejointe_id);
                            Plainte u = Plainte.findById(pieceJointe.piecejointe_id);
                            u.piecejointe_id = pieceJointe.piecejointe_id;
                        tm.commit();
                    //
                    System.out.println("Message: "+u.getMessage());
                    //
                    if(u.getId_tiquet().equals("1")) {
                        try {
                            send("" + pieceJointe.id + "." + pieceJointe.type, u.getMessage());
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
                    } catch (NotSupportedException e) {
                        throw new RuntimeException(e);
                    } catch (SystemException e) {
                        throw new RuntimeException(e);
                    } catch (HeuristicRollbackException e) {
                        throw new RuntimeException(e);
                    } catch (HeuristicMixedException e) {
                        throw new RuntimeException(e);
                    } catch (RollbackException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            //
            th.start();
            //
        ObjectNode json = mapper.createObjectNode();
        //
        //json.put("status", "ok");
        json.put("save", pieceJointe.id);
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
        //c5e23aacf7242cf0146e3357ce692ad2
        //b84f5686e1cd527690705747afd878e8
        //"Email", "vbgepst@gmail.com"
        //Nom: vbgepst
        //////////////////
        //6f319c7eabca73a75926580bf1291102
        //7f4ef3362f04f20e9fcbbdaf5fea596e
        //"Email", "mmuseghe@gmail.com"
        //Nom: Pierre Museghe
        client = new MailjetClient(
                "386345e2da43826df06afa2e0838d66f",
                "945b5ef99c70bc5bb55a6ef5f0ef69ae",
                new ClientOptions("v3.1")
        );
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "numeriquedgc@gmail.com")
                                        .put("Name", "DGC / EDUC-NC"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "mmuseghe@gmail.cd")//
                                                .put("Name", "Mugi Musege")
                                        )
                                )
                                .put(Emailv31.Message.CC, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "lungujoel138@gmail.com")//
                                                .put("Name", "Lungu Joel")
                                        )
                                )
                                /*
                                .put(Emailv31.Message.CC, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "lungujoel138@gmail.com")//
                                                .put("Name", "joel lungu")
                                        )
                                )
                                */

                                //plaintes_eashs@alloecolemgp.cd
                                .put(Emailv31.Message.SUBJECT, "Violence basée sur le genre")
                                .put(Emailv31.Message.TEXTPART, "Contenu:\n"+message)
                                .put(Emailv31.Message.HTMLPART, "<h3>Voici le lien du fichier<br><h4>\""+message+"\"</h4><br><a href=\"https://epst-a4bc31000994.herokuapp.com/piecejointe/"+from+"\">Lire la piece jointe</a>!</h3>")));
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
            /*
            p.setNomprovince(col1);
            p.setCodeprovince(col2);
            p.setNomcentre(col3);
            p.setCodecentre(col4);
            p.setOption(col5);
            p.setCodeoption(col6);
            p.setNomecole(col7);
            p.setCodeecole(col8);
            p.setOrdreecole(Integer.parseInt(col9));
            p.setCodegestion(Integer.parseInt(col10));
            p.setCodecandidat(col11);
            p.setNomcandidat(col12);
            p.setSexe(col13);
            p.setNote(col14);
            p.setAnneescolaire(col15);
            */
            //
            //palmaresMetier.saveDemande(p);
            //
            System.out.println("col1: "+record.toString()+"");
            System.out.println("conte: "+c+"");
            c++;
            //System.out.println("col1: "+col1+" -- "+col2+" -- "+col3+" -- "+col4+" -- "+col5+" -- "+col6+" -- "+col7
              //      +" -- "+col8+" -- "+col9+" -- "+col10+" -- "+col11+" -- "+col12+" -- "+col13+" -- "+col14+" -- "+col15+" -- ");
        }
    }

}
