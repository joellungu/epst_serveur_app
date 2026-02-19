package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

@Entity
public class Utilisateur extends PanacheEntity {
    public String nom;
    public String prenom;
    public String telephone;
    public String motDePasse;

    @Enumerated(EnumType.STRING)
    public Role role;

    //@ManyToOne
    public Long idClasse; //Classe
    public String classe;
    public String niveau;// uniquement pour les élèves

    public enum Role {
        ADMIN, ENSEIGNANT, ELEVE
    }
}