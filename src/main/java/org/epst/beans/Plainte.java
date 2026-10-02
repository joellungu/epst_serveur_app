package org.epst.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;
import java.util.List;


@Entity
public class Plainte extends PanacheEntity {

    public Plainte(){};

    public Plainte(
        //Long id,
        String envoyeur,
        String telephone,
        String email,
        String destinateur,
        String id_tiquet,
        String message,
        int id_statut,
        long piecejointe_id,
        String reference,
        String date,
        String province
    ){
        //this.id = id;
        this.envoyeur = envoyeur;
        this.telephone = telephone;
        this.email = email;
        this.destinateur = destinateur;
        this.id_tiquet = id_tiquet;
        this.message = message;
        this.id_statut = id_statut;
        this.piecejointe_id = piecejointe_id;
        this.reference = reference;
        this.date = date;
        this.province = province;
    }
    
    //public Long id;
    public String envoyeur;
    public String telephone;
    public String email;
    public String destinateur;
    public String id_tiquet;
    public String message;
    public int id_statut;
    public long piecejointe_id;
    public String reference;
    public String date;
    public String province;
    public Long test;
    // Nouvelles informations du formulaire MGP
    public String nom;
    public String postnom;
    public String prenom;
    public String sexe;
    public String etablissement;
    public String profil;
    public String province_education;
    // Géolocalisation (récupérée avec l'accord de l'utilisateur)
    public double latitude;
    public double longitude;

    // Getters
    public String getEnvoyeur() {
        return envoyeur;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getDestinateur() {
        return destinateur;
    }

    public String getId_tiquet() {
        return id_tiquet;
    }

    public String getMessage() {
        return message;
    }

    public int getId_statut() {
        return id_statut;
    }

    public long getPiecejointe_id() {
        return piecejointe_id;
    }

    public String getReference() {
        return reference;
    }

    public String getDate() {
        return date;
    }

    public String getProvince() {
        return province;
    }

    // Setters
    public void setEnvoyeur(String envoyeur) {
        this.envoyeur = envoyeur;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDestinateur(String destinateur) {
        this.destinateur = destinateur;
    }

    public void setId_tiquet(String id_tiquet) {
        this.id_tiquet = id_tiquet;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId_statut(int id_statut) {
        this.id_statut = id_statut;
    }

    public void setPiecejointe_id(long piecejointe_id) {
        this.piecejointe_id = piecejointe_id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    // Getters des nouvelles informations
    public String getNom() {
        return nom;
    }

    public String getPostnom() {
        return postnom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public String getProfil() {
        return profil;
    }

    public String getProvince_education() {
        return province_education;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Setters des nouvelles informations
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPostnom(String postnom) {
        this.postnom = postnom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public void setProvince_education(String province_education) {
        this.province_education = province_education;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
