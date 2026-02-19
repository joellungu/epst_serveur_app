package org.epst.models.transfere;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class TransfereEleve extends PanacheEntity {
    public String nom ;
    public String postnom;
    public String prenom ;
    public String sexe ;
    public String lieuNaissance ;
    public String dateNaissance ;
    public String telephone ;
    public String nompere ;
    public String nommere;
    public String adresse ;
    public String provinceOrigine ;
    public String option_avant ;
    public String option_apres ;
    public String ecoleProvenance ;
    public String ecoleProvenanceProv ;
    public String ecoleProvenanceDistric ;
    public String ecoleDestination ;
    public String ecoleDestinationProv ;
    public String ecoleDestinationDistric ;
    public byte[] photo;
    public String ext1 ;
    public String datedemande ;
    public String reference ;
    public String raison ;
    public int valider;
}
