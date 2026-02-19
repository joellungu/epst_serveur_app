package org.epst.models.document_scolaire.transfere;

import org.epst.models.SeConnecter;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class TransfereMetier {
    Jdbi jdbi = SeConnecter.jdbi;
    public void saveDemande(Transfere demandeIdentification){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            try{
                v.createTable();
            }catch (Exception ex){

            }
            v.insertDemande(demandeIdentification);
            //
        }
    }
    public List<Transfere> getAll(String province, String district, int valider){
        System.out.println("la valeur: "+province+" :-----: "+district);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            v.createTable();
            try{
                v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//listeDeDemandeByMatricule
            return v.listeDeDemande(valider, province, district);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }
    public List<Transfere> getAllByMatricule(String matricule){
        System.out.println("la valeur: "+matricule+" :-----: "+matricule);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            v.createTable();
            try{
                v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//
            return v.listeDeDemandeByMatricule(matricule);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }
    public List<Transfere> getOne(Long id){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            v.createTable();
            try{
                v.miseAjour();
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
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getPiecejointe(id);
            //
        }
    }
    public void setStatus(int status, Long id, String raison) {//setExpirer
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            v.createTable();
            //v.miseAjour();
            v.setStatus(status,raison,id);
            //
        }
    }
    public void setExpirer(int status, String cenome, Long id) {//
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            v.createTable();
            //v.miseAjour();
            v.setExpirer(status,cenome,id);
            //
        }
    }
    public Transfere getStatus(Long id) {
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            TransfereDao v = handle.attach(TransfereDao.class);
            //
            try{
                v.createTable();
                return v.getStatus(id);
            }catch (Exception e){
                System.out.println(e);
                HashMap t = new HashMap();
                //t.put("","");
                return null;
            }
            //v.miseAjour();

            //getStatus
        }
    }
    //
    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }

}
