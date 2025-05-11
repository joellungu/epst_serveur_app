package org.epst.models.mutuelle;

import org.epst.models.SeConnecter;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class DemandeMetier {
    Jdbi jdbi = SeConnecter.jdbi;
    public String saveDemande(Demande demande){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            try{
                v.createTable();
                v.insertDemande(demande);
                return "Validé";
            }catch (Exception ex){
                return ex.getMessage();
            }

            //
        }
    }
    public List<Demande> getAll(String province, String district){
        System.out.println("la valeur: "+province+" :-----: "+district);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //

            try{
                v.createTable();
                //v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//listeDeDemandeByMatricule
            return v.listeDeDemande(province, district);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }
    public List<Demande> getAllByMatricule(String matricule){
        System.out.println("la valeur: "+matricule+" :-----: "+matricule);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            //v.createTable();
            try{
                v.createTable();
                //v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//
            return v.listeDeDemandeByMatricule(matricule);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }
    public List<Demande> getOne(Long id){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            //v.createTable();
            try{
                v.createTable();
                //v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//
            return v.listeDeDemande(id);
            //
        }
    }
    public byte[] getPiecejointe(Long id){//
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getPiecejointe(id);
            //
        }
    }
    public byte[] getCarte(Long id){//getPiecejointe
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
    public void setStatus(int status, Long id) {//setExpirer
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
    public void setExpirer(int status, String cenome, Long id) {//
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            //v.miseAjour();
            v.setExpirer(status,cenome,id);
            //
        }
    }
    public int getStatus(Long id) {
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeDao v = handle.attach(DemandeDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getStatus(id);
            //getStatus
        }
    }
    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }

}
