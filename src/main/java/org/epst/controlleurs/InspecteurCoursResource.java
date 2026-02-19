package org.epst.controlleurs;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.InspecteurCours;

import java.util.*;

@Path("/api/inspecteur-cours")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InspecteurCoursResource {

    @GET
    public Response getAll(
            @QueryParam("pageIndex") Integer pageIndex,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortAsc") @DefaultValue("true") Boolean sortAsc,
            @QueryParam("idInspecteur") Long idInspecteur) {

        // Construire la query string complète
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (idInspecteur != null) {
            queryBuilder.append("idInspecteur = :idInspecteur");
            params.put("idInspecteur", idInspecteur);
        }

        // Ajouter le ORDER BY si nécessaire
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String order = sortAsc ? " ASC" : " DESC";
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" ORDER BY ").append(sortBy).append(order);
            } else {
                queryBuilder.append("ORDER BY ").append(sortBy).append(order);
            }
        }

        PanacheQuery<InspecteurCours> query;

        if (queryBuilder.length() == 0) {
            query = InspecteurCours.findAll();
        } else {
            query = InspecteurCours.find(queryBuilder.toString(), params);
        }

        // Pagination
        if (pageIndex != null && pageSize != null) {
            query.page(Page.of(pageIndex, pageSize));
        }

        List<InspecteurCours> inspecteurs = query.list();
        long totalCount = query.count();

        return Response.ok(
                Map.of(
                        "data", inspecteurs,
                        "totalCount", totalCount,
                        "pageIndex", pageIndex != null ? pageIndex : 0,
                        "pageSize", pageSize != null ? pageSize : inspecteurs.size()
                )
        ).build();
    }


    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        InspecteurCours inspecteur = InspecteurCours.findById(id);
        if (inspecteur == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours non trouvé avec l'id: " + id))
                    .build();
        }
        return Response.ok(inspecteur).build();
    }

    @POST
    @Transactional
    public Response create(InspecteurCours inspecteurCours) {
        if (inspecteurCours.idInspecteur == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "idInspecteur est obligatoire"))
                    .build();
        }

        inspecteurCours.persist();
        return Response.status(Response.Status.CREATED).entity(inspecteurCours).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, InspecteurCours inspecteurCours) {
        InspecteurCours existing = InspecteurCours.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours non trouvé avec l'id: " + id))
                    .build();
        }

        // Mise à jour des champs
        existing.idInspecteur = inspecteurCours.idInspecteur;
        existing.cours = inspecteurCours.cours != null ? inspecteurCours.cours : existing.cours;
        existing.classe = inspecteurCours.classe != null ? inspecteurCours.classe : existing.classe;

        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        InspecteurCours inspecteur = InspecteurCours.findById(id);
        if (inspecteur == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours non trouvé avec l'id: " + id))
                    .build();
        }

        inspecteur.delete();
        return Response.noContent().build();
    }

    // Opérations spécifiques

    @GET
    @Path("/inspecteur/{idInspecteur}")
    public Response getByInspecteurId(@PathParam("idInspecteur") Long idInspecteur) {
        List<InspecteurCours> inspecteurs = InspecteurCours.list("idInspecteur", idInspecteur);
        return Response.ok(inspecteurs).build();
    }

    @POST
    @Path("/{id}/cours")
    @Transactional
    public Response addCours(@PathParam("id") Long id, @QueryParam("coursId") Long coursId) {
        InspecteurCours inspecteur = InspecteurCours.findById(id);
        if (inspecteur == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours non trouvé"))
                    .build();
        }

        if (inspecteur.cours == null) {
            inspecteur.cours = new ArrayList<>();
        }

        if (!inspecteur.cours.contains(coursId)) {
            inspecteur.cours.add(coursId);
        }

        return Response.ok(inspecteur).build();
    }

    @DELETE
    @Path("/{id}/cours/{coursId}")
    @Transactional
    public Response removeCours(@PathParam("id") Long id, @PathParam("coursId") Long coursId) {
        InspecteurCours inspecteur = InspecteurCours.findById(id);
        if (inspecteur == null || inspecteur.cours == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours ou cours non trouvé"))
                    .build();
        }

        inspecteur.cours.remove(coursId);
        return Response.ok(inspecteur).build();
    }

    @POST
    @Path("/{id}/classe")
    @Transactional
    public Response addClasse(@PathParam("id") Long id, @QueryParam("classeId") UUID classeId) {
        InspecteurCours inspecteur = InspecteurCours.findById(id);
        if (inspecteur == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours non trouvé"))
                    .build();
        }

        if (inspecteur.classe == null) {
            inspecteur.classe = new ArrayList<>();
        }

        if (!inspecteur.classe.contains(classeId)) {
            inspecteur.classe.add(classeId);
        }

        return Response.ok(inspecteur).build();
    }

    @DELETE
    @Path("/{id}/classe/{classeId}")
    @Transactional
    public Response removeClasse(@PathParam("id") Long id, @PathParam("classeId") Long classeId) {
        InspecteurCours inspecteur = InspecteurCours.findById(id);
        if (inspecteur == null || inspecteur.classe == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "InspecteurCours ou classe non trouvé"))
                    .build();
        }

        inspecteur.classe.remove(classeId);
        return Response.ok(inspecteur).build();
    }

    @GET
    @Path("/search")
    public Response search(
            @QueryParam("idInspecteur") Long idInspecteur,
            @QueryParam("coursId") Long coursId,
            @QueryParam("classeId") Long classeId) {

        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (idInspecteur != null) {
            queryBuilder.append("idInspecteur = :idInspecteur");
            params.put("idInspecteur", idInspecteur);
        }

        if (coursId != null) {
            if (!queryBuilder.isEmpty()) queryBuilder.append(" AND ");
            queryBuilder.append(":coursId MEMBER OF cours");
            params.put("coursId", coursId);
        }

        if (classeId != null) {
            if (!queryBuilder.isEmpty()) queryBuilder.append(" AND ");
            queryBuilder.append(":classeId MEMBER OF classe");
            params.put("classeId", classeId);
        }

        List<InspecteurCours> results;
        if (queryBuilder.isEmpty()) {
            results = InspecteurCours.listAll();
        } else {
            results = InspecteurCours.list(queryBuilder.toString(), params);
        }

        return Response.ok(results).build();
    }

    @GET
    @Path("/count")
    public Response count() {
        long count = InspecteurCours.count();
        return Response.ok(Map.of("count", count)).build();
    }
}