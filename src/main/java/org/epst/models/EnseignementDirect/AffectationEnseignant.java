package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class AffectationEnseignant extends PanacheEntity {
    @ManyToOne
    public Utilisateur enseignant;

    @ManyToOne
    public CoursDirect coursDirect;

    @ManyToOne
    public AnneeScolaire anneeScolaire;
}
