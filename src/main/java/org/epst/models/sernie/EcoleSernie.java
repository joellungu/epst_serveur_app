package org.epst.models.sernie;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class EcoleSernie extends PanacheEntity {
    public String denomination_ecole;
    public String code_ecole;

}
