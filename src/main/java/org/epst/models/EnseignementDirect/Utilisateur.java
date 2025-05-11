package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class Utilisateur extends PanacheEntity {
    public String nom;
    public String prenom;
    public String email;
    public String motDePasse;

    @Enumerated(EnumType.STRING)
    public Role role;

    @ManyToOne
    public Classe classe; // uniquement pour les élèves

    public enum Role {
        ADMIN, ENSEIGNANT, ELEVE
    }
}