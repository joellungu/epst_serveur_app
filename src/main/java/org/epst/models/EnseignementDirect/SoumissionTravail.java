package org.epst.models.EnseignementDirect;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class SoumissionTravail extends PanacheEntity {
    @ManyToOne
    public Travail travail;

    @ManyToOne
    public Utilisateur eleve;

    public String urlFichier;
    public LocalDateTime dateSoumission;
}
