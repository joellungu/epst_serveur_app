package org.epst.models.Agent;

import org.epst.models.mutuelle.Demande;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface AgentDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS AGENT  (" +
            "id bigint PRIMARY KEY," +
            "nom varchar," +
            "postnom varchar," +
            "prenom varchar," +
            "numero varchar," +
            "email varchar," +
            "adresse varchar," +
            "role INTEGER," +
            "matricule text," +
            "idstatut INTEGER," +
            "datenaissance varchar," +
            "mdp varchar,"+
            "province varchar,"+
            "district varchar"+
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO DEMANDES (id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,district,carte,piecejointe) values " +
            "(:id,:nom,:postnom,:prenom,:matricule,:direction,:services,:beneficiaire,:notes,:valider,:jour,:ext1,:ext2,:province,:district,:carte,:piecejointe)")
    void insertAgent(@BindBean Agent agent);

    @SqlUpdate("UPDATE DEMANDES SET valider = ? where id = ?")
    void setStatus(int status, Long id);
    @SqlUpdate("UPDATE DEMANDES SET valider = ?, cenome = ? where id = ?")
    void setExpirer(int status, String cemone, Long id);

    //
    @SingleValue
    @SqlQuery("SELECT valider FROM DEMANDES where id = ?")
    int getStatus(Long id);
    //getStatus

    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,district FROM DEMANDES where valider = 0 and province = ? and district = ?")
    @RegisterBeanMapper(Demande.class)//
    List<Demande> listeDeDemande(String province, String district);//
    @SqlQuery("SELECT id,nom,postnom,prenom,matricule,direction,services,beneficiaire,notes,valider,jour,ext1,ext2,province,district FROM DEMANDES where matricule = ?")
    @RegisterBeanMapper(Demande.class)//
    List<Demande> listeDeDemandeByMatricule(String matricule);//

    @SqlQuery("SELECT * FROM DEMANDES where id = ?")
    @RegisterBeanMapper(Demande.class)
    List<Demande> listeDeDemande(Long id);

}
