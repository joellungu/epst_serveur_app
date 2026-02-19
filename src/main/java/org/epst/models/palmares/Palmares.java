package org.epst.models.palmares;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

@Entity
@Cacheable
public class Palmares extends PanacheEntity {
    public String nmpr;
    public String cdpr;
    public String nmctr;
    public String cdctr;
    public String nmop;
    public String cdop;
    public String nmets;
    public String cdets;
    public String ordets;
    public String cdgst;
    public String nid;
    public String cdcdt;
    public String nmscdt;
    public String prct;
    public String section;
    public String sx;
    public String ecolecode;
    public String annescolaire;

}
