package org.epst.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


//@Accessors(chain=true)
//@AllArgsConstructor
//@ToString
public class Utilisateur {

    /*
                statement.setLong(1, getId());
            statement.setString(2, utilisateur.getNom());
            statement.setString(3, utilisateur.getPostnom());
            statement.setString(4, utilisateur.getPrenom());
            statement.setString(5, utilisateur.getNumero());
            statement.setString(6, utilisateur.getEmail());
            statement.setString(7, utilisateur.getAdresse());
            statement.setInt(8, utilisateur.getRole());
            statement.setString(9, utilisateur.getMatricule());
            statement.setString(10, utilisateur.getId_statut());
            statement.setString(11, utilisateur.getDate_de_naissance());
                */
    public Utilisateur(
             Long id,
             String nom,
             String postnom,
             String prenom,
             String numero,
             String email,
             String adresse,
             int role,
             String matricule,
             String id_statut,
             String date_de_naissance,
             String mdp,
             String province,
             String district
            
        ){
        this.adresse = adresse;
        this.date_de_naissance = date_de_naissance;
        this.id = id;
        this.email = email;
        this.id_statut = id_statut;
        this.matricule = matricule;
        this.nom = nom;
        this.numero = numero;
        this.postnom = postnom;
        this.role = role;
        this.prenom = prenom;
        this.mdp = mdp;
        this.province = province;
        this.district = district;
    }

    public Long id;

    public String nom;

    public String postnom;

    public String prenom;

    public String date_de_naissance;

    public String numero;

    public String email;

    public String adresse;

    public int role;

    public String matricule;

    public String id_statut;

    public String mdp;

    public String province;

    public String district;
    //
    // Constructeur par défaut
    public Utilisateur() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPostnom() {
        return postnom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNumero() {
        return numero;
    }

    public String getEmail() {
        return email;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getRole() {
        return role;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getId_statut() {
        return id_statut;
    }

    public String getDate_de_naissance() {
        return date_de_naissance;
    }

    public String getMdp() {
        return mdp;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPostnom(String postnom) {
        this.postnom = postnom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setId_statut(String id_statut) {
        this.id_statut = id_statut;
    }

    public void setDate_de_naissance(String date_de_naissance) {
        this.date_de_naissance = date_de_naissance;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
