package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CoursDirect extends PanacheEntity {
    public String titre;
    public String description;

    @ManyToOne
    public Classe classe;
}
