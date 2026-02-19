package org.epst.models.sernie;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.sql.Timestamp;


@Entity
public class Sernie extends PanacheEntity {
    /*
    public String Nom;
    public String Postnom;
    public String Prenom;
    public String Sexe;
    public String Lieu_naissance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Timestamp Date_naissance;
    public String Nationalite;
    public String Telephone_principale;
    public String Nom_pere;
    public String Nom_mere;
    public String Adresse;
    public String DENOMINATIO_ECOLE;
    public String Code_Ecole;
    public byte[] photo;
    public String ext1;
    public String ecole;
    public String Antenne;
    public String niveau;
    public String provinceEcole;
    public String provinceEducationnel;
    public String option;
    public String Annee_scolaire;

    public String Classe;
    public String datedemande;
    public int typeIdentificationcode;
    public String typeIdentification;
    public String Province_origine;
    public String Territoire;
    public String Secteur;
    public String Groupement;
    public String Village;
    public String raison;
    public int valider;
    public String reference;
    */
    public String code;
    public String nom;
    public String postnom;
    public String prenom;
    public String sexe;
    public String nompere;
    public String nommere;
    public String telephone;
    public String adresse;
    public String territoire;
    public String secteur;
    public String groupement;
    public String village;
    public String nationalite;
    public String antenne;
    public String code_antenne;
    public String provinceOrigine;
    public String lieuNaissance;
    public String dateNaissance;
    public String ecole;
    public String code_ecole;
    public String adressePhysiqueEcole;
    public String reseaux;
    public String niveau;
    public String provinceEcole;
    public String provinceEducationnel;
    public String option;
    public String annee;
    public String classe;
    public String datedemande;

    //@Column(columnDefinition = "varchar default '0'")
    public String valider;
    /**
     *
     */
    public byte[] photo;
    public String ext;

}
