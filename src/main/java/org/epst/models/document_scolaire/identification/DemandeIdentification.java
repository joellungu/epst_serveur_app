package org.epst.models.document_scolaire.identification;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
public class DemandeIdentification extends PanacheEntity {
    //Long id;
    public String nom;
    public String postnom;
    public String prenom;
    public String sexe;
    public String lieuNaissance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Timestamp dateNaissance;
    public String telephone;
    public String nompere;
    public String nommere;
    public String adresse;
    public String provinceOrigine;
    public byte[] photo;
    public String ext1;
    public String ecole;
    public String provinceEcole;
    public String provinceEducationnel;
    public String option;
    public String annee;
    public String datedemande;
    public int typeIdentificationcode;
    public String typeIdentification;
    public String raison;
    public int valider;
    public String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDatedemande() {
        return datedemande;
    }

    public void setDatedemande(String datedemande) {
        this.datedemande = datedemande;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public int getTypeIdentificationcode() {
        return typeIdentificationcode;
    }
    public void setTypeIdentificationcode(int typeIdentificationcode) {
        this.typeIdentificationcode = typeIdentificationcode;
    }
    public int getValider() {
        return valider;
    }
    public void setValider(int valider) {
        this.valider = valider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Timestamp getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Timestamp dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNompere() {
        return nompere;
    }

    public void setNompere(String nompere) {
        this.nompere = nompere;
    }

    public String getNommere() {
        return nommere;
    }

    public void setNommere(String nommere) {
        this.nommere = nommere;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getProvinceOrigine() {
        return provinceOrigine;
    }

    public void setProvinceOrigine(String provinceOrigine) {
        this.provinceOrigine = provinceOrigine;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getEcole() {
        return ecole;
    }

    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public String getProvinceEcole() {
        return provinceEcole;
    }

    public void setProvinceEcole(String provinceEcole) {
        this.provinceEcole = provinceEcole;
    }

    public String getProvinceEducationnel() {
        return provinceEducationnel;
    }

    public void setProvinceEducationnel(String provinceEducationnel) {
        this.provinceEducationnel = provinceEducationnel;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getTypeIdentification() {
        return typeIdentification;
    }

    public void setTypeIdentification(String typeIdentification) {
        this.typeIdentification = typeIdentification;
    }
}
