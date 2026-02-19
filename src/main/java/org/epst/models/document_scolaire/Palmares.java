package org.epst.models.document_scolaire;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;


public class Palmares {
    String ecole;
    String ecolecode;
    int annee;

    public String getEcole() {
        return ecole;
    }

    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public String getEcolecode() {
        return ecolecode;
    }

    public void setEcolecode(String ecolecode) {
        this.ecolecode = ecolecode;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }
}
