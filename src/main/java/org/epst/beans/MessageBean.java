package org.epst.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;

@Accessors(chain=true)
//@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor // <--- THIS is it
@AllArgsConstructor
@ToString
@Entity
public class MessageBean
        extends PanacheEntity
        {

    private String fromt;
    private String tot;
    private String contentt;
    private String hostIdt;
    private String clientIdt;
    private Boolean closet;
    private Boolean allst;
    private String visiblet;
    private Boolean conversationt;
    private String matriculet;
    private String datet;
    private String heuret;

}
