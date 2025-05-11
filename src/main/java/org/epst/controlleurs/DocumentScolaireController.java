package org.epst.controlleurs;

import org.epst.models.document_scolaire.documents.Document;
import org.epst.models.document_scolaire.documents.DocumentMetier;
import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.document_scolaire.identification.DemandeIdentificationMetier;
import org.epst.models.mutuelle.Demande;

import jakarta.inject.Inject;
import javax.print.Doc;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("documentscolaire")
public class DocumentScolaireController {

    @Inject
    DocumentMetier documentMetier;

    @Path("enregistrement")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response saveDemande(Document d) throws IOException {
        //System.out.println("La demande: "+hashMap.get("nom"));
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
        //documentMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //
        return Response.status(Response.Status.CREATED).entity(d).build();
    }

    @Path("all/demandeencour")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response getAllDemandeEnCour(@QueryParam("province") String province){
        //
        HashMap params = new HashMap();
        params.put("provinceEcole",province);//Transfere
        params.put("valider", 0);//Transfere
        //
        List<Document> documents = Document.list("provinceEcole =:provinceEcole and valider =:valider",params);
        //
        List<HashMap> docs = new LinkedList<>();
        //
        for(Document document : documents) {
            //
            HashMap doc = new HashMap();
            //
            doc.put("id",document.id);
            doc.put("nom",document.nom);
            doc.put("postnom",document.postnom);
            doc.put("prenom",document.prenom);
            doc.put("sexe",document.sexe);
            doc.put("nompere",document.nompere);
            doc.put("nommere",document.nommere);
            doc.put("adresse",document.adresse);
            doc.put("telephone",document.telephone);
            doc.put("dateNaissance",document.dateNaissance);
            doc.put("datedemande",document.datedemande);
            doc.put("documenrDemande",document.documenrDemande);
            doc.put("documenrDemandecode",document.documenrDemandecode);
            doc.put("ecole",document.ecole);
            doc.put("lieuNaissance",document.lieuNaissance);
            doc.put("matricule",document.matricule);
            doc.put("option",document.option);
            doc.put("provinceEcole",document.provinceEcole);
            doc.put("provinceEducationnel",document.provinceEducationnel);
            doc.put("raison",document.raison);
            doc.put("type",document.type);
            doc.put("valider",document.valider);
            doc.put("annee",document.annee);
            doc.put("ecole",document.ecole);
            //
            docs.add(doc);
        }
        //return documentMetier.getAll(province, district, valider);
        return Response.ok().entity(docs).build();
    }
    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Document> getAll(@QueryParam("province") String province, @QueryParam("district") String district,
                                 @QueryParam("valider") int valider,
                                 @QueryParam("type") int type){
        //
        HashMap params = new HashMap();
        params.put("provinceEcole",province);//Transfere
        params.put("provinceEducationnel",district);//Transfere
        params.put("valider",valider);//Transfere
        //
        return Document.list("provinceEcole =:provinceEcole and provinceEducationnel =:provinceEducationnel and valider =:valider",params);
        //return documentMetier.getAll(province, district, valider);
        //return Response.status(Response.Status.CREATED).entity().build();
    }


    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Document> getAll(@QueryParam("matricule") String matricule){
        //
        return Document.list("matricule",matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Document getOne(@PathParam("id") Long id){
        //
        return Document.findById(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        Document document = Document.findById(id);
        return document.getPhoto();
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status, String raison){
        //
        Document document = Document.findById(id);
        if(document == null){
            return;
        }
        document.valider = status;
        document.raison = raison;
        //documentMetier.setStatus(status,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("saturer/{id}/{cenome}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setExpirer(@PathParam("id") Long id, @PathParam("cenome") String cenome, @PathParam("status") int status){
        //
        documentMetier.setExpirer(status,cenome,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Document getStatus(@QueryParam("id") Long id){
        //
        return Document.findById(id);
        //getAll
        //return Response.status(Response.Status.CREATED).entity().build();
    }

}
