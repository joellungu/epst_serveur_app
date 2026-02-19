package org.epst.controlleurs;

import org.epst.models.document_scolaire.transfere.Transfere;
import org.epst.models.mutuelle.Demande;
import org.epst.models.mutuelle.DemandeMetier;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    @Transactional
    public Response saveDemande(Demande demande) throws IOException {

        //System.out.println("La demande: "+hashMap.get("nom"));
        //

        demande.persist();

        /*
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
*/
        //String v = demandeMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //

        return Response.status(Response.Status.CREATED).entity(demande).build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Demande> getAll(@QueryParam("province") String province, @QueryParam("district") String district){
        //
        HashMap params = new HashMap();
        params.put("province",province);//
        params.put("district",district);//
        params.put("valider",0);//
        //
        return Demande.list("province =:province and district =:district and valider =:valider",params);
        //return demandeMetier.getAll(province, district);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Demande> getAll(@QueryParam("matricule") String matricule){
        //
        return Demande.list("matricule",matricule);
        //return demandeMetier.getAllByMatricule(matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Demande getOne(@PathParam("id") Long id){
        //
        return Demande.findById(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("carte/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getCarte(@PathParam("id") Long id){
        //
        Demande demande = Demande.findById(id);
        return demande.getCarte();
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        Demande demande = Demande.findById(id);
        return demande.getPiecejointe();
        //return demandeMetier.getPiecejointe(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status){
        //
        Demande demande = Demande.findById(id);
        if(demande == null){
            return;
        }
        demande.valider = status;
        //demandeMetier.setStatus(status,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("saturer/{id}/{cenome}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)

    public void setExpirer(@PathParam("id") Long id, @PathParam("cenome") String cenome, @PathParam("status") int status){
        //
        //demandeMetier.setExpirer(status,cenome,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int getStatus(@QueryParam("id") Long id){
        //
        Demande demande = Demande.findById(id);
        //
        return demande.valider;
        //
        //return Response.status(Response.Status.CREATED).entity().build();
        //
    }
    //setStatus

}
