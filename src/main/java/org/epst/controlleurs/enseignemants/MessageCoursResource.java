package org.epst.controlleurs.enseignemants;

import org.epst.models.EnseignementDirect.MessageCours;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/messagecours")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageCoursResource {

    @GET
    public List<MessageCours> listAll() {
        return MessageCours.listAll();
    }

    @GET
    @Path("/{id}")
    public MessageCours get(@PathParam("id") Long id) {
        return MessageCours.findById(id);
    }

    @POST
    @Transactional
    public Response create(MessageCours entity) {
        entity.persist();
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public MessageCours update(@PathParam("id") Long id, MessageCours updated) {
        MessageCours entity = MessageCours.findById(id);
        if (entity == null) throw new NotFoundException();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        MessageCours entity = MessageCours.findById(id);
        if (entity != null) entity.delete();
    }
}
