package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Travail extends PanacheEntity {
    public String titre;
    public String description;
    public LocalDate dateLimite;

    @ManyToOne
    public CoursDirect coursDirect;

    public TypeTravail type; // Devoir, Examen, Interrogation

    public enum TypeTravail {
        DEVOIR, EXAMEN, INTERROGATION
    }
}
