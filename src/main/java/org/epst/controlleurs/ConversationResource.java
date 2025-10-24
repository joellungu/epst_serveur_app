package org.epst.controlleurs;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.Conversation;
import org.epst.models.StartConversationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Path("/conversations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConversationResource {

    @GET
    @Path("/pending")
    public List<Conversation> getPendingConversations() {
        return Conversation.findPendingConversations();
    }

    @POST
    @Path("/start")
    @Transactional
    public Conversation startConversation(StartConversationRequest request) {
        Conversation conversation = new Conversation();
        conversation.clientName = request.clientName;
        conversation.clientId = UUID.randomUUID().toString();
        conversation.status = Conversation.ConversationStatus.PENDING;
        conversation.createdAt = LocalDateTime.now();
        conversation.persist();
        return conversation;
    }

    @PUT
    @Path("/{id}/assign/{matricule}")
    @Transactional
    public Conversation assignAgent(@PathParam("id") Long id, @PathParam("matricule") String matricule) {
        Conversation conversation = Conversation.findById(id);
        if (conversation != null) {
            conversation.agentMatricule = matricule;
            conversation.status = Conversation.ConversationStatus.ACTIVE;
            conversation.updatedAt = LocalDateTime.now();
            conversation.persist();
        }
        return conversation;
    }

    @GET
    @Path("/check/{id}")
    @Transactional
    public Conversation assignAgent(@PathParam("id") Long id) {
        Conversation conversation = Conversation.findById(id);

        return conversation;
    }
}

