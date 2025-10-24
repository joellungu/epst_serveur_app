package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Conversation extends PanacheEntity {
    public String clientName;
    public String clientId; // Identifiant unique du client
    public String agentMatricule;
    public ConversationStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static enum ConversationStatus {
        PENDING, ACTIVE, CLOSED
    }

    public static List<Conversation> findPendingConversations() {
        return list("status", ConversationStatus.PENDING);
    }

    public static List<Conversation> findByAgentMatricule(String matricule) {
        return list("agentMatricule", matricule);
    }
}