package org.epst.online;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
public class LiveSession extends PanacheEntity {
    public String classId;
    public String title;
    @Enumerated(EnumType.STRING)
    public SessionStatus status = SessionStatus.PLANNED;
    @Enumerated(EnumType.STRING)
    public SessionAudience audience = SessionAudience.STUDENT;
    public LocalDateTime scheduledAt;
    public LocalDateTime startedAt;
    public LocalDateTime endedAt;
    public String createdByMatricule;
    public String hostMatricule;
    public String zegoRoomId;
    public boolean recordingEnabled = false;
    public int maxParticipants = 40;

    public enum SessionStatus {
        PLANNED,
        LIVE,
        ENDED
    }
}
