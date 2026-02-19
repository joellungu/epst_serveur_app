package org.epst.models.paiement;

import org.epst.models.palmares.Palmares;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PaiementDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS PAIEMENT  (" +
            "nom varchar," +
            "postnom varchar," +
            "prenom varchar," +
            "date varchar," +
            "type varchar," +
            "demande varchar," +
            "phone varchar," +
            "reference varchar," +
            "amount INTEGER," +
            "valider INTEGER DEFAULT 0," +
            "currency varchar" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO PAIEMENT (nom,postnom,prenom,date,type,demande,phone,reference,amount,currency) values " +
            "(:nom,:postnom,:prenom,:date,:type,:demande,:phone,:reference,:amount,:currency)")
    void insertPaiement(@BindBean Paiement paiement);

    @SqlUpdate("UPDATE DEMANDE SET valider = ? where reference = ?")
    void setValidation(int valider, String reference);

    @SqlQuery("SELECT * FROM PAIEMENT where phone = ? AND date = ?")
    @RegisterBeanMapper(Paiement.class)//
    List<Paiement> listeAll(String phone, String date);//

}
