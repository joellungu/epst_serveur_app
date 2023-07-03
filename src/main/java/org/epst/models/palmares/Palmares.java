package org.epst.models.palmares;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

@Entity
@Cacheable
public class Palmares extends PanacheEntity {
    String nomprovince ;
    String codeprovince ;
    String nomcentre ;
    String codecentre ;
    String option ;
    String codeoption ;
    String nomecole ;
    String codeecole ;
    int ordreecole ;
    int codegestion ;
    int pourcentage ;
    String codecandidat ;
    String nomcandidat ;
    String sexe ;
    String note ;
    String anneescolaire;

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public String getNomprovince() {
        return nomprovince;
    }

    public void setNomprovince(String nomprovince) {
        this.nomprovince = nomprovince;
    }

    public String getCodeprovince() {
        return codeprovince;
    }

    public void setCodeprovince(String codeprovince) {
        this.codeprovince = codeprovince;
    }

    public String getNomcentre() {
        return nomcentre;
    }

    public void setNomcentre(String nomcentre) {
        this.nomcentre = nomcentre;
    }

    public String getCodecentre() {
        return codecentre;
    }

    public void setCodecentre(String codecentre) {
        this.codecentre = codecentre;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getCodeoption() {
        return codeoption;
    }

    public void setCodeoption(String codeoption) {
        this.codeoption = codeoption;
    }

    public String getNomecole() {
        return nomecole;
    }

    public void setNomecole(String nomecole) {
        this.nomecole = nomecole;
    }

    public String getCodeecole() {
        return codeecole;
    }

    public void setCodeecole(String codeecole) {
        this.codeecole = codeecole;
    }

    public int getOrdreecole() {
        return ordreecole;
    }

    public void setOrdreecole(int ordreecole) {
        this.ordreecole = ordreecole;
    }

    public int getCodegestion() {
        return codegestion;
    }

    public void setCodegestion(int codegestion) {
        this.codegestion = codegestion;
    }

    public String getCodecandidat() {
        return codecandidat;
    }

    public void setCodecandidat(String codecandidat) {
        this.codecandidat = codecandidat;
    }

    public String getNomcandidat() {
        return nomcandidat;
    }

    public void setNomcandidat(String nomcandidat) {
        this.nomcandidat = nomcandidat;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAnneescolaire() {
        return anneescolaire;
    }

    public void setAnneescolaire(String anneescolaire) {
        this.anneescolaire = anneescolaire;
    }
}
