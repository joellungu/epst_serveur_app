package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Transferes extends PanacheEntity {
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
    public String option_apres;
    public String ecoleprovenance;
    public String ecoleprovenancedistric;
    public String ecoledestination;
    public String ecoledestinationprov;
    public String ecoledestinationdistric;
    public byte[] photo;
    public String ext1;
    public int valider;
    public String raison;
    public String datedemande;
    public String reference;
}
