package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inspecteur_cours")
public class InspecteurCours extends PanacheEntity {

    public Long idInspecteur;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<Long> cours = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    public List<UUID> classe = new ArrayList<>();

    // Méthodes utilitaires
    public void addCours(Long coursId) {
        if (this.cours == null) {
            this.cours = new ArrayList<>();
        }
        if (!this.cours.contains(coursId)) {
            this.cours.add(coursId);
        }
    }

    public void removeCours(Long coursId) {
        if (this.cours != null) {
            this.cours.remove(coursId);
        }
    }

    public void addClasse(UUID classeId) {
        if (this.classe == null) {
            this.classe = new ArrayList<>();
        }
        if (!this.classe.contains(classeId)) {
            this.classe.add(classeId);
        }
    }

    public void removeClasse(Long classeId) {
        if (this.classe != null) {
            this.classe.remove(classeId);
        }
    }
}