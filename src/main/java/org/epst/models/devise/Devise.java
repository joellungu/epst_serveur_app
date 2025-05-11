package org.epst.models.devise;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;

@Entity
public class Devise extends PanacheEntity {


    public String devise;
    public double taux;
}
