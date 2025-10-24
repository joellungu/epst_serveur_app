package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.Utilisateur;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Path("/utilisateur")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    static class LoginRequest {
        public String telephone;
        public String motDePasse;
    }

    //
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    //

    @GET
    public List<Utilisateur> listAll() {
        return Utilisateur.listAll();
    }

    @GET
    @Path("/{id}")
    public Utilisateur get(@PathParam("id") Long id) {
        return Utilisateur.findById(id);
    }

    @GET
    @Path("/testsms")
    public Response testsms() {
        //
        envoiOTP("","");
        //
        return Response.ok().build();
    }

    @GET
    @Path("/classe")
    public Response getAllByClasse(@QueryParam("classe") String classe) {
        //
        List<Utilisateur> utilisateurs = Utilisateur.find( "classe", classe).list();
        return Response.ok(utilisateurs).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        HashMap params = new HashMap();
        params.put("telephone", request.telephone);
        params.put("motDePasse", request.motDePasse);

        Utilisateur utilisateur = (Utilisateur) Utilisateur.find( "telephone =: telephone and motDePasse =: motDePasse", params).firstResult();
        if (utilisateur != null) {
            return Response.ok(utilisateur).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Téléphone ou mot de passe incorrect")
                    .build();
        }
    }

    @POST
    @Transactional
    public Response create(Utilisateur entity) {
        System.out.println("Le tell: "+entity.telephone);
        Utilisateur utilisateur = Utilisateur.find("telephone", entity.telephone).firstResult();
        if(utilisateur == null){
            //
            entity.persist();
            //
            String num = entity.telephone.replace("+", "");
            envoiOTP(num, entity.motDePasse);

            return Response.status(Response.Status.CREATED).entity(entity).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ce numéro existe déjà. Veuillez vous connecter.").build();
        }
    }

    @PUT
    @Path("forgot")
    @Transactional
    @Consumes(MediaType.TEXT_PLAIN)
    public Response forgot(String telephone) {
        System.out.println("Le tell::: "+telephone);
        Utilisateur utilisateur = (Utilisateur) Utilisateur.find("telephone", telephone).firstResult();
        if(utilisateur == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ce numéro n'existe pas. Veuillez créer un compte.").build();
        } else {
            try {
                String num = telephone.replace("+", "");
                envoiOTP(num, utilisateur.motDePasse);
                return Response.status(Response.Status.CREATED).build();
            } catch (Exception ex) {
                System.out.println("Erreur: "+ex.getMessage());
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Erreur dans le serveur: "+ex.getMessage()).build();
            }
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Utilisateur update(@PathParam("id") Long id, Utilisateur updated) {
        Utilisateur entity = Utilisateur.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Utilisateur entity = Utilisateur.findById(id);
        if (entity != null) entity.delete();
    }
    //
    private void envoiOTP(String telephone, String code) {
        //https://api.keccel.com/sms/v1/message.asp?token=84hbYi6TU8Zu&from=GUEST&to=243851234567&message=Hello

        // Construction de l'URL avec encodage
        String baseUrl = "https://api.keccel.com/sms/v1/message.asp";
        String fullUrl = String.format("%s?token=%s&from=%s&to=%s&message=%s",
                baseUrl,
                URLEncoder.encode("HG59P642KW9AQ2M", StandardCharsets.UTF_8),
                "MIN%20EDU%20NC",
                URLEncoder.encode(telephone, StandardCharsets.UTF_8),
                URLEncoder.encode("Votre mot de passe est: "+code, StandardCharsets.UTF_8)
        );
        //Random random = new Random();
        // Génère un nombre entre 1000000 et 9999999 (inclus)
        //int number = 1_000_000 + random.nextInt(9_000_000);
        //
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .GET()
                    .build();
            //
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //
            System.out.println("GET status: " + response.statusCode());
            System.out.println("GET body:\n" + response.body());
            //
        } catch (Exception e) {
            System.out.println("Erreur:\n" + e.toString());
        }
    }

}
