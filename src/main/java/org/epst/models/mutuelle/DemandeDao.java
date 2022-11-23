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

    @SqlUpdate("CREATE TABLE IF NOT EXISTS DEMANDES  (" +
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
            "ext varchar,"+
            "carte bytea" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO DEMANDES (id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext,carte) values " +
            "(:id,:nom,:postnom,:prenom,:matricule,:direction,:services,:beneficiaire,:notes,:valider,:jour,:ext,:carte )")
    void insertDemande(@BindBean Demande demande);

    @SqlUpdate("UPDATE DEMANDES SET valider = ? where id = ?")
    void setStatus(int status, Long id);
    //
    @SingleValue
    @SqlQuery("SELECT valider FROM DEMANDES where matricule = ?")
    int getStatus(String matricule);
    //getStatus

    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext FROM DEMANDES where valider = 0")
    @RegisterBeanMapper(Demande.class)
    List<Demande> listeDeDemande();

    @SqlQuery("SELECT * FROM DEMANDES where id = ?")
    @RegisterBeanMapper(Demande.class)
    List<Demande> listeDeDemande(Long id);


    @SingleValue
    @SqlQuery("SELECT carte FROM DEMANDES where id = ?")
    byte[] getCarte(Long id);

    @SqlUpdate("ALTER TABLE DEMANDES ADD ext varchar;")
    public void miseAjour();

}
