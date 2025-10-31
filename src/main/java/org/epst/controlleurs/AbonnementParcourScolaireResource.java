package org.epst.controlleurs;

import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.parcourscolaire.AbonnementParcourScolaire;

import java.util.HashMap;
import java.util.List;

@Path("/abonnements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AbonnementParcourScolaireResource {

    @GET
    public List<AbonnementParcourScolaire> listAll() {
        return AbonnementParcourScolaire.listAll(Sort.by("codeEleve"));
    }

    @GET
    @Path("/{codeEleve}/{anneeScolaire}")
    public Response getByCode(@PathParam("codeEleve") String codeEleve, @PathParam("anneeScolaire") String anneeScolaire) {
        HashMap params = new HashMap();
        params.put("codeEleve", codeEleve);
        params.put("anneeScolaire", anneeScolaire);

        AbonnementParcourScolaire abonnement = (AbonnementParcourScolaire) AbonnementParcourScolaire.find("codeEleve =: codeEleve and anneeScolaire =: anneeScolaire", params).firstResult();
        if (abonnement == null) {
            return Response.status(404).build();
        }
        return Response.ok(abonnement).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        AbonnementParcourScolaire abonnement = AbonnementParcourScolaire.findById(id);
        if (abonnement == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(abonnement).build();
    }


    @POST
    @Transactional
    public Response create(AbonnementParcourScolaire abonnement) {
        if (abonnement.id != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("L'ID doit être null lors de la création")
                    .build();
        }
        abonnement.persist();
        return Response.status(Response.Status.CREATED).entity(abonnement).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, AbonnementParcourScolaire updatedAbonnement) {
        AbonnementParcourScolaire existing = AbonnementParcourScolaire.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Mettez à jour les champs
        existing.codeEleve = updatedAbonnement.codeEleve;
        existing.anneeScolaire = updatedAbonnement.anneeScolaire;
        existing.status = updatedAbonnement.status;

        existing.persist();
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = AbonnementParcourScolaire.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}