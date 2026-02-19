package org.epst.models.parcourscolaire;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class AbonnementParcourScolaire extends PanacheEntity {
    //
    public String codeEleve;
    public String anneeScolaire;
    public boolean status;
}
