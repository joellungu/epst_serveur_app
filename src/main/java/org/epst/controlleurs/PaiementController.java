package org.epst.controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.epst.models.devise.Devise;
import org.epst.models.devise.DeviseMetier;
import org.epst.models.paiement.Paiement;
import org.epst.models.paiement.PaiementMetier;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Path("/paiement")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaiementController {

    @Inject
    PaiementMetier paiementMetier;

    @Inject
    DeviseMetier deviseMetier;

    public PaiementController(){}


    public String lancer(String devise, String telephone, Double m, String reference) {
        System.out.println("la devise: $"+devise+"lE MONTANT: $"+m);
        //String dev = devise == "USD" ? "USD":"CDF";
        //double montant = deviseMetier.conversion(m,1L, devise=="USD");
        //
        String dev = devise.equals("USD") ? "USD":"CDF";
        double montant = conversion(m,1L, devise.equals("USD"));
        //
        System.out.println("la devise: $"+devise+"lE MONTANT: $"+montant);
        //String urlPost = "http://41.243.7.46:3006/flexpay/api/rest/v1/paymentService";
        //////////////////http://41.243.7.46:3006/api/rest/v1/paymentService

        String urlPost = "https://backend.flexpay.cd/api/rest/v1/paymentService";
        String body = "{\n" +
                "  \"merchant\":\"Min_EDU-NC\"," +
                "  \"type\":1," +
                "  \"reference\": \""+reference+"\"," +
                "  \"phone\": \""+telephone+"\"," +
                "  \"amount\": \""+"1"+"\"," +
                "  \"currency\":\""+dev+"\"," +
                "  \"callbackUrl\":\"http://dgc-epst.uc.r.appspot.com\"" +
                "}";
        /*
        //montant
        String body = "{\n" +
                "  \"merchant\":\"EPSTAPP\"," +
                "  \"type\":1," +
                "  \"reference\": \""+reference+"\"," +
                "  \"phone\": \""+telephone+"\"," +
                "  \"amount\": \""+montant+"\"," +
                "  \"currency\":\""+dev+"\"," +
                "  \"callbackUrl\":\"http://dgc-epst.uc.r.appspot.com\"" +
                "}";
         */
        var requete = HttpRequest.newBuilder()
                .uri(URI.create(urlPost))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJcL2xvZ2luIiwicm9sZXMiOlsiTUVSQ0hBTlQiXSwiZXhwIjoxNzk0NzYxNDYzLCJzdWIiOiJlZGZiYTY0ZTYxNjM1NWMzYjdjZDJjYzZiZTA5NzMzYiJ9.1gps60CJKzY1CP8XgAEvx8ArRAfqD9v5a9PeJr4qA6c")
                //.header("Authorization","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MkkydjNXQkhUUVdpTlg4ejhQVSIsInJvbGVzIjpbIk1FUkNIQU5UIl0sImlzcyI6Ii9sb2dpbiIsImV4cCI6MTczNTY4NjAwMH0.b3H5IvM1cNtQ5I3Xz3Rf3hBO_pbgFgQ5VpdKrFUI3g0")
                //.header("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJcL2xvZ2luIiwicm9sZXMiOlsiTUVSQ0hBTlQiXSwiZXhwIjoxNzM3NTUyMDEwLCJzdWIiOiI4ZTE4NzJlODQwZTc5YjM5OWIxMDliMmYyNjk5YWY3YSJ9.co6sS0YEdCy3v3nja0NHvS5dYnMNmjZPJET_Ri7pB0E")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        var client = HttpClient.newHttpClient();
        try {
            var reponse = client.send(requete, HttpResponse.BodyHandlers.ofString());
            System.out.println(reponse.statusCode());
            System.out.println(reponse.body());
            return reponse.body();
        } catch (IOException e) {
            System.out.println(e);
            return "";
        } catch (InterruptedException e) {
            System.out.println(e);
            return "";
        }

        //return "";
    }

    public String checklancer(String orderNumer) {

        String urlPost = "https://backend.flexpay.cd/api/rest/v1/check/"+orderNumer;
        //////////////////http://41.243.7.46:3006/api/rest/v1/paymentService
        /* orderNumer
        // flexpay
        String body = "{\n" +
                "  \"merchant\":\"KACHIDI_BINARY\"," +
                "  \"type\":1," +
                "  \"reference\": \""+reference+"\"," +
                "  \"phone\": \""+telephone+"\"," +
                "  \"amount\": \""+m+"\"," +
                "  \"currency\":\""+devise+"\"," +
                "  \"callbackUrl\":\"http://dgc-epst.uc.r.appspot.com\"" +
                "}";
        */
        var requete = HttpRequest.newBuilder()
                .uri(URI.create(urlPost))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJcL2xvZ2luIiwicm9sZXMiOlsiTUVSQ0hBTlQiXSwiZXhwIjoxNzk0NzYxNDYzLCJzdWIiOiJlZGZiYTY0ZTYxNjM1NWMzYjdjZDJjYzZiZTA5NzMzYiJ9.1gps60CJKzY1CP8XgAEvx8ArRAfqD9v5a9PeJr4qA6c")
                .GET()
                //.POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        var client = HttpClient.newHttpClient();
        try {
            var reponse = client.send(requete, HttpResponse.BodyHandlers.ofString());
            System.out.println(reponse.statusCode());
            System.out.println(reponse.body());
            return reponse.body();
        } catch (IOException e) {
            System.out.println(e);
            return "";
        } catch (InterruptedException e) {
            System.out.println(e);
            return "";
        }

        //return "";
    }
    Toolkit toolkit;
    Timer timer;

    public void AnnoyingBeep() {
        String reponse = lancer("","",1.0,"");
        System.out.println(reponse);
        //reponse = "{"+reponse+"}";
        System.out.println("{"+reponse+"}");
        System.out.println(reponse.getClass());
        //ObjectMapper obj = new ObjectMapper();
        //
        try {
            JSONObject obj = new JSONObject(reponse);
            //JsonNode jn = obj.readTree(reponse);
            String c1 = obj.get("code").toString();
            String c2 = obj.get("message").toString();
            String c3 = obj.get("orderNumber").toString();

            System.out.println(obj.get("code").toString());
            System.out.println(obj.get("message").toString());
            System.out.println(obj.get("orderNumber").toString());
            System.out.println("____________________________________________");
            //
            if(c1.equals("0")) {
                toolkit = Toolkit.getDefaultToolkit();
                timer = new Timer();
                timer.schedule(new RemindTask(),
                        5 * 1000,        //initial delay
                        5 * 1000);  //subsequent rate
            }
            //
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    class RemindTask extends TimerTask {
        int numWarningBeeps = 5;
        public void run() {
            if (numWarningBeeps > 0) {
                toolkit.beep();
                System.out.println("Beep!");
                numWarningBeeps--;
            } else {
                toolkit.beep();
                System.out.println("Time's up!");
                timer.cancel(); // Not necessary because
                // we call System.exit
                //System.exit(0);   // Stops the AWT thread
                // (and everything else)
            }
        }
    }

    @Path("paie")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response lancerPaiment(Paiement paiement
                                  //HashMap paiement
    ) throws InterruptedException, JsonProcessingException, JsonProcessingException {
        /*
        //System.out.println("Le montant: "+paiement.amount);
        //System.out.println("Le devise: "+paiement.callbackurl);
        //System.out.println("Le phone: "+paiement.phone);
        //System.out.println("Le montant: ");
        */
        String reponse = "Paiement non éffectué";
        Response repData = Response.status(404).entity(reponse).build();
        //paiement.persist();
        //AnnoyingBeep();

        String rep = lancer(
                paiement.currency,
                paiement.phone,
                paiement.amount,
                paiement.reference
        );
        ObjectMapper obj = new ObjectMapper();
        JsonNode jsonNode = obj.readTree(rep);
        for(int x = 4; x < 5; x++){
            //
            //try {
                //
                //jsonNode
                //rep['orderNumber']
                JsonNode repCheck = obj.readTree(checklancer(jsonNode.get("orderNumber").asText()));
                //repCheck["transaction"]['status']
                System.out.println("Le status: "+repCheck.get("transaction").get("status").asText());
                if (repCheck.get("transaction").get("status").asText().equals("0") ||
                        repCheck.get("transaction").get("status").asInt() == (0)) {
                    reponse = "Paiement éffectué";
                    System.out.println("Status: "+reponse);
                    //commandeService.ticketList.forEach((t)-> t.persist());
                    paiement.persist();
                    repData = Response.status(200).entity(reponse).build();

                    break;
                }

                if (repCheck.get("transaction").get("status").asText().equals("1") ||
                        repCheck.get("transaction").get("status").asInt() == (1)) {
                    reponse = repCheck.get("message").asText();
                    System.out.println("Status: "+reponse);
                    repData = Response.status(404).entity(reponse).build();
                    break;
                }

                if (repCheck.get("transaction").get("status").asText().equals("3") ||
                        repCheck.get("transaction").get("status").asInt() == (3)) {
                    reponse = repCheck.get("message").asText();
                    System.out.println("Status: "+reponse);
                    repData = Response.status(404).entity(reponse).build();
                    break;
                }

                if (repCheck.get("transaction").get("status").asText().equals("4") ||
                        repCheck.get("transaction").get("status").asInt() == (4)) {
                    reponse = repCheck.get("message").asText();
                    System.out.println("Status: "+reponse);
                    repData = Response.status(404).entity(reponse).build();
                    break;
                }

                if (repCheck.get("transaction").get("status").asText().equals("5") ||
                        repCheck.get("transaction").get("status").asInt() == (5)) {
                    reponse = repCheck.get("message").asText();
                    System.out.println("Status: "+reponse);
                    repData = Response.status(404).entity(reponse).build();
                    //break;
                }

                System.out.println("La vérification: " + repCheck.asText());

                //TimeUnit.SECONDS.sleep(10);

            /*
            }catch (Exception ex){
                System.out.println("Erreur du à "+ex.getMessage());
                reponse = jsonNode.get("message").asText();
                repData = Response.status(404).entity(reponse).build();
                break;
            }
            */

            System.out.println("En attente kk");

            TimeUnit.SECONDS.sleep(10);
        }
        System.out.println("On attends plus");
        return repData;
    }

    @Path("/paiee")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String verificationPaiment() {
        //
        //AnnoyingBeep();
        //
        //return lancer("",1,"");
        return "";
    }

    @Path("/devise")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String setDevise(Devise devise) {
        //
        return deviseMetier.saveAgent(devise);
        //
    }

    private double conversion(Double montant, Long id, Boolean de) {
        Devise devise = Devise.findAll().firstResult();
        double prct = (10 * montant) / 100;
        if (de) {
            System.out.println("En dollar: " + de);
            return (montant + prct) ;// devise.taux;
        } else {
            System.out.println("En franc: " + de);
            return (montant + prct) * devise.taux;
        }
    }

    @Path("/devise")
    @DELETE
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTaux(Devise devise) {
        //
        Devise.deleteAll();
        //
    }

    @Path("/devise")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public double setTaux(Devise devise) {
        //
        devise.persist();
        return devise.taux;
    }

    @Path("/devise")
    @GET
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public double getTaux() {
        //
        try {
            Devise devise = Devise.findAll().firstResult();
            return devise.taux;
        }catch (Exception ex){
            return 0;
        }
    }

    @Path("/test")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public double getTest() throws InterruptedException {
        //
        TimeUnit.SECONDS.sleep(10);
        return 0.0;
    }
}
