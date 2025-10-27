package org.epst.controlleurs;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
//import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.epst.beans.FileUploadForm;
import org.epst.models.ModelMagasin;
import org.epst.models.magasin.Magasin;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

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
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/magasin")
//@RegisterRestClient
public class MagasinControlleur {

    private static final ObjectMapper mapper = new ObjectMapper();
    ModelMagasin modelMagasin = new ModelMagasin();
    
    @Path("/{id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Magasin getMagasint(@PathParam("id") Long id) {
        System.out.println("le id: "+id);
        Magasin u = Magasin.findById(id);
        //Todo todo = new Todo();
        //todo.setSummary(id);
        //todo.setDescription("Application JSON Todo Description");
        return u;
    }

    @Path("/all/{type}")
    @GET()
    //@Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Magasin> getAllMagasints(@PathParam("type") String type) {
        System.out.println("Element type: "+type);
        //
        List<Magasin> listeU = Magasin.list("type",type);
        List<Magasin> lU = new LinkedList<>();
        listeU.forEach((e)->{
            //
            e.setPiecejointe(new byte[]{});
            lU.add(e);
        });

        System.out.println("La liste vaut: "+ listeU.size());
        System.out.println("La liste vaut: "+ listeU.toString());
        //
        //listeU.forEach((u)->{
        //  System.out
        //        .println("Element nom: "+u.nom);
        //});
        //
        //Todo todo = new Todo();
        //todo.setSummary("Application JSON Todo Summary");
        //todo.setDescription("Application JSON Todo Description");
        //
        //Todo todo2 = new Todo();
        //todo2.setSummary("Application JSON ");
        //todo2.setDescription("Application JSON ");
        return lU;//Arrays.asList(todo,todo2);
    }

    //@Path("")
    //Content-Type: text/plain;charset=UTF-8
    @POST()
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response savetMagasint(Magasin magasin) {
        magasin.persist();
        /*
        Long t = modelMagasin.saveMagasin(magasin);
        System.out.println("votre element: "+
        magasin.getDate()+":\n__:"+
        magasin.getDescription()+":\n__:"+
        magasin.getId()+":\n__:"+
        magasin.getLibelle()+":\n__:"
        );
        //
        */
        ObjectNode json = mapper.createObjectNode();
        
        json.put("status", 1);
        //Random random = new Random();
        //long random63BitLong = random.nextLong();
        //
        //Magasin.setId(random63BitLong);
        //
        //Magasin.persist();
        //
        //ObjectNode json = mapper.createObjectNode();
        ///json.put("save", "ok");

        
        return Response.ok().build();
    }

    @POST
    @Path("/savepice/{id}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response savePieceJointeMaga(@PathParam("id") Long id,byte[] requestBody){
        System.out.println(requestBody);
        //for(int t = 0; t < requestBody.length; t++){
        //    System.out.println(requestBody[t]);
        //}
        ModelMagasin modelMagasin = new ModelMagasin();
        int t = modelMagasin.miseaJourMagasin(id, requestBody);
        return Response.ok().entity("Ok").build();//service.sendMultipartData("");
    }

    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMagasint(Magasin Magasin) {

        //int t = modelMagasin.miseaJourMagasin(Magasin);
        //System.out.println(Magasin.adresse);
        
        ObjectNode json = mapper.createObjectNode();
        //
        //json.put("mettre à jour", t);
        return Response.ok().build();
    }

    @Path("/{id}")
    @DELETE()
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteMagasint(@PathParam("id") Long id) {
        //
        try {
            Magasin.deleteById(id);
            return Response.status(Response.Status.CREATED).entity("Data id: "+id).build();
        } catch (Exception ex) {
            System.out.println("Erreur du à : "+ex.getCause());
            System.out.println("Erreur du à : "+ex.getMessage());
            return Response.serverError().entity(ex.getCause() + " = "+id).build();
        }
    }

    @Path("recherche/{mot}")
    @GET()
    @Consumes(MediaType.TEXT_PLAIN)
    public List<Magasin> rechercheMag(@PathParam("type")String mot){

        List<Magasin> liste = new LinkedList<Magasin>();
        
        return liste;

    }

    
    @Path("update/{id}")
    @POST()
    @Consumes(MediaType.APPLICATION_OCTET_STREAM) //"multipart/form-data")
    public Response sendMultipartData(byte[] data){
        //byte[] data, @PathParam("id") Long id
        //
        String fileName = "";//

        try {
            System.out.println("Id: ---: ");
            //writeFile(form.getData(), fileName);
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        System.out.println("Done");

        return Response.status(200)
                .entity("uploadFile is called, Uploaded file name : " + fileName).build();

        //return null;
    }
    //
}

