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
            con = DriverManager.getConnection("jdbc:postgresql://ec2-3-219-19-205.compute-1.amazonaws.com:5432/d16ufjtdkl3718",
            "hdjdavgerbrhwh",
            "f8f66e5c2f380b7dadb2943959687e6b6c5baf2f44eec7f028e6a5e3acb0ecf3");
            /*
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
