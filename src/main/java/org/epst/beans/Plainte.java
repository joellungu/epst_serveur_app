package org.epst.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain=true)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Plainte {

    public Plainte(
        Long id,
        String envoyeur,
        String telephone,
        String email,
        String destinateur,
        String id_tiquet,
        String message,
        String id_statut,
        String piecejointe_id,
        String reference,
        String date,
        String province
    ){
        this.id = id;
        this.envoyeur = envoyeur;
        this.telephone = telephone;
        this.email = email;
        this.destinateur = destinateur;
        this.id_tiquet = id_tiquet;
        this.message = message;
        this.id_statut = id_statut;
        this.piecejointe_id = piecejointe_id;
        this.reference = reference;
        this.date = date;
        this.province = province;
    }
    
    public Long id;
    public String envoyeur;
    public String telephone;
    public String email;
    public String destinateur;
    public String id_tiquet;
    public String message;
    public String id_statut;
    public String piecejointe_id;
    public String reference;
    public String date;
    public String province;

}
