package org.epst.models.Cours;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Cours extends PanacheEntity {
    public String cours;
    public String propriete;//Eleve ou les enseignants
    public String banche; // Pour la cas de Francais par ex on a Conjugaison, Grammaire etc
    public String categorie;//Maternelle , Education de base et secondaire
    public int chapitre;//
    public String notion;//

    public String type;// Type de contenue (audio, video, pdf)
    public int classe; // Ex 1er primaire ...

    public byte[] data;
    //
}
