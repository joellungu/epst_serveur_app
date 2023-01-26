package org.epst.models.document_scolaire.transfere;

import org.epst.models.mutuelle.Demande;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.HashMap;
import java.util.List;

public interface TransfereDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS TRANSFERES  (" +
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
            "option_avant varchar," +
            "option_apres varchar," +
            "ecoleProvenance varchar,"+
            "ecoleProvenanceProv varchar,"+
            "ecoleProvenanceDistric varchar,"+
            "ecoleDestination varchar," +
            "ecoleDestinationProv varchar," +
            "ecoleDestinationDistric varchar," +
            "photo bytea,"+
            "ext1 varchar," +
            "datedemande varchar," +
            "reference varchar,"+
            "raison text," +
            "valider INTEGER" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO TRANSFERES (id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,option_avant,option_apres," +
            "ecoleProvenance,ecoleProvenanceProv,ecoleProvenanceDistric,ecoleDestination,ecoleDestinationProv,ecoleDestinationDistric," +
            "photo,ext1,datedemande,reference,valider) values " +
            "(:id,:nom,:postnom,:prenom,:sexe,:lieuNaissance,:dateNaissance,:telephone,:nompere,:nommere,:adresse,:provinceOrigine,:option_avant,:option_apres," +
            ":ecoleProvenance,:ecoleProvenanceProv,:ecoleProvenanceDistric,:ecoleDestination,:ecoleDestinationProv,:ecoleDestinationDistric," +
            ":photo,:ext1,:datedemande,:reference,:valider)")
    void insertDemande(@BindBean Transfere transfere);

    @SqlUpdate("UPDATE TRANSFERES SET valider = ?, raison = ? where id = ?")
    void setStatus(int status,String raison, Long id);
    @SqlUpdate("UPDATE TRANSFERES SET valider = ?, cenome = ? where id = ?")
    void setExpirer(int status, String cemone, Long id);

    //
    @SingleValue
    @SqlQuery("SELECT valider,raison FROM TRANSFERES where id = ?")
    @RegisterBeanMapper(Transfere.class)//
    Transfere getStatus(Long id);
    //getStatus

    @SqlQuery("SELECT id,nom,postnom,prenom,sexe,lieuNaissance,dateNaissance,telephone,nompere,nommere,adresse,provinceOrigine,option_avant,option_apres," +
            "ecoleProvenance,ecoleProvenanceProv,ecoleProvenanceDistric,ecoleDestination,ecoleDestinationProv,ecoleDestinationDistric," +
            "datedemande,reference,valider FROM TRANSFERES where valider = ? and ecoleDestinationProv = ? and ecoleDestinationDistric = ?")
    @RegisterBeanMapper(Transfere.class)//
    List<Transfere> listeDeDemande(int valider, String province, String district);//
    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,reference,district FROM TRANSFERES where matricule = ?")
    @RegisterBeanMapper(Transfere.class)//
    List<Transfere> listeDeDemandeByMatricule(String matricule);//

    @SqlQuery("SELECT * FROM TRANSFERES where id = ?")
    @RegisterBeanMapper(Transfere.class)
    List<Transfere> listeDeDemande(Long id);
    @SingleValue
    @SqlQuery("SELECT photo FROM TRANSFERES where id = ?")
    byte[] getPiecejointe(Long id);
    @SqlUpdate("ALTER TABLE TRANSFERES ADD datedemande varchar;")
    public void miseAjour();
}
