package org.epst.controlleurs;


import org.epst.models.Agent.Agent;
import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.sernie.Sernie;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Path("sernie")
public class SernieController {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDemande(Sernie d) throws IOException {
        //System.out.println("La demande: "+d.getAdresse());
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
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateDemande(Sernie sernie) throws IOException {
        //System.out.println("La demande: "+d.getAdresse());
        Sernie sernie1 = Sernie.findById(sernie.id);
        if(sernie1 == null){
            return Response.serverError().build();
        }

        //
        sernie1.nom = sernie.nom;
        sernie1.postnom = sernie.postnom;
        sernie1.prenom = sernie.prenom;
        sernie1.sexe = sernie.sexe;
        sernie1.nompere = sernie.nompere;
        sernie1.nommere = sernie.nommere;
        sernie1.telephone = sernie.telephone;
        sernie1.adresse = sernie.adresse;
        sernie1.Territoire = sernie.Territoire;
        sernie1.Secteur = sernie.Secteur;
        sernie1.Groupement = sernie.Groupement;
        sernie1.Village = sernie.Village;
        sernie1.Nationalite = sernie.Nationalite;
        sernie1.Antenne = sernie.Antenne;
        sernie1.provinceOrigine = sernie.provinceOrigine;
        sernie1.lieuNaissance = sernie.lieuNaissance;
        sernie1.dateNaissance = sernie.dateNaissance;
        sernie1.ecole = sernie.ecole;
        sernie1.code_ecole = sernie.code_ecole;
        sernie1.reseaux = sernie.reseaux;
        sernie1.provinceEcole = sernie.provinceEcole;
        sernie1.provinceEducationnel = sernie.provinceEducationnel;
        sernie1.option = sernie.option;
        sernie1.Niveau = sernie.Niveau;
        sernie1.annee = sernie.annee;
        sernie1.Classe = sernie.Classe;
        sernie1.datedemande = sernie.datedemande;
        sernie1.photo = sernie.photo;
        //sernie1.ext = sernie.ext;
        sernie1.valider = sernie.valider;

        return Response.status(Response.Status.CREATED).entity("ok").build();
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
    public DemandeIdentification getDemandeId(@PathParam("id") Long id){
        //
        System.out.println("Le id vaut: "+id);
        //
        return DemandeIdentification.findById(id);
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

    @Path("getallby/{province}/{provinceeducationnel}/{antenne}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response getAllBy(@PathParam("province") String province,
                             @PathParam("provinceeducationnel") String provinceeducationnel,
                             @PathParam("antenne") String antenne){
        //
        HashMap params = new HashMap<>();
        params.put("provinceEducationnel",provinceeducationnel);
        params.put("provinceEcole",province);
        params.put("antenne",antenne);
        params.put("valider",0);

        List<Sernie> liste = Sernie.list("provinceEducationnel =: provinceEducationnel and provinceEcole =: provinceEcole and valider =: valider and antenne =: antenne",params);

        return Response.ok(liste).build();
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

    @Path("piecejointe/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public byte[] getPhoto(@PathParam("id") Long id){
        Sernie sernie = Sernie.findById(id);
        //
        return sernie.photo;
        //getAll
        //return Response.status(Response.Status.CREATED).entity().build();
    }

}