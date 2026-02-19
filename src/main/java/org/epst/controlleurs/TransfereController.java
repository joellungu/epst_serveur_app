package org.epst.controlleurs;

import org.epst.models.document_scolaire.documents.Document;
import org.epst.models.document_scolaire.documents.DocumentMetier;
import org.epst.models.document_scolaire.transfere.Transfere;
import org.epst.models.document_scolaire.transfere.TransfereMetier;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path("transfere")
public class TransfereController {

    @Inject
    TransfereMetier transfereMetier;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDemande(Transfere transfere) throws IOException {
        //System.out.println("La demande: "+hashMap.get("nom"));
        transfere.persist();
        //
        //
        return Response.ok(transfere).build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Transfere> getAll(@QueryParam("province") String province, @QueryParam("district") String district,
                                 @QueryParam("valider") int valider){
        //
        HashMap params = new HashMap();
        //params.put("ecoleDestinationProv",province);//Transfere
        //params.put("ecoleDestinationDistric",district);//Transfere
        params.put("valider",valider);//Transfere
        System.out.println("ecoleDestinationProv: "+province+" // ecoleDestinationDistric: "+district+" // valider: "+valider+" //");
        //
        //and ecoleDestinationDistric =:ecoleDestinationDistric and ecoleDestinationProv =:ecoleDestinationProv
        //
        List<Transfere> list = Transfere.list("valider =:valider ",params);
        //
        List<Transfere> list2 = new LinkedList<>();
        //
        list.stream().filter((p) -> {
            if(p.ecoleDestinationProv.toLowerCase().contains(province.toLowerCase())
                    && p.ecoleDestinationDistric.equalsIgnoreCase(district)){
                Transfere transfere = new Transfere();
                /**
                 *     public String ext1;
                 *     public String datedemande;
                 *     public String raison;
                 *     public int valider;
                 *     public String reference
                 */
                //
                transfere.nom = p.nom;
                transfere.postnom = p.postnom;
                transfere.prenom = p.prenom;
                transfere.sexe = p.sexe;
                transfere.lieuNaissance = p.lieuNaissance;
                transfere.dateNaissance = p.dateNaissance;
                transfere.telephone = p.telephone;
                transfere.nompere = p.nompere;
                transfere.nommere = p.nommere;
                transfere.adresse = p.adresse;
                transfere.option_avant = p.option_avant;
                transfere.option_apres = p.option_apres;
                transfere.provinceOrigine = p.provinceOrigine;
                transfere.ecoleProvenance = p.ecoleProvenance;
                transfere.ecoleProvenanceProv = p.ecoleProvenanceProv;
                transfere.ecoleProvenanceDistric = p.ecoleProvenanceDistric;
                transfere.ecoleDestination = p.ecoleDestination;
                transfere.ecoleDestinationProv = p.ecoleDestinationProv;
                transfere.ecoleDestinationDistric = p.ecoleDestinationDistric;
                transfere.datedemande = p.datedemande;
                transfere.raison = p.raison;
                transfere.valider = p.valider;
                transfere.reference = p.reference;
                //
                list2.add(transfere);
            }
            return p.ecoleDestinationProv.toLowerCase().contains(province.toLowerCase())
                    && p.ecoleDestinationDistric.equalsIgnoreCase(district);
        });
        //
        return list2;
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<Transfere> getAll(@QueryParam("matricule") String matricule){
        //
        //return transfereMetier.getAllByMatricule(matricule);
        return Transfere.list("matricule",matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Transfere getOne(@PathParam("id") Long id){
        //
        return Transfere.findById(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)

    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        Transfere transfere = Transfere.findById(id);
        return transfere.photo;
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status, String raison){
        //
        Transfere transfere = Transfere.findById(id);
        if(transfere == null){
            return;
        }
        transfere.valider = status;
        transfere.raison = raison;
        //
        //return Response.status(Response.Status.CREATED).entity().build();
    }
    @Path("saturer/{id}/{cenome}/{status}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void setExpirer(@PathParam("id") Long id, @PathParam("cenome") String cenome, @PathParam("status") int status){
        //
        Transfere transfere = Transfere.findById(id);
        if(transfere == null){
            return;
        }
        transfere.valider = status;
        //transfere.cenome = cenome;
        //
        transfereMetier.setExpirer(status,cenome,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Transfere getStatus(@QueryParam("id") Long id){
        //
        System.out.println(id);
        //
        return Transfere.findById(id);
        //getAll
        //return Response.status(Response.Status.CREATED).entity().build();
    }

}
