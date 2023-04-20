package org.epst.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class PieceJointe extends PanacheEntity {
    public long piecejointe_id;
    public byte[] donne;
    public String type;
}
