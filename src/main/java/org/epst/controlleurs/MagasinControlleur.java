package org.epst.controlleurs;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
//import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.epst.beans.FileUploadForm;
import org.epst.beans.Magasin;
import org.epst.models.ModelMagasin;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
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
        Magasin u = modelMagasin.getMagasin(id);
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
        List<Magasin> listeU = modelMagasin.getAllMagasin(type);
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
        return listeU;//Arrays.asList(todo,todo2);
    }

    //@Path("")
    //Content-Type: text/plain;charset=UTF-8
    @POST()
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response savetMagasint(Magasin magasin) {
        Long t = modelMagasin.saveMagasin(magasin);
        System.out.println("votre element: "+
        magasin.getDate()+":\n__:"+
        magasin.getDescription()+":\n__:"+
        magasin.getId()+":\n__:"+
        magasin.getLibelle()+":\n__:"
        );
        //
        
        ObjectNode json = mapper.createObjectNode();
        
        json.put("status", t);
        //Random random = new Random();
        //long random63BitLong = random.nextLong();
        //
        //Magasin.setId(random63BitLong);
        //
        //Magasin.persist();
        //
        //ObjectNode json = mapper.createObjectNode();
        ///json.put("save", "ok");
        
        return Response.status(Response.Status.CREATED).entity(json).build();
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

        int t = modelMagasin.miseaJourMagasin(Magasin);
        //System.out.println(Magasin.adresse);
        
        ObjectNode json = mapper.createObjectNode();
        //
        json.put("mettre à jour", t);
        return Response.status(Response.Status.CREATED).entity(json).build();
    }

    @Path("/{id}")
    @DELETE()
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMagasint(@PathParam("id") Long id) {
        int t = modelMagasin.supprimerMagasin(id);
        ObjectNode json = mapper.createObjectNode();
        //
        //json.put("status", "ok");
        json.put("supprimer", t);
        return Response.status(Response.Status.CREATED).entity(json).build();
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
    Response sendMultipartData(byte[] data){//byte[] data, @PathParam("id") Long id
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

