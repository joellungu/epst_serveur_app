package org.epst.models.Cours;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.HashMap;

@Entity
public class Cours extends PanacheEntity {

    String classe;
    HashMap matiere;

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public HashMap getMatiere() {
        return matiere;
    }

    public void setMatiere(HashMap matiere) {
        this.matiere = matiere;
    }
}
