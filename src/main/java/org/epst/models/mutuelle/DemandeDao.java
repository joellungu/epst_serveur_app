package org.epst.models.mutuelle;

import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

public interface DemandeDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS DEMANDE  (" +
            "id bigint PRIMARY KEY," +
            "nom varchar," +
            "postnom varchar," +
            "prenom varchar," +
            "matricule varchar," +
            "direction varchar," +
            "services varchar," +
            "beneficiaire varchar," +
            "notes text," +
            "valider INTEGER," +
            "jour varchar," +
            "ext1 varchar,"+
            "ext2 varchar,"+
            "province varchar,"+
            "district varchar,"+
            "cenome varchar," +
            "piecejointe bytea," +
            "carte bytea" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO DEMANDE (id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,district,carte,piecejointe) values " +
            "(:id,:nom,:postnom,:prenom,:matricule,:direction,:services,:beneficiaire,:notes,:valider,:jour,:ext1,:ext2,:province,:district,:carte,:piecejointe)")
    void insertDemande(@BindBean Demande demande);

    @SqlUpdate("UPDATE DEMANDE SET valider = ? where id = ?")
    void setStatus(int status, Long id);
    @SqlUpdate("UPDATE DEMANDE SET valider = ?, cenome = ? where id = ?")
    void setExpirer(int status, String cemone, Long id);

    //
    @SingleValue
    @SqlQuery("SELECT valider FROM DEMANDE where id = ?")
    int getStatus(Long id);
    //getStatus

    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,district FROM DEMANDE where valider = 0 and province = ? and district = ?")
    @RegisterBeanMapper(Demande.class)//
    List<Demande> listeDeDemande(String province, String district);//
    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,district FROM DEMANDE where matricule = ?")
    @RegisterBeanMapper(Demande.class)//
    List<Demande> listeDeDemandeByMatricule(String matricule);//

    @SqlQuery("SELECT * FROM DEMANDE where id = ?")
    @RegisterBeanMapper(Demande.class)
    List<Demande> listeDeDemande(Long id);

    @SingleValue
    @SqlQuery("SELECT carte FROM DEMANDE where id = ?")
    byte[] getCarte(Long id);
    @SingleValue
    @SqlQuery("SELECT piecejointe FROM DEMANDE where id = ?")
    byte[] getPiecejointe(Long id);

    //@SqlUpdate("ALTER TABLE DEMANDES ADD ext1 varchar;")
    //public void miseAjour();

}
