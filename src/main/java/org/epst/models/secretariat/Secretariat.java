package org.epst.models.secretariat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Secretariat extends PanacheEntity {
    @Column(name = "denomition")
    public String denomination;
    public String sigle;
    public String adresse;

    @Lob
    public byte[] photoProfil;

    public String telephone;
    public String email;
    public String responsable;

    @Column(columnDefinition = "bytea")
    public byte[] maps;

    @OneToMany(mappedBy = "secretariat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    public List<Departement> departements = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Arrete arrete;

    @Column(columnDefinition = "TEXT")
    public String attributionMission;

    @Column(columnDefinition = "TEXT")
    public String historique;

    @Column(columnDefinition = "TEXT")
    public String realisation;
}
