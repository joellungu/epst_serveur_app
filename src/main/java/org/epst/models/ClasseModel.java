package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class ClasseModel extends PanacheEntity {
    //
    public String nom;
    public String categorie;
    //public String typeFormation;
    public int cls;
    //
}
