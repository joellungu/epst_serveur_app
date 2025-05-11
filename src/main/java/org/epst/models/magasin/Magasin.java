package org.epst.models.magasin;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Magasin extends PanacheEntity {
    //Long id;
    String libelle;
    String description;
    byte[] piecejointe;
    String dateenligne;
    String type;
    String extention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPiecejointe() {
        return piecejointe;
    }

    public void setPiecejointe(byte[] piecejointe) {
        this.piecejointe = piecejointe;
    }

    public String getDateenligne() {
        return dateenligne;
    }

    public void setDateenligne(String dateenligne) {
        this.dateenligne = dateenligne;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }
}
