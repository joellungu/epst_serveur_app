package org.epst.models.secretariat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
public class Secretariat extends PanacheEntity {
    public String denomition;
    public String sigle;
    public String adresse;

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
