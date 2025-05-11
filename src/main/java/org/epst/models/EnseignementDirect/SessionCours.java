package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class SessionCours extends PanacheEntity {
    @ManyToOne
    public CoursDirect coursDirect;

    public LocalDateTime dateDebut;
    public LocalDateTime dateFin;

    public String urlStreaming; // lien live ou replay

    @ManyToOne
    public Utilisateur enseignant;
}
