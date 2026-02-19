package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Evaluation extends PanacheEntity {

    @ManyToOne
    public Travail travail;

    @ManyToOne
    public Utilisateur eleve;

    public double note; // note sur 10 ou 20
    public String commentaire;
}
