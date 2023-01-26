package org.epst.models.devise;

import org.epst.models.Agent.Agent;
import org.epst.models.Agent.AgentDao;
import org.epst.models.SeConnecter;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.enterprise.context.ApplicationScoped;

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

    public double conversion(int montant, Long id, Boolean de) {
        try(Handle handle = jdbi.open()){
            DeviseDao v = handle.attach(DeviseDao.class);
            //
            try{
                Devise d = v.selectDevise(id);
                if(de){
                    return montant + 0.2;
                }else{
                    return (montant + 0.2) * d.montant;
                }
                //v.createTable();
            }catch (Exception ex){
                return montant * 10;
            }
        }
    }
}
