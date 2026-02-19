package org.epst.models.devise;

import org.epst.models.Agent.Agent;
import org.epst.models.mutuelle.Demande;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface DeviseDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS Devise  (" +
            "id bigint PRIMARY KEY," +
            "nom varchar," +
            "montant int8 DEFAULT 2020"+
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO Devise (id,nom,montant) values " +
            "(:id,:nom,:montant)")
    void insertDevise(@BindBean Devise devise);

    @SqlQuery("select * from Devise where id = ?")
    @RegisterBeanMapper(Devise.class)
    Devise selectDevise(Long id);
}
