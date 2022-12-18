package org.epst.models.palmares;

import org.epst.models.SeConnecter;
import org.epst.models.mutuelle.Demande;
import org.epst.models.mutuelle.DemandeDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PalmaresMetier {

    Jdbi jdbi = SeConnecter.jdbi;
    public void saveDemande(Palmares palmares){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            PalmaresDao v = handle.attach(PalmaresDao.class);
            //
            try{
                v.createTable();
            }catch (Exception ex){

            }
            v.insertPalmare(palmares);
            //
        }
    }
    public List<Palmares> getAll(String ecole, String codeoption, String annee){
        System.out.println("la valeur: "+ecole+" :-----: "+codeoption+" :-----: "+annee);
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            PalmaresDao v = handle.attach(PalmaresDao.class);
            //

            try{
                v.createTable();
                //v.miseAjour();
            }catch (Exception ex){
                System.out.println("Erreur du à: "+ex);
            }//listeDeDemandeByMatricule
            return v.listeAll(ecole, codeoption, annee);//province, district
            //http://localhost:8080/mutuelle/all/demande?province=Kinshasa&district=KINSHASA-MONT%20AMBA
        }
    }

}
