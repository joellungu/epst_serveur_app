package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.SoumissionTravail;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/soumissiontravail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SoumissionTravailResource {

    @GET
    public List<SoumissionTravail> listAll() {
        return SoumissionTravail.listAll();
    }

    @GET
    @Path("/{id}")
    public SoumissionTravail get(@PathParam("id") Long id) {
        return SoumissionTravail.findById(id);
    }

    @POST
    @Transactional
    public Response create(SoumissionTravail entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public SoumissionTravail update(@PathParam("id") Long id, SoumissionTravail updated) {
        SoumissionTravail entity = SoumissionTravail.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        SoumissionTravail entity = SoumissionTravail.findById(id);
        if (entity != null) entity.delete();
    }
}
