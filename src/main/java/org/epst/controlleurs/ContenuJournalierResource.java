package org.epst.controlleurs;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.epst.models.ContenuForm;
import org.epst.models.ContenuJournalier;
import org.epst.models.Cours.Cours;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Path("/contenus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContenuJournalierResource {

    static class ContenuDTO {
        public Long id;
        public String date; // yyyy-MM-dd
        public String type;
        public String titre;
        public String description;
        public Long idClasse;
        public Long idCours;
        public Long idInspecteur;
        public String anneeScolaire;
        public String cle;
        public String updatedAt;
    }

    @GET
    @Path("/classe/{idClasse}")
    public List<ContenuJournalier> getByClasse(@PathParam("idClasse") Long idClasse) {
        return ContenuJournalier.findByClasse(idClasse);
    }

    @GET
    @Path("/{id}")
    public ContenuJournalier getById(@PathParam("id") Long id) {
        return ContenuJournalier.findById(id);
    }

    @GET
    @Path("/by-date")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContenuByDate(
            @QueryParam("idClasse") Long idClasse,
            @QueryParam("idCours") Long idCours,
            @QueryParam("date") String date,
            @QueryParam("type") String type // <- nouveau param optionnel
    ) {
        if (idClasse == null || idCours == null || date == null) {
            //
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("idClasse, idCours et date sont requis")
                    .build();
        }
        //

        LocalDate localDate = LocalDateTime.parse(date).toLocalDate(); //LocalDate.parse(date);
        String q = "idClasse = ?1 AND idCours = ?2 AND date = ?3";
        List<Object> params = new ArrayList<>();
        params.add(idClasse);
        params.add(idCours);
        params.add(localDate);

        if (type != null && !type.isBlank()) {
            q += " AND type = ?4";
            params.add(type);
        }

        List<ContenuJournalier> contenus = ContenuJournalier.find(q, params.toArray()).list();

        // Convertir en DTO (sans le fichier)
        List<ContenuDTO> dtos = contenus.stream().map(c -> {
            ContenuDTO d = new ContenuDTO();
            d.id = c.id;
            d.date = c.date.toString();
            d.type = c.type;
            d.titre = c.titre;
            d.description = c.description;
            d.idClasse = c.idClasse;
            d.idCours = c.idCours;
            d.idInspecteur = c.idInspecteur;
            d.anneeScolaire = c.anneeScolaire;
            d.cle = c.cle;
            d.updatedAt = c.updatedAt != null ? c.updatedAt.toString() : null;
            return d;
        }).collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}/file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response downloadFile(@PathParam("id") Long id) {
        ContenuJournalier c = ContenuJournalier.findById(id);
        if (c == null || c.fichier == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Fichier introuvable").build();
        }
        return Response.ok(c.fichier)
                .header("Content-Disposition", "attachment; filename=\"contenu-" + id + "\"")
                .build();
    }


    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response upload(ContenuForm form) throws IOException {
        ContenuJournalier contenu = new ContenuJournalier();
        // ✅ Conversion manuelle
        if (form.date != null && !form.date.isEmpty()) {
            contenu.date = LocalDate.parse(form.date.substring(0, 10)); // garde que YYYY-MM-DD
        } else {
            contenu.date = LocalDate.now(); // fallback
        }
        //contenu.date = form.date;
        System.out.println("Contenu: "+contenu.titre);

        contenu.type = form.type;
        contenu.titre = form.titre;
        contenu.description = form.description;
        contenu.idClasse = form.idClasse;
        contenu.idCours = form.idCours;
        contenu.idInspecteur = form.idInspecteur;
        contenu.anneeScolaire = form.anneeScolaire;
        contenu.fichier = form.fichier;
        contenu.persist();

        return Response.ok(contenu).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response update(@PathParam("id") Long id, @MultipartForm ContenuForm form) throws IOException {
        ContenuJournalier existing = ContenuJournalier.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenu introuvable");
        }

        // ✅ Conversion manuelle
        if (form.date != null && !form.date.isEmpty()) {
            existing.date = LocalDate.parse(form.date.substring(0, 10)); // garde que YYYY-MM-DD
        } else {
            existing.date = LocalDate.now(); // fallback
        }

        //existing.date = form.date;
        existing.type = form.type;
        existing.titre = form.titre;
        existing.description = form.description;
        existing.idClasse = form.idClasse;
        existing.idCours = form.idCours;
        existing.idInspecteur = form.idInspecteur;
        existing.anneeScolaire = form.anneeScolaire;
        if (form.fichier != null && form.fichier.length > 0) {
            existing.fichier = form.fichier;
        }
        existing.persist();

        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = ContenuJournalier.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(404).build();
    }
}
