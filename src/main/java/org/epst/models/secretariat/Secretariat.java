package org.epst.models.secretariat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
public class Secretariat extends PanacheEntity {
    public String denomition;
    public String sigle;
    public String adresse;
    public byte[] photo;
    public String telephone;
    public String email;
    public String responsable;
    public byte[] maps;
    //
    @ElementCollection
    public List<Departement> departement;
    public HashMap<String,Object> arretes;

    @Column(columnDefinition = "TEXT")
    public String attributionMission;

    @Column(columnDefinition = "TEXT")
    public String historique;

    @Column(columnDefinition = "TEXT")
    public String realisation;
    //
}
