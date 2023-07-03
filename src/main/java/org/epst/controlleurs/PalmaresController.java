package org.epst.controlleurs;

import com.opencsv.CSVReader;
import org.epst.models.Agent.Agent;
import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresMetier;
import org.epst.models.palmares.Resultat;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Path("/actions")
public class PalmaresController {

    @Inject
    PalmaresMetier palmaresMetier;

    //@Path("/{codeecole}/{codeoption}/{anneescolaire}")
    //@Path("/{codeecole}/{codeoption}/{anneescolaire}")
    @POST()
    @Produces(MediaType.APPLICATION_JSON)
    public List<org.epst.models.palmares.Palmares> getPlainte(HashMap e) {
        System.out.println(e.get("codeecole"));
        System.out.println(e.get("codeoption"));
        System.out.println(e.get("anneescolaire"));
        String c0 = e.get("nomprovince").toString();
        String c1 = e.get("nomecole").toString();
        String c2 = e.get("codeoption").toString();
        String c3 = e.get("anneescolaire").toString();
        System.out.println(c1);
        HashMap<String, Object> params = new HashMap<>();
        params.put("nomprovince",c0);
        params.put("nomecole",c1);
        params.put("codeoption",c2);
        params.put("anneescolaire",c3);

        return Palmares.list("nomprovince =:nomprovince and nomecole =:nomecole and codeoption =:codeoption and anneescolaire =:anneescolaire ",params);
        /*
        @PathParam("codeecole") String codeecole,
        @PathParam("anneescolaire") String anneescolaire
         */
        //return Agent.list("matricule =:matricule and mdp =:mdp ",params);
        //palmaresMetier.getAll(c0,c1,c2,c3);
    }

    @GET()
    @Path("resultat/{anneescolaire}/{codecandidat}")
    public Palmares getPalmares(@PathParam("anneescolaire") String anneescolaire, @PathParam("codecandidat") String codecandidat){
        //
        return palmaresMetier.getPalmare(anneescolaire,codecandidat);
        //
    }

    @GET()
    @Path("all")
    //@Consumes(MediaType.WILDCARD)
    //@Produces(MediaType.APPLICATION_JSON)
    public List<Palmares> getAllPalmares(){

        //
        List<Palmares> palmares = Palmares.listAll();
        return palmares;
    }

    @GET()
    @Path("save")
    @Transactional
    //@Consumes(MediaType.WILDCARD)
    //@Produces(MediaType.APPLICATION_JSON)
    public String savePalmares(){

        //
        File fichier = new File("C:\\Users\\Pierre\\Documents\\palmares_provinces_educationnelle\\PalmOPGLOBAL2022 KINSHASA FUNA.csv");
        try {
            Resultat resultat = new Resultat();
            byte[] fileContent = Files.readAllBytes(fichier.toPath());
            resultat.setFichier(fileContent);
            resultat.setAnnee(2022);
            resultat.setProvinceEducationnel("KINSHASA FUNA");
            resultat.persist();
            return "Tres cool";

        } catch (IOException e) {
            System.out.println(e);
            return "Pas cool";
        }

        /*

        try (CSVReader reader = new CSVReader(new FileReader("src/Resultat_Global_2013.csv"))) {
            List<String[]> r = reader.readAll();
            for (int t = 1; t < r.size(); t++){

                    String[] x = r.get(t);

                    String arrays = Arrays.toString(x);
                    Palmares p = new Palmares();
                    p.setSexe(x[18]);
                    //p.setNomecole(x[18]);
                    p.setOption(x[15]);
                    p.setPourcentage(Integer.parseInt(x[14].replace("%", "0")));
                    p.setNomcandidat(x[13]);
                    p.setCodecandidat(x[12]);
                    p.setCodeecole(x[8]);
                    p.setNomecole(x[7]);
                    p.setAnneescolaire("2013");
                    p.setOption(x[5]);
                    p.setNomcentre(x[3]);
                    p.setCodecentre(x[4]);
                    p.setCodegestion(Integer.parseInt(x[10]));
                    p.setOrdreecole(Integer.parseInt(x[9]));
                    //
                    p.persistAndFlush();
                    //
                ///
                for(int i = 0; i < x.length; i++){

                    System.out.println(i+" : "+x[i]);
                }
                //
                    System.out.println("-------------------------");

            };
            return "Très cool";
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex);
            return "Pas très cool du tout";
        }
        */
        //
        //return "Très cool";
    }

    @GET()
    @Path("test")
    @Transactional
    //@Consumes(MediaType.WILDCARD)
    //@Produces(MediaType.APPLICATION_JSON)
    public String testPalmares(){

        //
        Resultat resultat = Resultat.findAll().firstResult();
        //
        Reader targetReader = new StringReader(new String(resultat.getFichier()));

        try (CSVReader reader = new CSVReader(targetReader)) {
            List<String[]> r = reader.readAll();
            String reponse = "";
            for (int t = 1; t < r.size(); t++){
                String[] x = r.get(t);
                /*
                String arrays = Arrays.toString(x);
                Palmares p = new Palmares();
                p.setSexe(x[18]);
                //p.setNomecole(x[18]);
                p.setOption(x[15]);
                p.setPourcentage(Integer.parseInt(x[14].replace("%", "0")));
                p.setNomcandidat(x[13]);
                p.setCodecandidat(x[12]);
                p.setCodeecole(x[8]);
                p.setNomecole(x[7]);
                p.setAnneescolaire("2013");
                p.setOption(x[5]);
                p.setNomcentre(x[3]);
                p.setCodecentre(x[4]);
                p.setCodegestion(Integer.parseInt(x[10]));
                p.setOrdreecole(Integer.parseInt(x[9]));
                //
                p.persistAndFlush();
                */
                //
                //
                for(int i = 0; i < x.length; i++){
                    System.out.println(i+" : "+x[i]);
                }
                //
                System.out.println("-------------------------");
            };
            return "Très cool";
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex);
            return "Pas très cool du tout";
        }

        //
        //return "Très cool";
    }

}
