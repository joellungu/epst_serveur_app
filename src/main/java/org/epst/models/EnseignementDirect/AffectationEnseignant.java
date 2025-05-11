package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class AffectationEnseignant extends PanacheEntity {
    @ManyToOne
    public Utilisateur enseignant;

    @ManyToOne
    public CoursDirect coursDirect;

    @ManyToOne
    public AnneeScolaire anneeScolaire;
}
