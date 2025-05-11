package org.epst.models.document_scolaire.transfere;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Transfere extends PanacheEntity {
    //Long id;
    public String nom;
    public String postnom;
    public String prenom;
    public String sexe;
    public String lieuNaissance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate dateNaissance;
    public String telephone;
    public String nompere;
    public String nommere;
    public String adresse;
    public String option_avant;
    public String option_apres;
    public String provinceOrigine;
    public String ecoleProvenance;
    public String ecoleProvenanceProv;
    public String ecoleProvenanceDistric;
    public String ecoleDestination;
    public String ecoleDestinationProv;
    public String ecoleDestinationDistric;
    public byte[] photo;
    public String ext1;
    public String datedemande;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
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

    public String getOption_avant() {
        return option_avant;
    }

    public void setOption_avant(String option_avant) {
        this.option_avant = option_avant;
    }

    public String getOption_apres() {
        return option_apres;
    }

    public void setOption_apres(String option_apres) {
        this.option_apres = option_apres;
    }

    public String getProvinceOrigine() {
        return provinceOrigine;
    }

    public void setProvinceOrigine(String provinceOrigine) {
        this.provinceOrigine = provinceOrigine;
    }

    public String getEcoleProvenance() {
        return ecoleProvenance;
    }

    public void setEcoleProvenance(String ecoleProvenance) {
        this.ecoleProvenance = ecoleProvenance;
    }

    public String getEcoleProvenanceProv() {
        return ecoleProvenanceProv;
    }

    public void setEcoleProvenanceProv(String ecoleProvenanceProv) {
        this.ecoleProvenanceProv = ecoleProvenanceProv;
    }

    public String getEcoleProvenanceDistric() {
        return ecoleProvenanceDistric;
    }

    public void setEcoleProvenanceDistric(String ecoleProvenanceDistric) {
        this.ecoleProvenanceDistric = ecoleProvenanceDistric;
    }

    public String getEcoleDestination() {
        return ecoleDestination;
    }

    public void setEcoleDestination(String ecoleDestination) {
        this.ecoleDestination = ecoleDestination;
    }

    public String getEcoleDestinationProv() {
        return ecoleDestinationProv;
    }

    public void setEcoleDestinationProv(String ecoleDestinationProv) {
        this.ecoleDestinationProv = ecoleDestinationProv;
    }

    public String getEcoleDestinationDistric() {
        return ecoleDestinationDistric;
    }

    public void setEcoleDestinationDistric(String ecoleDestinationDistric) {
        this.ecoleDestinationDistric = ecoleDestinationDistric;
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

    public int getValider() {
        return valider;
    }

    public void setValider(int valider) {
        this.valider = valider;
    }
}
