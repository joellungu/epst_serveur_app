package org.epst.models.paiement;

import org.epst.models.SeConnecter;
import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PaiementMetier {

    Jdbi jdbi = SeConnecter.jdbi;
    public void savePaiement(Paiement paiement){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            PaiementDao v = handle.attach(PaiementDao.class);
            //
            try{
                v.createTable();
            }catch (Exception ex){

            }
            v.insertPaiement(paiement);
            //
        }
    }
    public List<Paiement> getAll(String phone, String date){
        //System.out.println("la valeur: "+ecole+" :-----: "+codeoption+" :-----: "+annee);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            PaiementDao v = handle.attach(PaiementDao.class);
            //

            try{
                v.createTable();
                //v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//listeDeDemandeByMatricule
            return v.listeAll(phone, date);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }

}
