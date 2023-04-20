package org.epst.models.document_scolaire;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.sql.Date;

@Entity
public class DemandeDiplome extends PanacheEntity {
    Long id;
    String nom;
    String postnom;
    String prenom;
    char sexe;
    Date datenaissance;
    String telephone;
    String nompere;
    String nommere;
    String adresse;
    String provinceorigine;
    byte[] photo;
    String ext1;
    String province;

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

    public char getSexe() {
        return sexe;
    }

    public void setSexe(char sexe) {
        this.sexe = sexe;
    }

    public Date getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(Date datenaissance) {
        this.datenaissance = datenaissance;
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

    public String getProvinceorigine() {
        return provinceorigine;
    }

    public void setProvinceorigine(String provinceorigine) {
        this.provinceorigine = provinceorigine;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
