package org.epst.controlleurs;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.epst.models.Conversation;
import org.epst.models.Messages;
import org.epst.models.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    @GET
    @Path("/conversation/{conversationId}")
    public List<Messages> getMessages(@PathParam("conversationId") Long conversationId) {
        return Messages.findByConversationId(conversationId);
    }

    @POST
    @Path("/send")
    @Transactional
    public Messages sendMessage(SendMessageRequest request) {
        Messages message = new Messages();
        message.conversationId = request.conversationId;
        message.sender = request.sender;
        message.content = request.content;
        message.timestamp = LocalDateTime.now();
        message.type = Messages.MessageType.TEXT;
        message.persist();
        //
        System.out.println("Mess 1 "+message.conversationId);
        System.out.println("Mess 2 "+message.sender);
        System.out.println("Mess 3 "+message.content);
        System.out.println("Mess 4 "+message.timestamp);
        System.out.println("Mess 5 "+message.type);

        // WebSocket broadcast
        // Implémentation WebSocket à ajouter
        return message;
    }

    @POST
    @Path("/fermer")
    @Transactional
    public Messages fermerMessage(SendMessageRequest request) {
        Messages message = new Messages();
        message.conversationId = request.conversationId;
        message.sender = request.sender;
        message.content = request.content;
        message.timestamp = LocalDateTime.now();
        message.type = Messages.MessageType.SYSTEM;
        message.persist();
        //
        Conversation conversation = Conversation.findById(request.conversationId);
        conversation.status = Conversation.ConversationStatus.CLOSED;
        conversation.persist();

        // WebSocket broadcast
        // Implémentation WebSocket à ajouter
        return message;
    }


}
