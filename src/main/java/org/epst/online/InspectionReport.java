package org.epst.online;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
public class InspectionReport extends PanacheEntity {
    public Long sessionId;
    public Long classId;
    public String inspectorMatricule;
    public int inspectorRole;
    @Enumerated(EnumType.STRING)
    public InspectorFocus inspectorFocus = InspectorFocus.STUDENTS;
    public String observedTeacherMatricule;
    public int score;
    public String notes;
    public LocalDateTime createdAt = LocalDateTime.now();
}
