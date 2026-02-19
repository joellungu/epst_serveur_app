package org.epst.controlleurs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.Classe;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClasseResource {

    //
    /* =========================
       CREATE
       ========================= */
    @POST
    @Transactional
    public Response create(Classe classe) {
        //classe.id = UUID.randomUUID();
        classe.persist();

        return Response
                .created(URI.create("/classes/" + classe.id))
                .entity(classe)
                .build();
    }

    /* =========================
       READ ALL
       ========================= */
    @GET
    public List<Classe> list() {
        return Classe.listAll();
    }

    /* =========================
   SEARCH (niveau / cycle / section)
   ========================= */
    @GET
    @Path("/search")
    public List<Classe> search(
            @QueryParam("niveau") String niveau,
            @QueryParam("cycle") String cycle,
            @QueryParam("section") String section
    ) {
        StringBuilder query = new StringBuilder("1=1");

        if (niveau != null && !niveau.isBlank()) {
            query.append(" and niveau = ?1");
        }
        if (cycle != null && !cycle.isBlank()) {
            query.append(" and cycle = ?2");
        }
        if (section != null && !section.isBlank()) {
            query.append(" and section = ?3");
        }

        return Classe.find(query.toString(),
                niveau, cycle, section
        ).list();
    }


    /* =========================
       READ BY ID
       ========================= */
    @GET
    @Path("/{id}")
    public Classe getById(@PathParam("id") UUID id) {
        Classe classe = Classe.findById(id);

        if (classe == null) {
            throw new NotFoundException("Classe non trouvée");
        }

        return classe;
    }

    /* =========================
       UPDATE
       ========================= */
    @PUT
    @Path("/{id}")
    @Transactional
    public Classe update(@PathParam("id") UUID id, Classe data) {
        Classe classe = Classe.findById(id);

        if (classe == null) {
            throw new NotFoundException("Classe non trouvée");
        }

        classe.niveau = data.niveau;
        classe.cycle = data.cycle;
        classe.section = data.section;

        return classe;
    }

    /* =========================
       DELETE
       ========================= */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = Classe.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("Classe non trouvée");
        }

        return Response.noContent().build();
    }

}
