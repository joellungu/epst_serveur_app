package org.epst.models.annonces;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Annonce extends PanacheEntity {

    public String nom;
    public byte[] image;
}
