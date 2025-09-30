package org.epst.models.palmares;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Palms {
    public static Connection connection;

    static{
        //
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ec2-3-234-204-26.compute-1.amazonaws.com:5432/d91bqr4fs9ag4p",
                    "eoatiupvtvpqum",
                    "ea60f987ee3d2239cc01c4b44dd28e949362252af1315c2e51a24c4f34ee123f");
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //
        //sql2o = new Sql2o("jdbc:postgresql://ec2-3-234-204-26.compute-1.amazonaws.com:5432/d91bqr4fs9ag4p",
        //        "eoatiupvtvpqum",
        //        "ea60f987ee3d2239cc01c4b44dd28e949362252af1315c2e51a24c4f34ee123f");
    }
}
