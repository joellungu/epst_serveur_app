package org.epst.controlleurs;

import org.epst.models.mutuelle.Demande;
import org.epst.models.mutuelle.DemandeMetier;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("mutuelle")
public class MutuelleController {

    @Inject
    DemandeMetier demandeMetier;

    @Path("demande")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDemande(HashMap hashMap) throws IOException {
        System.out.println("La demande: "+hashMap.get("nom"));
        //

        //
        Demande d = new Demande();
        d.setId(Long.parseLong(""+hashMap.get("id")));
        d.setBeneficiaire(""+hashMap.get("beneficiaire"));
        d.setDirection(""+hashMap.get("direction"));
        d.setMatricule(""+hashMap.get("matricule"));
        d.setNom(""+hashMap.get("nom"));
        d.setNotes(""+hashMap.get("notes"));
        d.setPostnom(""+hashMap.get("postnom"));
        d.setPrenom(""+hashMap.get("prenom"));
        d.setExt1(""+hashMap.get("ext1"));
        d.setExt2(""+hashMap.get("ext2"));
        d.setProvince(""+hashMap.get("province"));
        d.setDistrict(""+hashMap.get("district"));
        d.setValider(0);
        d.setJour(LocalDate.now().toString());
        d.setServices(""+hashMap.get("services"));
        //
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DataOutputStream out1 = new DataOutputStream(baos1);

        for (Object element : (ArrayList) hashMap.get("carte")) {
            System.out.print(element);
            out1.writeByte((int) element);
        }
        byte[] bytes = baos1.toByteArray();
        //
        d.setCarte(bytes);
        //---------------------------------
        //
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DataOutputStream out2 = new DataOutputStream(baos2);

        for (Object element : (ArrayList) hashMap.get("piecejointe")) {
            System.out.print(element);
            out2.writeByte((int) element);
        }
        byte[] bytes2 = baos2.toByteArray();
        //
        d.setPiecejointe(bytes2);
        //---------------------------------

        demandeMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Demande> getAll(@QueryParam("province") String province, @QueryParam("district") String district){
        //
        return demandeMetier.getAll(province, district);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Demande> getAll(@QueryParam("matricule") String matricule){
        //
        return demandeMetier.getAllByMatricule(matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("one/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Demande> getOne(@PathParam("id") Long id){
        //
        return demandeMetier.getOne(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("carte/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getCarte(@PathParam("id") Long id){
        //
        return demandeMetier.getCarte(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        return demandeMetier.getPiecejointe(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status){
        //
        demandeMetier.setStatus(status,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("saturer/{id}/{cenome}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setExpirer(@PathParam("id") Long id, @PathParam("cenome") String cenome, @PathParam("status") int status){
        //
        demandeMetier.setExpirer(status,cenome,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int getStatus(@QueryParam("id") Long id){
        //
        return demandeMetier.getStatus(id);
        //
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    //setStatus

}
