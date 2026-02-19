package org.epst.controlleurs;


import org.epst.models.secretariat.Departement;
import org.epst.models.secretariat.Secretariat;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("secretariat")
public class SecretariatController {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("detail")
    @Transactional
    public Response getSecretariat(@QueryParam("id") Long id) {
        //
        Secretariat secretariat = Secretariat.findById(id);
        //
        List<Departement> departements = secretariat.departement;
        //
        List<HashMap> dpts = new LinkedList<>();
        //
        secretariat.departement.forEach((dd)->{
            //
            HashMap d = new HashMap();
            d.put("responsable", dd.responsable);
            d.put("departement", dd.departement);
            d.put("photo", dd.photo);
            //
            dpts.add(d);
        });
        //
        HashMap data = new HashMap();
        data.put("id", secretariat.id);
        data.put("adresse", secretariat.adresse);
        data.put("arretes", secretariat.arretes);
        data.put("attributionMission", secretariat.attributionMission);
        data.put("denomition", secretariat.denomition);
        data.put("email", secretariat.email);
        data.put("historique", secretariat.historique);
        data.put("realisation", secretariat.realisation);
        data.put("responsable", secretariat.responsable);
        data.put("sigle", secretariat.sigle);
        data.put("telephone", secretariat.telephone);
        data.put("departement", dpts);
        //data.put("", secretariat.telephone);
        //data.put("", secretariat.telephone);
        //
        return Response.ok(data).build();
    }

    @GET
    @Path("all")
    @Transaction
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
            return Response.status(405).build();
        }
        //
        secretariat1.denomition = secretariat.denomition;
        secretariat1.sigle = secretariat.sigle;
        secretariat1.arretes = secretariat.arretes;
        secretariat1.telephone = secretariat.telephone;
        secretariat1.email = secretariat.email;
        secretariat1.maps = secretariat.maps;
        secretariat1.departement = secretariat.departement;
        //
        if(secretariat.photo != null) {
            //
            secretariat1.photo = secretariat.photo;
            //
        }
        //
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

    @GET
    @Path("photoprofil/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response profilChef(@PathParam("id") Long id) {
        //
        Secretariat secretariat = Secretariat.findById(id);
        //
        return Response.ok(secretariat.photo).build();
    }

}
