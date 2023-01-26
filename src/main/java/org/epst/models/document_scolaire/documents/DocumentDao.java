package org.epst.models.document_scolaire.documents;

import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.mutuelle.Demande;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface DocumentDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS DOCUMENT (" +
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
            "documenrDemandecode INTEGER," +
            "raison text," +
            "reference varchar,"+
            "documenrDemande varchar" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO DOCUMENT (id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,photo,ext1,ecole,provinceEcole,provinceEducationnel,option,annee,datedemande,valider,documenrDemandecode,reference,documenrDemande) values " +
            "(:id,:nom,:postnom,:prenom,:sexe,:lieuNaissance,:dateNaissance,:telephone,:nompere,:nommere,:adresse,:provinceOrigine,:photo,:ext1,:ecole,:provinceEcole,:provinceEducationnel,:option,:annee,:datedemande,:valider,:documenrDemandecode,:reference,:documenrDemande)")
    void insertDemande(@BindBean Document document);

    @SqlUpdate("UPDATE DOCUMENT SET valider = ? where id = ?")
    void setStatus(int status, Long id);
    @SqlUpdate("UPDATE DOCUMENT SET valider = ?, cenome = ? where id = ?")
    void setExpirer(int status, String cemone, Long id);

    //
    @SingleValue
    @SqlQuery("SELECT valider,raison FROM DOCUMENT where id = ?")
    Document getStatus(Long id);
    //getStatus

    @SqlQuery("SELECT id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,ecole,provinceEcole,provinceEducationnel,option,annee,datedemande,valider,documenrDemandecode,reference,documenrDemande FROM DOCUMENT where valider = ? and provinceEcole = ? and provinceEducationnel = ?")
    @RegisterBeanMapper(Document.class)//
    List<Document> listeDeDemande(int valider, String province, String district);//
    @SqlQuery("SELECT id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,ecole,provinceEcole,provinceEducationnel,option,annee,datedemande,valider,documenrDemandecode,reference,documenrDemande FROM DOCUMENT where matricule = ?")
    @RegisterBeanMapper(Document.class)//
    List<Document> listeDeDemandeByMatricule(String matricule);//

    @SqlQuery("SELECT * FROM DOCUMENT where id = ?")
    @RegisterBeanMapper(Document.class)
    List<Document> listeDeDemande(Long id);
    @SingleValue
    @SqlQuery("SELECT photo FROM DOCUMENT where id = ?")
    byte[] getPiecejointe(Long id);
    @SqlUpdate("ALTER TABLE DOCUMENT ADD datedemande varchar;")
    public void miseAjour();

}
