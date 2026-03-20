package org.epst.online;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class OnlineClass extends PanacheEntity {
    public String code;
    public String name;
    public String niveau;
    public String ecoleCode;
    public String province;
    public String district;
    public String createdByMatricule;
    public LocalDateTime createdAt = LocalDateTime.now();
    public boolean active = true;
    public int maxParticipants = 40;
}
