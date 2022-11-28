package org.epst.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


//@Accessors(chain=true)
//@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor // <--- THIS is it
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
}
