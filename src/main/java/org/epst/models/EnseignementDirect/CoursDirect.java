package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class CoursDirect extends PanacheEntity {
    public String titre;
    public String description;

    //@ManyToOne
    //public Classe classe;
}
