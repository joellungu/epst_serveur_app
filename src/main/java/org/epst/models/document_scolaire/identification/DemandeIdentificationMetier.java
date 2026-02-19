package org.epst.models.document_scolaire.identification;

import org.epst.models.SeConnecter;
import org.epst.models.mutuelle.Demande;
import org.epst.models.mutuelle.DemandeDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class DemandeIdentificationMetier {
    Jdbi jdbi = SeConnecter.jdbi;
    public void saveDemande(DemandeIdentification demandeIdentification){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
            //
            try{
                v.createTable();
            }catch (Exception ex){

            }
            v.insertDemande(demandeIdentification);
            //
        }
    }
    public List<DemandeIdentification> getAll(String province, String district, int valider, int code){
        System.out.println("la valeur: "+province+" :-----: "+district);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
            //
            v.createTable();
            try{
                v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//listeDeDemandeByMatricule
            return v.listeDeDemande(valider, province, district, code);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }
    public List<DemandeIdentification> getAllByMatricule(String matricule){
        System.out.println("la valeur: "+matricule+" :-----: "+matricule);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
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
    public List<DemandeIdentification> getOne(Long id){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
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
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getPiecejointe(id);
            //
        }
    }
    public void setStatus(int status, Long id) {//setExpirer
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
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
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
            //
            v.createTable();
            //v.miseAjour();
            v.setExpirer(status,cenome,id);
            //
        }
    }
    public DemandeIdentification getStatus(Long id) {
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DemandeIdentificationDao v = handle.attach(DemandeIdentificationDao.class);
            //
            v.createTable();
            //v.miseAjour();
            return v.getStatus(id);
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
