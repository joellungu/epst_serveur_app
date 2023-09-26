package org.epst.controlleurs;


import org.epst.models.secretariat.Secretariat;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("secretariat")
public class SecretariatController {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getSecretariat(@PathParam("id") Long id) {
        //
        Secretariat secretariat = Secretariat.findById(id);
        //
        return Response.ok(secretariat).build();
    }

    @GET
    @Path("all")
    public Response allSecretariat() {
        //
        List<Secretariat> ss = Secretariat.listAll();
        List<HashMap> secretariats = new LinkedList<>();
        //
        ss.forEach((s)->{
            //
            HashMap data = new HashMap();
            //
            data.put("id",s.id);
            data.put("denomition",s.denomition);
            data.put("sigle",s.sigle);
            //
            secretariats.add(data);
            //
        });
        //
        return Response.ok(secretariats).build();
    }

    @PUT
    @Transactional
    public Response putSecretariat(Secretariat secretariat) {
        //
        Secretariat secretariat1 = Secretariat.findById(secretariat.id);
        //
        if(secretariat1 == null){
            return Response.serverError().build();
        }
        //
        secretariat1.denomition = secretariat.denomition;
        secretariat1.sigle = secretariat.sigle;
        secretariat1.arretes = secretariat.arretes;
        secretariat1.telephone = secretariat.telephone;
        secretariat1.email = secretariat.email;
        secretariat1.maps = secretariat.maps;
        secretariat1.departement = secretariat.departement;
        secretariat1.arretes = secretariat.arretes;
        secretariat1.attributionMission = secretariat.attributionMission;
        secretariat1.historique = secretariat.historique;
        secretariat1.realisation = secretariat.realisation;
        //
        return Response.ok(secretariat).build();
    }

    @POST
    @Consumes(MediaType.WILDCARD)
    @Transactional
    public Response postSecretariat(Secretariat secretariat) {
        //
        try {
            secretariat.persist();
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex);
        }
        //
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteSecretariat(@PathParam("id") Long id) {
        //
        Secretariat.deleteById(id);
        //
        return Response.ok().build();
    }

    @GET
    @Path("photo/{id}/{index}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response Secretariat(@PathParam("id") Long id, @PathParam("index") int index) {
        //
        Secretariat secretariat = Secretariat.findById(id);
        byte[] photo = new byte[0];
        //
        int i = 0;
        for(var departement : secretariat.departement){
            if(i == index){
                photo = departement.photo;
                break;
            }
            i++;
        }
        //
        return Response.ok(photo).build();
    }



}
