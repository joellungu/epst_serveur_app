package org.epst.models.palmares;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Resultat extends PanacheEntity {
    public int annee;
    public String provinceEducationnel;
    public byte[] fichier;
    //

    public int getAnnee() {
        return annee;
    }

    public String getProvinceEducationnel() {
        return provinceEducationnel;
    }

    public void setProvinceEducationnel(String provinceEducationnel) {
        this.provinceEducationnel = provinceEducationnel;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }
}
