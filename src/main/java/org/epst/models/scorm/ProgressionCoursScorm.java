package org.epst.models.scorm;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "progression_cours_scorm",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_scorm_progression_client_sync", columnNames = {"clientSyncId"})
        },
        indexes = {
                @Index(name = "idx_scorm_progression_apprenant", columnList = "numeroIdentifiant, cle"),
                @Index(name = "idx_scorm_progression_cours", columnList = "numeroIdentifiant, cle, courseId"),
                @Index(name = "idx_scorm_progression_sync", columnList = "statutSynchronisation"),
                @Index(name = "idx_scorm_progression_sequence", columnList = "numeroIdentifiant, cle, courseId, sequenceNumber")
        }
)
public class ProgressionCoursScorm extends PanacheEntity {

    @Column(nullable = false, length = 100)
    public String numeroIdentifiant;

    @Column(nullable = false, length = 150)
    public String cle;

    @Column(nullable = false, length = 150)
    public String courseId;

    @Column(length = 255)
    public String courseTitle;

    @Column(nullable = false, length = 120)
    public String clientSyncId;

    public Long sequenceNumber;

    @Column(length = 80)
    public String typeAvancee;

    @Column(length = 80)
    public String derniereAction;

    @Column(length = 50)
    public String lessonStatus;

    public Double progressPercent;

    public Double scoreRaw;

    public Double scoreMin;

    public Double scoreMax;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String lessonLocation;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String suspendData;

    @Column(length = 50)
    public String sessionTime;

    @Column(length = 50)
    public String totalTime;

    public Long tempsPasseSecondes;

    public Integer nombreInteractions;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String interactionsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String donneesScormJson;

    @Column(nullable = false, length = 40)
    public String statutSynchronisation = "SYNCHRONISE";

    public LocalDateTime clientCreatedAt;

    public LocalDateTime synchronizedAt;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (synchronizedAt == null) {
            synchronizedAt = now;
        }
        if (statutSynchronisation == null || statutSynchronisation.isBlank()) {
            statutSynchronisation = "SYNCHRONISE";
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
