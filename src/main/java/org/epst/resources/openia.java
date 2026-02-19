package org.epst.resources;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.ws.rs.Path;

//import static java.lang.StringTemplate.STR;

@Path("apiia")
public class openia {
    //
    public void testString() {
        String message = String.format("Bonjour %s, age %d", "Joel", 13);
        //
        //String msg = STR."Bonjour {nom}, âge {age}";
    }
}
