package org.epst.online;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
public class SessionParticipant extends PanacheEntity {
    public Long sessionId;
    public String matricule;
    public String displayName;
    @Enumerated(EnumType.STRING)
    public OnlineRole role;
    @Enumerated(EnumType.STRING)
    public InspectorFocus inspectorFocus;
    @Enumerated(EnumType.STRING)
    public ParticipantStatus status = ParticipantStatus.JOINED;
    public LocalDateTime joinedAt = LocalDateTime.now();
    public LocalDateTime leftAt;

    public enum ParticipantStatus {
        JOINED,
        LEFT
    }
}
