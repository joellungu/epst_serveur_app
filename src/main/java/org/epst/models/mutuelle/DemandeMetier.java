package org.epst.models.mutuelle;

import org.epst.models.SeConnecter;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class DemandeMetier {
    Jdbi jdbi = SeConnecter.jdbi;
    public void saveDemande(Demande demande){
        demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            v.insertDemande(demande);
            //
        }
    }
    public List<Demande> getAll(){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            try{
                v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }
            return v.listeDeDemande();
            //
        }
    }

    public List<Demande> getOne(Long id){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            v.miseAjour();
            return v.listeDeDemande(id);
            //
        }
    }
    public byte[] getCarte(Long id){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getCarte(id);
            //
        }
    }

    public void setStatus(int status, Long id) {
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            //v.miseAjour();
            v.setStatus(status,id);
            //
        }
    }

    public int getStatus(String matricule) {
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getStatus(matricule);
            //getStatus
        }
    }

    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }

}
