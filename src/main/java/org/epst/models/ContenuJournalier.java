package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class ContenuJournalier extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public LocalDate date;

    @Column(nullable = false)
    public String type; // "VIDEO", "NOTE", "DEVOIR"

    public String titre;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Lob
    public byte[] fichier; // fichier binaire (PDF, MP4, image, etc.)

    @Column(nullable = false)
    public Long idClasse;

    @Column(nullable = false)
    public Long idCours;

    @Column(nullable = false)
    public Long idInspecteur;

    public String anneeScolaire;

    public LocalDateTime updatedAt = LocalDateTime.now();

    public boolean synced = false;

    @Column(unique = true, updatable = false)
    public String cle = UUID.randomUUID().toString();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 🔎 Méthodes utilitaires
    public static ContenuJournalier findByClasseAndDate(Long idClasse, LocalDate date) {
        return find("idClasse = ?1 and date = ?2", idClasse, date).firstResult();
    }

    public static List<ContenuJournalier> findByClasse(Long idClasse) {
        return find("idClasse = ?1 ORDER BY date DESC", idClasse).list();
    }

    public static List<ContenuJournalier> findByClasseAndType(Long idClasse, String type) {
        return find("idClasse = ?1 and type = ?2 ORDER BY date DESC", idClasse, type).list();
    }
}
