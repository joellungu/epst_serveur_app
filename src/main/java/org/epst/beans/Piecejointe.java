package org.epst.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain=true)
//@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor // <--- THIS is it
@ToString
public class Piecejointe {

    public Piecejointe(
        Long id,
        Long piecejointe_id,
        byte[] donne,
        String type
    ){
        this.id = id;
        this.piecejointe_id = piecejointe_id;
        this.donne = donne;
        this.type = type;
    }

    public Long id;
    public Long piecejointe_id;
    public byte[] donne;
    public String type;

    //
    public Long getId() {
        return id;
    }

    public Long getPiecejointe_id() {
        return piecejointe_id;
    }

    public byte[] getDonne() {
        return donne;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPiecejointe_id(Long piecejointe_id) {
        this.piecejointe_id = piecejointe_id;
    }

    public void setDonne(byte[] donne) {
        this.donne = donne;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
