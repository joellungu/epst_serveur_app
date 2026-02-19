package org.epst.models.devise;

import org.epst.models.Agent.Agent;
import org.epst.models.Agent.AgentDao;
import org.epst.models.SeConnecter;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviseMetier {

    Jdbi jdbi = SeConnecter.jdbi;

    public String saveAgent(Devise devise){
        //
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            DeviseDao v = handle.attach(DeviseDao.class);
            //
            try{
                v.createTable();
                v.insertDevise(devise);
                return "Enregistrement effecuté avec succès";
                //v.createTable();
            }catch (Exception ex){
                return "Enregistrement non effecuté : "+ex.getMessage();
            }
        }
    }

    public double conversion(Double montant, Long id, Boolean de) {
        try(Handle handle = jdbi.open()){
            DeviseDao v = handle.attach(DeviseDao.class);
            //
            try{
                Devise d = v.selectDevise(id);
                double prct = (2 * montant) / 100;
                if(de){
                    return montant + prct;
                }else{
                    return (montant + prct) * d.taux;
                }
                //v.createTable();
            }catch (Exception ex){
                return montant * 10;
            }
        }
    }
}
