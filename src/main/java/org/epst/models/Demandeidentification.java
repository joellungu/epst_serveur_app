package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Demandeidentification extends PanacheEntity {
    public String nom;
    public String postnom;
    public String prenom;
    public String sexe;
    public String lieunaissance;
    public String datenaissance;
    public String telephone;
    public String nompere;
    public String nommere;
    public String adresse;
    public String provinceorigine;

    public byte[] photo;
    public String ext1;
    public String ecole;
    public String provinceecole;
    public String provinceeducationnel;
    public String option;
    public String annee;
    public int valider;
    public String typeidentificationcode;
    public String typeidentification;
    public String reference;
}
