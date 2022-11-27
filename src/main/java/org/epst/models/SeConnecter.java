package org.epst.models;

import org.jdbi.v3.core.Jdbi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class SeConnecter {

    public static Jdbi jdbi = Jdbi.create
            ("jdbc:postgresql://localhost:5432/postgres", "postgres", "joellungu");
    /*
                ("jdbc:postgresql://ec2-44-209-57-4.compute-1.amazonaws.com:5432/ddivp7ga07bltc",
                "elywutaxvrrkea",
                "4308f845fd0dd585b8a6ca2ac78b0d77e8da3d3918f2e7a86c8cd86d73e979e2");
    */

    public Connection con;
    //
    //ResultSet résultats = null;
    //

    public SeConnecter(){
        try{
            //String dbUrl = System.getenv("JDBC_DATABASE_URL");
            //con = DriverManager.getConnection(dbUrl);
            //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "joellungu");
            //con = DriverManager.getConnection(
            //        "jdbc:postgres://ec2-44-209-57-4.compute-1.amazonaws.com:5432/ddivp7ga07bltc",
            //"elywutaxvrrkea",
            //"4308f845fd0dd585b8a6ca2ac78b0d77e8da3d3918f2e7a86c8cd86d73e979e2");

            //4308f845fd0dd585b8a6ca2ac78b0d77e8da3d3918f2e7a86c8cd86d73e979e2
            //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
            //"postgres",
            //"joellungu");
            /**/
            ///////////////////////////////////////////////////////////////////////////////////////////////////
            con = DriverManager.getConnection("jdbc:postgresql://ec2-44-209-57-4.compute-1.amazonaws.com:5432/ddivp7ga07bltc",
            "elywutaxvrrkea",
            "4308f845fd0dd585b8a6ca2ac78b0d77e8da3d3918f2e7a86c8cd86d73e979e2");
            //Statement stmt = con.createStatement();
            //
            //stmt.executeUpdate(sql);
            //
        }catch(Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }
    }
}
