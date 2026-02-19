package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class MessageCours extends PanacheEntity {
    @ManyToOne
    public SessionCours sessionCours;

    @ManyToOne
    public Utilisateur auteur;

    public String message;
    public LocalDateTime dateEnvoi;
}
