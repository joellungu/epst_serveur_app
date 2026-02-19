package org.epst.models.EnseignementDirect;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class AnneeScolaire extends PanacheEntity {
    public String nom; // ex: 2024-2025
    public LocalDate debut;
    public LocalDate fin;
}
