package org.epst.controlleurs;

import org.epst.models.Diplome.DemandeDiplome;
import org.epst.models.document_scolaire.documents.Document;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("diplome")
public class DiplomeController {

    //
    @Path("enregistrement")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDemande(DemandeDiplome d) throws IOException {
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

    //
    @Path("all/demandeencour")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response getAllDemandeEnCour(){
        //
        HashMap params = new HashMap();
        //params.put("provinceEcole",province);//Transfere
        params.put("status", 0);//Transfere
        //
        List documents = DemandeDiplome.list("status =:status",params);
        //
        return Response.ok().entity(documents).build();
    }
    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAll(){
        /**
         * @QueryParam("province") String province, @QueryParam("district") String district,
         *                                  @QueryParam("valider") int valider,
         *                                  @QueryParam("type") int type
         */
        HashMap params = new HashMap();
        //params.put("provinceEcole",province);//Transfere
        params.put("status", 1);//Transfere
        //
        List documents = DemandeDiplome.list("status =:status",params);
        //
        return Response.ok().entity(documents).build();
    }


}
