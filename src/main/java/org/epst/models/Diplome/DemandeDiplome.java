package org.epst.models.Diplome;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class DemandeDiplome extends PanacheEntity {
    //
    public String matricule;
    public String annee;
    public String dateDemande;
    public int documenrDemandecode;
    public String documenrDemande;
    public String codePaiement;
    public int status;
}
