package org.epst.beans;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


//@Entity
public class Magasin {//PanacheEntityBase

    public Magasin(Long id, String libelle, String descirption, byte[] piecejointe, String types, String date, String extention){
        this.date = date; this.description = descirption; this.id = id; this.libelle = libelle; this.piecejointe = piecejointe;
        this.types = types; this.extention = extention;
    }

    public Magasin(){}

    /*
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
    */

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String libelle;

    public String description;

    public String date;

    public byte[] piecejointe;

    public String types;

    public String extention;

    //
    // Getters
    public Long getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public byte[] getPiecejointe() {
        return piecejointe;
    }

    public String getTypes() {
        return types;
    }

    public String getExtention() {
        return extention;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPiecejointe(byte[] piecejointe) {
        this.piecejointe = piecejointe;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

}
