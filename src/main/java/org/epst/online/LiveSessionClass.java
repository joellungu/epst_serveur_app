package org.epst.online;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class LiveSessionClass extends PanacheEntity {
    public Long sessionId;
    public String classId;
    public String classLabel;
}
