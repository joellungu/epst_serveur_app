package org.epst.models.apprenant;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Apprenant extends PanacheEntity {
    public String nom;
    public String postnom;
    public String prenom;
    public int type;
    public String telephone;
    public String codeUnique;

}
