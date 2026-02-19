package org.epst.models;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;

public class ContenuForm {

    @PartType(MediaType.TEXT_PLAIN)
    public String titre;

    @PartType(MediaType.TEXT_PLAIN)
    public String description;

    @PartType(MediaType.TEXT_PLAIN)
    public String type; // VIDEO, NOTE, DEVOIR

    @PartType(MediaType.TEXT_PLAIN)
    public Long idClasse;

    @PartType(MediaType.TEXT_PLAIN)
    public Long idCours;

    @PartType(MediaType.TEXT_PLAIN)
    public Long idInspecteur;

    @PartType(MediaType.TEXT_PLAIN)
    public String anneeScolaire;

    @PartType(MediaType.TEXT_PLAIN)
    public String date;

    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] fichier;
}
