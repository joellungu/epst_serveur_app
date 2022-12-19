package org.epst.models.autres;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface Autres {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS archive  (" +
            "fromt varchar," +
            "tot varchar," +
            "contentt varchar," +
            "hostidt varchar," +
            "clientidt varchar," +
            "closet bool," +
            "allt bool," +
            "visiblet varchar," +
            "conversation bool," +
            "matriculet varchar," +
            "datet varchar,"+
            "heure varchar"+
            ")")
    void createTableArchive();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS archivetchat  (" +
            "id bigint PRIMARY KEY," +
            "date_save date," +
            "nom_agent varchar," +
            "nom_client varchar,"+
            "conversation TEXT"+
            ")")
    void createTableArchivechat();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS cours  (" +
            "id bigint PRIMARY KEY," +
            "classe varchar," +
            "matiere bytea"+
            ")")
    void createTableCours();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS depot_plainte  (" +
            "id bigint PRIMARY KEY," +
            "envoyeur varchar," +
            "telephone varchar," +
            "email varchar," +
            "destinateur varchar," +
            "id_tiquet varchar," +
            "message TEXT," +
            "id_statut varchar," +
            "piecejointe_id varchar," +
            "reference varchar," +
            "date varchar," +
            "province varchar"+
            ")")
    void createTableDepotPlainte();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS MAGASIN  (" +
            "id bigint PRIMARY KEY," +
            "libelle varchar," +
            "description TEXT," +
            "piecejointe bytea," +
            "date_mise_en_ligne varchar," +
            "types varchar," +
            "extention varchar"+
            ")")
    void createTableMagasin();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS messagebean  (" +
            "id bigint PRIMARY KEY," +
            "allst bool," +
            "clientidt varchar," +
            "closet bool," +
            "contentt varchar," +
            "conversation bool," +
            "datet varchar," +
            "fromt INTEGER," +
            "heuret text," +
            "hostidt INTEGER," +
            "matricule varchar," +
            "tot varchar,"+
            "visiblet varchar,"+
            "district varchar"+
            ")")
    void createTableMessagebean();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS note_traitement  (" +
            "id bigint PRIMARY KEY," +
            "nom_admin varchar," +
            "reference varchar," +
            "note TEXT"+
            ")")
    void createTableNoteTraitement();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS PIECEJOINTE  (" +
            "id bigint PRIMARY KEY," +
            "piecejointe_id bigint," +
            "donne bytea," +
            "type varchar"+
            ")")
    void createTablePiecejointe();

}
