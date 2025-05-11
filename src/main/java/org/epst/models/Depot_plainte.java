package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class Depot_plainte extends PanacheEntity {
    public String envoyeur;
    public String telephone;
    public String email;
    public String destinateur;
    public String id_tiquet;

    @Lob
    public String message;
    public String id_statut;
    public String piecejointe_id;
    public String reference;
    public String date;
    public String province;
}
