package org.epst.online;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
public class OnlineSchedule extends PanacheEntity {
    public String classId;
    public String title;
    @Enumerated(EnumType.STRING)
    public SessionAudience audience = SessionAudience.STUDENT;
    public LocalDateTime startsAt;
    public LocalDateTime endsAt;
    public String createdByMatricule;
    public LocalDateTime createdAt;
}
