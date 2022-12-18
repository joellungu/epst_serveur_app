package org.epst.controlleurs;

import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.document_scolaire.identification.DemandeIdentificationMetier;
import org.epst.models.mutuelle.Demande;
import org.epst.models.mutuelle.DemandeMetier;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("identification")
public class DemandeIdentificationController {

    @Inject
    DemandeIdentificationMetier demandeIdentificationMetier;

    @Path("save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDemande(DemandeIdentification d) throws IOException {
        //System.out.println("La demande: "+hashMap.get("nom"));
        //

        /*
        DemandeIdentification d = new DemandeIdentification();
        d.setId(Long.parseLong(""+hashMap.get("id")));
        d.setNom(""+hashMap.get("Nom"));
        d.setPostnom(""+hashMap.get("Postnom"));
        d.setPrenom(""+hashMap.get("Prenom"));
        d.setSexe((char)hashMap.get("Sexe"));
        d.setLieuNaissance(""+hashMap.get("notes"));
        d.setDateNaissance(""+hashMap.get("postnom"));
        d.setTelephone(""+hashMap.get("prenom"));
        d.setNommere(""+hashMap.get("Nommere"));
        d.setNompere(""+hashMap.get("Nompere"));
        d.setAdresse(""+hashMap.get("Adresse"));
        d.setProvinceOrigine(""+hashMap.get("ProvinceOrigine"));
        d.setExt1("Ext1");
        d.setEcole(LocalDate.now().toString());
        d.setProvinceEcole(""+hashMap.get("ProvinceEcole"));
        d.setProvinceEducationnel((int)hashMap.get("provinceEducationnel"));
        d.setOption(""+hashMap.get("Option"));
        d.setTypeIdentification(""+hashMap.get("TypeIdentification"));
        //
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DataOutputStream out1 = new DataOutputStream(baos1);

        for (Object element : (ArrayList) hashMap.get("photo")) {
            System.out.print(element);
            out1.writeByte((int) element);
        }
        byte[] bytes = baos1.toByteArray();
        //
        d.setPhoto(bytes);
        //
        //---------------------------------
        //
        //ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        //DataOutputStream out2 = new DataOutputStream(baos2);

        //for (Object element : (ArrayList) hashMap.get("piecejointe")) {
        //    System.out.print(element);
        //    out2.writeByte((int) element);
        //}
        //byte[] bytes2 = baos2.toByteArray();
        //
        //d.setPiecejointe(bytes2);
        //---------------------------------
        */
        demandeIdentificationMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<DemandeIdentification> getAll(@QueryParam("province") String province, @QueryParam("district") String district,
                                              @QueryParam("valider") int valider,
                                              @QueryParam("type") int type){
        //
        return demandeIdentificationMetier.getAll(province, district, valider);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<DemandeIdentification> getAll(@QueryParam("matricule") String matricule){
        //
        return demandeIdentificationMetier.getAllByMatricule(matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("one/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DemandeIdentification> getOne(@PathParam("id") Long id){
        //
        return demandeIdentificationMetier.getOne(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        return demandeIdentificationMetier.getPiecejointe(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status){
        //
        demandeIdentificationMetier.setStatus(status,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("saturer/{id}/{cenome}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setExpirer(@PathParam("id") Long id, @PathParam("cenome") String cenome, @PathParam("status") int status){
        //
        demandeIdentificationMetier.setExpirer(status,cenome,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int getStatus(@QueryParam("id") Long id){
        //
        return demandeIdentificationMetier.getStatus(id);
        //getAll
        //return Response.status(Response.Status.CREATED).entity().build();
    }

}
