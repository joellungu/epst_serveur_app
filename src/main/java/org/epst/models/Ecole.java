package org.epst.models;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Ecole extends PanacheEntity {
    public String nom;
    public String province;
    public String provinceEducationnelle;
    public String adresse;
    public String codeEcole;
}
