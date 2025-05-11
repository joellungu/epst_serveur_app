package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Classe extends PanacheEntity {
    public String nom; // ex: 6ème Primaire
}