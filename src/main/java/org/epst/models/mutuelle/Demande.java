package org.epst.models.mutuelle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Demande {
    Long id;
    String nom;
    String postnom;
    String prenom;
    String matricule;
    String direction;
    String services;
    String beneficiaire;
    String notes;
    int valider;
    String jour;
    String ext;
    byte[] carte;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPostnom() {
        return postnom;
    }

    public void setPostnom(String postnom) {
        this.postnom = postnom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getCarte() {
        return carte;
    }

    public void setCarte(byte[] carte) {
        this.carte = carte;
    }

    public int getValider() {
        return valider;
    }

    public void setValider(int valider) {
        this.valider = valider;
    }
}
