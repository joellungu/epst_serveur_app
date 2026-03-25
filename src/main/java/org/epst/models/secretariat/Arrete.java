package org.epst.models.secretariat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class Arrete extends PanacheEntity {
    @Column(columnDefinition = "TEXT")
    public String texte;

    @Lob
    public byte[] photo;
}
