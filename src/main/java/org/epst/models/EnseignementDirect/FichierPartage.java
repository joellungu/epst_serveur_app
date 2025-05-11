package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class FichierPartage extends PanacheEntity {
    public String nomFichier;
    public String urlFichier;

    @ManyToOne
    public SessionCours sessionCours;
}
