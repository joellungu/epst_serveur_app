package org.epst.models.document_scolaire.documents;

import org.epst.models.SeConnecter;
import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.document_scolaire.identification.DemandeIdentificationDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import jakarta.enterprise.context.ApplicationScoped;
import java.lang.annotation.Documented;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class DocumentMetier {
    Jdbi jdbi = SeConnecter.jdbi;
    public void saveDemande(Document document){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DocumentDao v = handle.attach(DocumentDao.class);
            //
            try{
                v.createTable();
            }catch (Exception ex){

            }
            v.insertDemande(document);
            //
        }
    }
    public List<Document> getAll(String province, String district, int valider){
        System.out.println("la valeur: "+province+" :-----: "+district);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DocumentDao v = handle.attach(DocumentDao.class);
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
    public List<Document> getAllByMatricule(String matricule){
        System.out.println("la valeur: "+matricule+" :-----: "+matricule);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DocumentDao v = handle.attach(DocumentDao.class);
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
    public List<Document> getOne(Long id){
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DocumentDao v = handle.attach(DocumentDao.class);
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
            DocumentDao v = handle.attach(DocumentDao.class);
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
            DocumentDao v = handle.attach(DocumentDao.class);
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
            DocumentDao v = handle.attach(DocumentDao.class);
            //
            v.createTable();
            //v.miseAjour();
            v.setExpirer(status,cenome,id);
            //
        }
    }
    public Document getStatus(Long id) {
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DocumentDao v = handle.attach(DocumentDao.class);
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
