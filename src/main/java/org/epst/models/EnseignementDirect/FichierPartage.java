package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class FichierPartage extends PanacheEntity {
    public String nomFichier;
    public String urlFichier;

    @ManyToOne
    public SessionCours sessionCours;
}
