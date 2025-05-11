package org.epst.controlleurs;

import org.epst.models.document_scolaire.documents.Document;
import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.document_scolaire.identification.DemandeIdentificationMetier;
import org.epst.models.mutuelle.Demande;
import org.epst.models.mutuelle.DemandeMetier;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @Path("enregistrement")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDemande(DemandeIdentification d) throws IOException {
        System.out.println("La demande: "+d.getAdresse());
        //
        d.persist();
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
        //demandeIdentificationMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //
        return Response.status(Response.Status.CREATED).entity(d).build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<DemandeIdentification> getAll(@QueryParam("province") String province, @QueryParam("district") String district,
                                              @QueryParam("valider") int valider,
                                              @QueryParam("role") int role){
        /*
        "Inspecteur exetat",8
    "Inspecteur tenassop",9
    "Inspecteur tenafepe",10
    "Inspecteur examen professionnel",12
    "Agent sernie",11
         */
        int code = role == 8 ? 0 : role == 9 ? 1 : role == 10 ? 2 : role == 11 ? 3 : 4;
        //
        HashMap params = new HashMap();
        params.put("provinceEcole",province);//Transfere
        params.put("provinceEducationnel",district);//Transfere
        params.put("valider",valider);//Transfere
        params.put("typeIdentificationcode",code);
        //
        return DemandeIdentification.list("provinceEcole =:provinceEcole and provinceEducationnel =:provinceEducationnel and valider =:valider and typeIdentificationcode =: typeIdentificationcode",params);
        //return demandeIdentificationMetier.getAll(province, district, valider, code);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<DemandeIdentification> getAll(@QueryParam("matricule") String matricule){
        //
        return DemandeIdentification.list("matricule",matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public HashMap getDemandeId(@PathParam("id") Long id){
        //
        System.out.println("Le id vaut: "+id);
        //
        DemandeIdentification d = DemandeIdentification.findById(id);
        //
        HashMap demande = new HashMap();
        demande.put("id", d.id);
        demande.put("nom", d.nom);
        demande.put("postnom", d.postnom);
        demande.put("prenom", d.prenom);
        demande.put("sexe", d.sexe);
        demande.put("telephone", d.telephone);
        demande.put("adresse", d.adresse);
        demande.put("annee", d.annee);
        demande.put("datedemande", d.datedemande);
        demande.put("dateNaissance", d.dateNaissance);
        demande.put("ecole", d.ecole);
        demande.put("ext1", d.ext1);
        demande.put("lieuNaissance", d.lieuNaissance);
        demande.put("nommere", d.nommere);
        demande.put("nompere", d.nompere);
        demande.put("option", d.option);
        demande.put("provinceEcole", d.provinceEcole);
        demande.put("provinceEducationnel", d.provinceEducationnel);
        demande.put("provinceOrigine", d.provinceOrigine);
        demande.put("typeIdentification", d.typeIdentification);
        demande.put("typeIdentificationcode", d.typeIdentificationcode);
        demande.put("valider", d.valider);
        demande.put("reference", d.reference);
        //
        return demande;
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        DemandeIdentification demandeIdentification = DemandeIdentification.findById(id);
        return demandeIdentification.getPhoto();
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status, String raison){
        //
        DemandeIdentification demandeIdentification = DemandeIdentification.findById(id);
        if(demandeIdentification == null){
            return;
        }
        demandeIdentification.valider = status;
        demandeIdentification.raison = raison;
        //demandeIdentificationMetier.setStatus(status,id);
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
    public DemandeIdentification getStatus(@QueryParam("id") Long id){
        //
        return DemandeIdentification.findById(id);
        //getAll
        //return Response.status(Response.Status.CREATED).entity().build();
    }

}
