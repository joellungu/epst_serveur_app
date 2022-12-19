package org.epst;

import org.epst.models.Agent.AgentDao;
import org.epst.models.SeConnecter;
import org.epst.models.autres.Autres;
import org.epst.models.document_scolaire.identification.DemandeIdentificationDao;
import org.epst.models.mutuelle.DemandeDao;
import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/epst")
public class Principale {

    Jdbi jdbi = SeConnecter.jdbi;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        initDatabase();

        return "Hello from RESTEasy Reactive";
    }
    public void initDatabase(){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            PalmaresDao v1 = handle.attach(PalmaresDao.class);
            Autres v2 = handle.attach(Autres.class);
            PalmaresDao v3 = handle.attach(PalmaresDao.class);
            DemandeDao v4 = handle.attach(DemandeDao.class);
            DemandeIdentificationDao v5 = handle.attach(DemandeIdentificationDao.class);
            AgentDao v6 = handle.attach(AgentDao.class);

            //
            try{
                v1.createTable();
                v3.createTable();
                v4.createTable();
                v5.createTable();
                v6.createTable();
                //
                v2.createTableCours();
                v2.createTableArchive();
                v2.createTableArchivechat();
                v2.createTableMagasin();
                v2.createTableDepotPlainte();
                v2.createTableMessagebean();
                v2.createTableNoteTraitement();
                v2.createTablePiecejointe();
                //
            }catch (Exception ex){
                System.out.println("Erreur du: "+ex);
            }
            //
        }catch (Exception ex){
            System.out.println("Erreur du: "+ex);
        }
    }

}