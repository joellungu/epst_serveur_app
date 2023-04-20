package org.epst.controlleurs;

import java.util.List;
import org.epst.beans.Utilisateur;
import org.epst.models.Agent.Agent;
import org.epst.models.ModelUtilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/agent")
public class AgentControlleur {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    ModelUtilisateur modelUtilisateur = new ModelUtilisateur();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {

        // create a JSON string
        ObjectNode json = mapper.createObjectNode();
        json.put("EPST APP", "Server");
        return Response.status(Response.Status.OK).entity(json).build();
    }

    @Path("/login/{matricule}/{mdp}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Utilisateur getAgent(@PathParam("matricule") String matricule, @PathParam("mdp") String mdp) {
        Utilisateur u = modelUtilisateur.getUtilisateur(matricule, mdp);
        //Todo todo = new Todo();
        //todo.setSummary(id);
        //todo.setDescription("Application JSON Todo Description");
        return u;
    }

    @Path("/{id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Utilisateur getAgent(@PathParam("id") int id) {
        Utilisateur u = modelUtilisateur.getUtilisateur(id);
        //Todo todo = new Todo();
        //todo.setSummary(id);
        //todo.setDescription("Application JSON Todo Description");
        return u;
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
    public Response savetAgent(Utilisateur utilisateur) {
        String t = modelUtilisateur.saveUtilisateur(utilisateur);
        System.out.println("votre element: "+
        utilisateur.getAdresse()+":\n__:"+
            utilisateur.getDate_de_naissance()+":\n__:"+
            utilisateur.getEmail()+":\n__:"+
            utilisateur.getNom()+":\n__:"+
            utilisateur.getNumero()+":\n__:"+
            utilisateur.getPostnom()+":\n__:"+
            utilisateur.getPrenom()+":\n__:"+
            utilisateur.getRole()+":\n__:"+
            utilisateur.getMatricule()+":\n__:"+
            utilisateur.getId_statut()
        );
        //

        ObjectNode json = mapper.createObjectNode();
        //
        json.put("status", "votre element: "+
        utilisateur.getAdresse()+":\n__:"+
            utilisateur.getDate_de_naissance()+":\n__:"+
            utilisateur.getEmail()+":\n__:"+
            utilisateur.getNom()+":\n__:"+
            utilisateur.getNumero()+":\n__:"+
            utilisateur.getPostnom()+":\n__:"+
            utilisateur.getPrenom()+":\n__:"+
            utilisateur.getRole()+":\n__:"+
            utilisateur.getMatricule()+":\n__:"+
            utilisateur.getId_statut());
        json.put("save", t);
        
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAgent(Utilisateur utilisateur) {

        int t = modelUtilisateur.miseaJourUtilisateur(utilisateur);
        System.out.println(utilisateur.adresse);
        
        ObjectNode json = mapper.createObjectNode();
        //
        json.put("mettre à jour", t);
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @Path("/{id}")
    @DELETE()
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
