package org.epst.models.palmares;

import org.epst.models.mutuelle.Demande;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PalmaresDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS PALMARES  (" +
            "nomprovince varchar," +
            "codeprovince varchar," +
            "nomcentre varchar," +
            "codecentre INTEGER," +
            "option varchar," +
            "codeoption varchar," +
            "nomecole varchar," +
            "codeecole varchar," +
            "ordreecole INTEGER," +
            "codegestion INTEGER," +
            "codecandidat varchar," +
            "nomcandidat varchar," +
            "sexe varchar," +
            "note varchar," +
            "anneescolaire varchar" +
            ")")
    void createTable();

    @SqlUpdate("INSERT INTO PALMARES (nomprovince,codeprovince,nomcentre,codecentre,option,codeoption,nomecole,codeecole,ordreecole,codegestion,codecandidat,nomcandidat,sexe,note,anneescolaire) values " +
            "(:nomprovince,:codeprovince,:nomcentre,:codecentre,:option,:codeoption,:nomecole,:codeecole,:ordreecole,:codegestion,:codecandidat,:nomcandidat,:sexe,:note,:anneescolaire)")
    void insertPalmare(@BindBean Palmares palmares);

    @SqlQuery("SELECT * FROM PALMARES where nomprovince = ? AND nomecole = ? AND codeoption = ? AND anneescolaire = ?")
    @RegisterBeanMapper(Palmares.class)//
    List<Palmares> listeAll(String nomprovince, String nomecole, String codeoption, String anneescolaire);//

    @SqlQuery("SELECT * FROM Palmopglobal2022 where ANNEE_SCOLAIRE = ? AND Code_Candidat = ?")
    @RegisterBeanMapper(Palmares.class)//
    Palmares getPalmare(String ANNEE_SCOLAIRE, String Code_Candidat);//

}
