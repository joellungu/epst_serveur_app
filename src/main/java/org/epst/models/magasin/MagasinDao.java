package org.epst.models.magasin;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface MagasinDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS MAGASIN  (" +
            "id bigint PRIMARY KEY," +
            "libelle varchar," +
            "description TEXT," +
            "piecejointe bytea," +
            "dateenligne varchar," +
            "type varchar," +
            "extention varchar" +
            ")")
    void createTable();

}
