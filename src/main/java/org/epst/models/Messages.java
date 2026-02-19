package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Messages extends PanacheEntity {
    public Long conversationId;
    public String sender; // "client" ou matricule agent
    public String content;
    public LocalDateTime timestamp;
    public MessageType type;

    public static enum MessageType {
        TEXT, SYSTEM
    }

    public static List<Messages> findByConversationId(Long conversationId) {
        return list("conversationId", conversationId);
    }
}
