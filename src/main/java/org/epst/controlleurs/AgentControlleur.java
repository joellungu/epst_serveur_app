package org.epst.controlleurs;

import java.util.HashMap;
import java.util.List;
import org.epst.beans.Utilisateur;
import org.epst.models.Agent.Agent;
import org.epst.models.LoginRequest;
import org.epst.models.ModelUtilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/agent")
public class AgentControlleur {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    ModelUtilisateur modelUtilisateur = new ModelUtilisateur();

    @Path("/login/{matricule}/{mdp}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAgent(@PathParam("matricule") String matricule,
                                @PathParam("mdp") String mdp){
        return doLogin(matricule, mdp);
    }

    @Path("/login")
    @POST()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request){
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorJson("Veuillez fournir votre matricule et votre mot de passe."))
                    .build();
        }
        return doLogin(request.matricule, request.mdp);
    }

    private Response doLogin(String matricule, String mdp) {
        String mat = matricule == null ? "" : matricule.trim();
        String pass = mdp == null ? "" : mdp;

        if (mat.isEmpty() || pass.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorJson("Veuillez fournir votre matricule et votre mot de passe."))
                    .build();
        }

        System.out.println("matricule: "+mat);

        HashMap<String, Object> params = new HashMap<>();
        params.put("matricule", mat);
        params.put("mdp", pass);

        Agent utilisater;
        try {
            utilisater = Agent.find("matricule =:matricule and mdp =:mdp ", params).firstResult();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorJson("Erreur interne du serveur. Veuillez réessayer plus tard."))
                    .build();
        }

        if (utilisater == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorJson("Matricule ou mot de passe incorrect."))
                    .build();
        }

        if ("0".equals(utilisater.id_statut)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(errorJson("Votre compte est désactivé. Veuillez contacter l'administrateur."))
                    .build();
        }

        return Response.ok(utilisater).build();
    }

    private ObjectNode errorJson(String message) {
        ObjectNode json = mapper.createObjectNode();
        json.put("status", "error");
        json.put("message", message);
        return json;
    }

    @Path("/{id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Agent getAgent(@PathParam("id") int id) {
        Agent agent = Agent.findById(id);
        //Todo todo = new Todo();
        //todo.setSummary(id);
        //todo.setDescription("Application JSON Todo Description");
        return agent;
    }
    
    @Path("/all")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Agent> getAllAgents() {
        /*
        List<Utilisateur> listeU = modelUtilisateur.getAllUtilisateur();
        listeU.forEach((u)->{
            System.out.println("Element nom: "+u.nom);
        });
        */
        //Todo todo = new Todo();
        //todo.setSummary("Application JSON Todo Summary");
        //todo.setDescription("Application JSON Todo Description");
        //
        //Todo todo2 = new Todo();
        //todo2.setSummary("Application JSON ");
        //todo2.setDescription("Application JSON ");
        List<Agent> agens = Agent.listAll();
        return agens;//Arrays.asList(todo,todo2);
    }

    //@Path("")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response savetAgent(Agent agent) {
        //
        agent.persist();
        //
        ObjectNode json = mapper.createObjectNode();
        //
        json.put("status", "votre element: ");
        
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateAgent(Agent agent) {

        Agent agent1 = Agent.findById(agent.id);
        if(agent1 == null){
            return Response.serverError().build();
        }
        //
        agent1.nom = agent.nom;
        agent1.postnom = agent.postnom;
        agent1.prenom = agent.prenom;
        agent1.numero = agent.numero;
        agent1.email = agent.email;
        agent1.adresse = agent.adresse;
        agent1.role = agent.role;
        agent1.matricule = agent.matricule;
        agent1.id_statut = agent.id_statut;
        agent1.date_de_naissance = agent.date_de_naissance;
        agent1.mdp = agent.mdp;
        agent1.province = agent.province;
        agent1.district = agent.district;
        agent1.antenne = agent.antenne;
        //
        ObjectNode json = mapper.createObjectNode();
        //
        json.put("mettre à jour", "coll");
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @Path("/{id}")
    @DELETE()
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAgent(@PathParam("id") int id) {
        int t = modelUtilisateur.supprimerUtilisateur(id);
        ObjectNode json = mapper.createObjectNode();
        //
        //json.put("status", "ok");
        json.put("supprimer", t);
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    
}
