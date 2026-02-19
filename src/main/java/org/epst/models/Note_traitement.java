package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class Note_traitement extends PanacheEntity {
    public String nom_admin;
    public String reference;
    @Lob
    public String note;
}
