package org.epst.models.palmares;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Palmares2013 extends PanacheEntity {

    public String NmPr;
    public String CdPr;
    public String NmCtr;
    public String CdCtr;
    public String NmOp;
    public String CdOp;
    public String NmEts;
    public String CdEts;
    public String OrdEts;
    public String CdGst;
    public String NID;
    public String CdCdt;
    public String NmsCdt;
    public String prct;
    public String Section;
    public String sx;
    public String EcoleCode;
    public String annescolaire;
}
