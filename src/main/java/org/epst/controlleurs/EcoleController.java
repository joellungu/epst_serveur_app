package org.epst.controlleurs;


import org.epst.models.Ecole;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("ecole")
public class EcoleController {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEcole(@PathParam("id") Long id){
        Ecole ecole = Ecole.findById(id);
        return Response.ok(ecole).build();
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEcoles(){
        List<Ecole> ecoles = Ecole.listAll();
        return Response.ok(ecoles).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEcoles(Ecole ecole){
        ecole.persist();
        return Response.ok("ok").build();
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response putEcoles(Ecole ecole){
        Ecole ecole1 = Ecole.findById(ecole.id);
        if(ecole1 == null){
            return Response.serverError().build();
        }

        ecole1.nom = ecole.nom;
        ecole1.adresse = ecole.adresse;
        ecole1.codeEcole = ecole.codeEcole;
        ecole1.province = ecole.province;
        ecole1.provinceEducationnelle = ecole.provinceEducationnelle;

        return Response.ok(ecole1).build();
    }


    @DELETE
    @Path("{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEcoles(@PathParam("id") Long id){
        Ecole.deleteById(id);
        return Response.ok("ok").build();
    }


}
