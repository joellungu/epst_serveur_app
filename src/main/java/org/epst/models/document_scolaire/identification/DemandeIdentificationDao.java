package org.epst.models.document_scolaire.identification;

import org.epst.models.mutuelle.Demande;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface DemandeIdentificationDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS DEMANDEIDENTIFICATION  (" +
            "id bigint PRIMARY KEY," +
            "nom varchar," +
            "postnom varchar," +
            "prenom varchar," +
            "sexe varchar," +
            "lieuNaissance varchar," +
            "dateNaissance varchar," +
            "telephone varchar," +
            "nompere varchar," +
            "nommere text," +
            "adresse varchar," +
            "provinceOrigine varchar," +
            "photo bytea,"+
            "ext1 varchar,"+
            "ecole varchar,"+
            "provinceEcole varchar,"+
            "provinceEducationnel varchar," +
            "option varchar," +
            "annee varchar," +
            "datedemande varchar," +
            "valider INTEGER," +
            "typeIdentificationcode INTEGER," +
            "raison text," +
            "reference varchar,"+
            "typeIdentification varchar" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO DEMANDEIDENTIFICATION (id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,photo,ext1,ecole,provinceEcole,provinceEducationnel,option,annee,valider,typeIdentificationcode,reference,typeIdentification) values " +
            "(:id,:nom,:postnom,:prenom,:sexe,:lieuNaissance,:dateNaissance,:telephone,:nompere,:nommere,:adresse,:provinceOrigine,:photo,:ext1,:ecole,:provinceEcole,:provinceEducationnel,:option,:annee,:valider,:typeIdentificationcode,:reference,:typeIdentification)")
    void insertDemande(@BindBean DemandeIdentification demandeIdentification);

    @SqlUpdate("UPDATE DEMANDEIDENTIFICATION SET valider = ? where id = ?")
    void setStatus(int status, Long id);
    @SqlUpdate("UPDATE DEMANDEIDENTIFICATION SET valider = ?, cenome = ? where id = ?")
    void setExpirer(int status, String cemone, Long id);

    //
    @SingleValue
    @SqlQuery("SELECT valider,raison FROM DEMANDEIDENTIFICATION where id = ?")
    DemandeIdentification getStatus(Long id);
    //getStatus

    @SqlQuery("SELECT id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,ecole,provinceEcole,provinceEducationnel,option,annee,reference,datedemande,typeIdentification FROM DEMANDEIDENTIFICATION where valider = ? and provinceEcole = ? and provinceEducationnel = ? and typeIdentificationcode = ?")
    @RegisterBeanMapper(DemandeIdentification.class)//
    List<DemandeIdentification> listeDeDemande(int valider, String province, String district, int code);//
    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,reference,district FROM DEMANDEIDENTIFICATION where matricule = ?")
    @RegisterBeanMapper(Demande.class)//
    List<DemandeIdentification> listeDeDemandeByMatricule(String matricule);//

    @SqlQuery("SELECT * FROM DEMANDEIDENTIFICATION where id = ?")
    @RegisterBeanMapper(Demande.class)
    List<DemandeIdentification> listeDeDemande(Long id);
    @SingleValue
    @SqlQuery("SELECT photo FROM DEMANDEIDENTIFICATION where id = ?")
    byte[] getPiecejointe(Long id);
    @SqlUpdate("ALTER TABLE DEMANDEIDENTIFICATION ADD valider INTEGER;")
    public void miseAjour();
}
