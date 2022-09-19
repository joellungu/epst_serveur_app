package org.epst.controlleurs;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.epst.beans.Magasin;
import org.epst.beans.MultipartService;
import org.epst.models.ModelMagasin;
import org.epst.models.ModelPlainte;

@Path("/client")
public class MultipartClientResource {

        //@Inject
        //@RestClient
        //MultipartService service;
        //ModelPlainte modelPlainte = new ModelPlainte();
        //ModelMagasin modelMagasin = new ModelMagasin();

        @POST
        @Path("/multipart/{id}")
        @Consumes(MediaType.APPLICATION_OCTET_STREAM)
        @Produces(MediaType.TEXT_PLAIN)
        public String sendFile(byte[] requestBody, @PathParam("id") Long id) throws Exception {//FileUploadForm data
            System.out.println(requestBody);
            //for(int t = 0; t < requestBody.length; t++){
            //    System.out.println(requestBody[t]);
            //}
            ModelMagasin modelMagasin = new ModelMagasin();
            int t = modelMagasin.miseaJourMagasin(id, requestBody);
            return "La reponse du serveur: ";//service.sendMultipartData("");
        }
/*
        @POST
        @Path("/multipart1/{piecejointe_id}/{type}")
        @Consumes(MediaType.APPLICATION_OCTET_STREAM)
        @Produces(MediaType.TEXT_PLAIN)
        public String sendFile1(@PathParam("piecejointe_id") Long piecejointe_id, @PathParam("type") String type,byte[] requestBody) throws Exception {//FileUploadForm data
            //System.out.println(magasin);
            int t = modelPlainte.savePiecejointe(0L,piecejointe_id, type, requestBody);
            //
            System.out.println("piecejointe_id: "+piecejointe_id+"// type "+type+"");
            System.out.println("votre element piece jointe_______: "+
                    piecejointe_id+":\n__:"+
                    type+":\n__:"
            );
            return "La reponse du serveur: ";//service.sendMultipartData("");
        }
    */
}
