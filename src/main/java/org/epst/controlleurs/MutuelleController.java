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
        d.setBeneficiaire(""+hashMap.get("beneficiaire"));
        d.setDirection(""+hashMap.get("direction"));
        d.setMatricule(""+hashMap.get("matricule"));
        d.setNom(""+hashMap.get("nom"));
        d.setNotes(""+hashMap.get("notes"));
        d.setPostnom(""+hashMap.get("postnom"));
        d.setPrenom(""+hashMap.get("prenom"));
        d.setExt(""+hashMap.get("ext"));
        d.setValider(0);
        d.setJour(LocalDate.now().toString());
        d.setServices(""+hashMap.get("services"));
        //
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        for (Object element : (ArrayList) hashMap.get("carte")) {
            System.out.print(element);
            out.writeByte((int) element);
        }
        byte[] bytes = baos.toByteArray();
        //
        d.setCarte(bytes);
        //
        demandeMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Demande> noterMa(){
        //
        return demandeMetier.getAll();
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

    @Path("update/{id}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status){
        //
        demandeMetier.setStatus(status,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int getStatus(@QueryParam("matricule") String matricule){
        //
        return demandeMetier.getStatus(matricule);
        //
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    //setStatus

}
