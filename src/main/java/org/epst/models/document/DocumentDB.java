package org.epst.models.document;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class DocumentDB extends PanacheEntity {
    public String nom ;
    public String postnom ;
    public String prenom ;
    public String sexe ;
    public String lieuNaissance ;
    public String dateNaissance ;
    public String telephone ;
    public String nompere ;
    public String nommere ;
    public String adresse ;
    public String provinceOrigine ;
    public byte[] photo ;
    public String ext1;
    public String ecole ;
    public String provinceEcole ;
    public String provinceEducationnel ;
    public String option ;
    public String annee ;
    public String datedemande ;
    public int valider ;
    public int documenrDemandecode ;

    @Lob
    public String raison;

    public String reference ;
    public String documenrDemande ;
}
