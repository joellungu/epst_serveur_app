package org.epst.controlleurs;

import org.epst.models.devise.Devise;
import org.epst.models.devise.DeviseMetier;
import org.epst.models.paiement.Paiement;
import org.epst.models.paiement.PaiementMetier;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Timer;
import java.util.TimerTask;

@Path("/paiement")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaiementController {

    @Inject
    PaiementMetier paiementMetier;
    @Inject
    DeviseMetier deviseMetier;

    public PaiementController(){}

    public String verif(String reference){
        String urlPost = "http://41.243.7.46:3006/flexpay/api/rest/v1/paymentService";
        String body = "{\n" +
                "  \"merchant\":\"ECOSYS\"," +
                "  \"type\":1," +
                "  \"reference\":\"data31\"," +
                "  \"phone\":\"243815381693\"," +
                "  \"amount\":500," +
                "  \"currency\":\"CDF\"," +
                "  \"callbackUrl\":\"http://dgc-epst.uc.r.appspot.com\"" +
                "}";
        var requete = HttpRequest.newBuilder()
                .uri(URI.create(urlPost))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJcL2xvZ2luIiwicm9sZXMiOlsiTUVSQ0hBTlQiXSwiZXhwIjoxNzMzOTEwMTY5LCJzdWIiOiJlNjFiZTYyNTA2M2NlNGQzOTc3ZTY2ZTI1ODdiZjIwOSJ9.KXwGzLyTGJT4iLnA6rtqPKRLE195j5oFWLbmpOlh2uo")
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
    }

    public String lancer(String devise, String telephone, int m, String reference) {
        System.out.println("la devise: $"+devise+"lE MONTANT: $"+m);
        String dev = devise == "USD" ? "USD":"CDF";
        double montant = deviseMetier.conversion(m,1L, devise=="USD");
        System.out.println("la devise: $"+devise+"lE MONTANT: $"+montant);
        String urlPost = "http://41.243.7.46:3006/flexpay/api/rest/v1/paymentService";
        String body = "{\n" +
                "  \"merchant\":\"EPSTAPP\"," +
                "  \"type\":1," +
                "  \"reference\": \""+reference+"\"," +
                "  \"phone\": \""+telephone+"\"," +
                "  \"amount\": \""+montant+"\"," +
                "  \"currency\":\""+dev+"\"," +
                "  \"callbackUrl\":\"http://dgc-epst.uc.r.appspot.com\"" +
                "}";
        var requete = HttpRequest.newBuilder()
                .uri(URI.create(urlPost))
                .header("Content-Type","application/json")
                .header("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJcL2xvZ2luIiwicm9sZXMiOlsiTUVSQ0hBTlQiXSwiZXhwIjoxNzM3NTUyMDEwLCJzdWIiOiI4ZTE4NzJlODQwZTc5YjM5OWIxMDliMmYyNjk5YWY3YSJ9.co6sS0YEdCy3v3nja0NHvS5dYnMNmjZPJET_Ri7pB0E")
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

    Toolkit toolkit;
    Timer timer;

    public void AnnoyingBeep() {
        String reponse = lancer("","",1,"");
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

    @Path("/paie")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String lancerPaiment(Paiement paiement) {
        //
        paiementMetier.savePaiement(paiement);
        //AnnoyingBeep();
        //
        return lancer(paiement.getCurrency(),paiement.getPhone(),paiement.getAmount(),paiement.getReference());
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
}
