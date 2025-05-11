package org.epst.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;

@Accessors(chain=true)
//@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor // <--- THIS is it
@ToString
@Entity
public class NoteTraitementBean extends PanacheEntity {
    public Long id;
    public String nomIdmin;
    public String reference;
    public String note;

    public NoteTraitementBean(Long id, String nomIdmin, String reference, String note){
        this.id = id; this.nomIdmin = nomIdmin; this.note = note; this.reference = reference;
    }
}
