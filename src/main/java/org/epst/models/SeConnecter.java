package org.epst.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class SeConnecter {

    public Connection con;
    //
    //ResultSet résultats = null;
    //

    public SeConnecter(){
        try{
            //String dbUrl = System.getenv("JDBC_DATABASE_URL");
            //con = DriverManager.getConnection(dbUrl);
            //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "joellungu");
            //con = DriverManager.getConnection("jdbc:postgres://ec2-44-209-57-4.compute-1.amazonaws.com:5432/ddivp7ga07bltc",
                    //"jdbc:postgresql://ec2-3-219-19-205.compute-1.amazonaws.com:5432/d16ufjtdkl3718",
            //"elywutaxvrrkea",
            //"4308f845fd0dd585b8a6ca2ac78b0d77e8da3d3918f2e7a86c8cd86d73e979e2");
            /*4308f845fd0dd585b8a6ca2ac78b0d77e8da3d3918f2e7a86c8cd86d73e979e2
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "joellungu");
            ///////////////////////////////////////////////////////////////////////////////////////////////////
            * con = DriverManager.getConnection("jdbc:postgresql://ec2-3-219-19-205.compute-1.amazonaws.com:5432/d16ufjtdkl3718",
            "hdjdavgerbrhwh",
            "f8f66e5c2f380b7dadb2943959687e6b6c5baf2f44eec7f028e6a5e3acb0ecf3");*/
            //Statement stmt = con.createStatement();
            //
            //stmt.executeUpdate(sql);
            //
        }catch(Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }
    }
}
