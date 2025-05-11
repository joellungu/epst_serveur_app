package org.epst.models.secretariat;


import jakarta.persistence.Embeddable;

@Embeddable
public class Departement {
    public String responsable;
    public String departement;
    public byte[] photo;

}
