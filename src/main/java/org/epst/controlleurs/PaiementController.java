package org.epst.controlleurs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.epst.models.devise.Devise;
import org.epst.models.devise.DeviseMetier;
import org.epst.models.paiement.Paiement;
import org.epst.models.paiement.PaiementMetier;
import org.json.JSONObject;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Path("/paiement")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaiementController {

    @Inject
    PaiementMetier paiementMetier;

    @Inject
    DeviseMetier deviseMetier;

    private final Map<String, CompletableFuture<String>> waitingRequests = new ConcurrentHashMap<>();


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
                "  \"amount\": \""+montant+"\"," +
                "  \"currency\":\""+dev+"\"," +
                "  \"callbackUrl\":\"https://educ-app-serveur-43d00822f87c.herokuapp.com/paiement/trigger\"" +
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

        //

        String rep = lancer(
                paiement.currency,
                paiement.phone,
                paiement.amount,
                paiement.reference
        );
        //"req-" + System.currentTimeMillis(); // Générer un ID unique pour la requête

        // Créer un CompletableFuture pour cette requête
        CompletableFuture<String> future = new CompletableFuture<>();
        waitingRequests.put(paiement.reference, future);

        try {
            // Attendre que la future soit complétée par une autre requête
            String result = future.get(); // Bloque jusqu'à ce que la future soit complétée
            return Response.ok(result).build();
        } catch (ExecutionException e) {
            //return Response.serverError().entity("Error: " + e.getMessage()).build();
            return Response.status(404).entity(e.getMessage()).build();
        } finally {
            // Nettoyer la future après utilisation
            waitingRequests.remove("joellungu123");
            //return Response.serverError().entity("Error: " + e.getMessage()).build();
        }
        //return repData;
    }

    private double conversion(Double montant, Long id, Boolean de) {
        Devise devise = Devise.findAll().firstResult();
        double prct = (10 * montant) / 100;
        if (de) {
            System.out.println("En dollar: " + de);
            return (montant + prct) ;// devise.taux;
        } else {
            System.out.println("En franc: " + de);
            return (montant + prct);// * devise.taux;
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

    /////////////////////////////////////////////////////////////////////////////
    @GET
    @Path("/start")
    public Response startRequest() throws InterruptedException {
        //String requestId = lancer("CDF","243815381693",500.0,"joellungu123");
        //"req-" + System.currentTimeMillis(); // Générer un ID unique pour la requête
        /*
        // Créer un CompletableFuture pour cette requête
        CompletableFuture<String> future = new CompletableFuture<>();
        waitingRequests.put("joellungu123", future);

        try {
            // Attendre que la future soit complétée par une autre requête
            String result = future.get(); // Bloque jusqu'à ce que la future soit complétée
            return Response.ok(result).build();
        } catch (ExecutionException e) {
            //return Response.serverError().entity("Error: " + e.getMessage()).build();
            return Response.status(404).entity(e.getMessage()).build();
        } finally {
            // Nettoyer la future après utilisation
            waitingRequests.remove("joellungu123");
            //return Response.serverError().entity("Error: " + e.getMessage()).build();
        }
        */
        return Response.serverError().entity("").build();
    }

    @POST
    @Path("/trigger")
    public Response triggerRequest(String reponse) {
        /*
            if (waitingRequests.isEmpty()) {
                return Response.ok("No requests are waiting.").build();
            }
        */
        //
        System.out.println("Reponse: "+reponse);
        //
        HashMap hashRep = new HashMap<>();
        //
        ObjectMapper mapper = new ObjectMapper();
        //
        try {
            JsonNode result = mapper.readTree(reponse);
            //
            // Compléter la première future en attente (ou une spécifique selon votre logique)
            Iterator iterator = waitingRequests.entrySet().iterator();
            //
            while (iterator.hasNext()) {
                //
                Map.Entry<String, CompletableFuture<String>> entry = (Map.Entry<String, CompletableFuture<String>>) iterator.next();
                //
                String requestId = entry.getKey();
                System.out.println("requestId: "+requestId);
                if (result.get("reference").asText().equals(requestId)) {
                    //
                    CompletableFuture<String> future = entry.getValue();
                    //
                    // Compléter la future pour débloquer la requête en attente
                    //
                    future.complete(reponse);
                }
                //
            }
            //
        }catch (Exception ex) {
            System.out.println("Erreur 1: "+ ex.getMessage());
            System.out.println("Erreur 2: "+ ex.getCause());
            //future.complete(reponse);
        }


        return Response.ok("Ok").build();
    }
}
