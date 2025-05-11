package org.epst.models.palmares;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Palmopglobal2022 extends PanacheEntity {

    private String
            Nom_Province,
            Code_Province,
            Nom_Centre,
            Code_centre,
            Option,
            Code_Option,
            Nom_ecole,
            Code_Ecole,
            Ordre_Ecole,
            Code_Gestion,
            Code_Candidat,
            Nom_Candidat,
            Sexe,
            Note,
            ANNEE_SCOLAIRE;
}
