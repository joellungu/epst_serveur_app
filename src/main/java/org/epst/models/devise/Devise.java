package org.epst.models.devise;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

@Entity
public class Devise extends PanacheEntity {


    public String devise;
    public double taux;
}
