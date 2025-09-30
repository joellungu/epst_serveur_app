package org.epst.controlleurs;

import com.opencsv.CSVReader;
import org.epst.models.Agent.Agent;
import org.epst.models.palmares.*;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.*;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


@Path("/actions")
public class PalmaresController {

    @Inject
    PalmaresRepository palmaresRepository;

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

        //Palmares palmares = new Palmares();
        //palmares
        //palmaresRepository.persist();

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
    public Response getPalmares(@PathParam("anneescolaire") String anneescolaire, @PathParam("codecandidat") String codecandidat){
        //
        HashMap<String, Object> params = new HashMap<>();
        params.put("annescolaire",anneescolaire);
        params.put("cdcdt",codecandidat);
        //
        List<Palmares> palmares = Palmares.find("annescolaire =: annescolaire and cdcdt =: cdcdt",params).list();
        //
        if(!palmares.isEmpty()){
            return Response.ok(palmares.get(0)).build();
        } else {
            return Response.status(404).build();
        }
        //return Response.ok(palmaresMetier.getPalmare(anneescolaire,codecandidat)).build();
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

        //
        File fichier = new File("C:\\Users\\Pierre\\Documents\\EXETATS\\PalmOPGLOBAL2022.csv");
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

    /*
    @GET()
    @Path("test")
    @Transactional
    //@Consumes(MediaType.WILDCARD)
    //@Produces(MediaType.APPLICATION_JSON)
    public String testPalmares(){

        //
        HashMap<String, Object> params = new HashMap<>();
        params.put("annee",2022);
        params.put("provinceEducationnel","KINSHASA FUNA");
        //
        Resultat resultat = (Resultat) Resultat.find("annee", 2022).firstResult();
                //list("annee =: annee and provinceEducationnel =: provinceEducationnel ").get(0);
        //
        Reader targetReader = new StringReader(new String(resultat.getFichier()));

        try (CSVReader reader = new CSVReader(targetReader)) {
            List<String[]> r = reader.readAll();
            //
            //palmaresRepository.persist();
            String reponse = "";
            List<Palmares> iterable = new LinkedList<>();

            for (int t = 1; t < r.size(); t++){
                String[] x = r.get(t);
                //
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
                //p.persistAndFlush();
                //
                //
                iterable.add(p);
                //for(int i = 0; i < x.length; i++){
                //    System.out.println(i+" : "+x[i]);
                //}

                //
                System.out.println("-------------------------");
            };
            //
            //palmaresRepository.persist((Iterable<Palmares>) iterable.iterator());
            //
            return "Très cool";
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex);
            return "Pas très cool du tout";
        }

        //
        //return "Très cool";
    }

    */
    //
    public HashMap getTasksBetweenDates(String annee, String codeEleve){
        //
        Statement stmt = null;
        //
        String sql =
                "SELECT * FROM palmglobal " +
                        "WHERE ANNEE_SCOLAIRE = '"+annee+"' AND Code_Candidat = '"+codeEleve+"'";
        //
        HashMap h = new HashMap();

        try {//Connection con = Palms.sql2o.open()
            stmt = Palms.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                String s1 = rs.getString(1);
                String s2 = rs.getString(2);
                String s3 = rs.getString(3);
                String s4 = rs.getString(4);
                String s5 = rs.getString(5);
                String s6 = rs.getString(6);
                String s7 = rs.getString(7);
                String s8 = rs.getString(8);
                String s9 = rs.getString(9);
                String s10 = rs.getString(10);
                String s11 = rs.getString(11);
                String s12 = rs.getString(12);
                String s13 = rs.getString(13);
                String s14 = rs.getString(14);
                String s15 = rs.getString(15);
                //
                h.put("Nom_Province",s1);
                h.put("Code_Province",s2);
                h.put("Nom_Centre",s3);
                h.put("Code_centre",s4);
                h.put("Option",s5);
                h.put("Code_Option",s6);
                h.put("Nom_ecole",s7);
                h.put("Code_Ecole",s8);
                h.put("Ordre_Ecole",s9);
                h.put("Code_Gestion",s10);
                h.put("Code_Candidat",s11);
                h.put("Nom_Candidat",s12);
                h.put("Sexe",s13);
                h.put("Note",s14);
                h.put("ANNEE_SCOLAIRE",s15);
                //
                System.out.println( "s1 = " + s1 );
                System.out.println( "s2 = " + s2 );
                System.out.println( "s3 = " + s3 );
                System.out.println( "s4 = " + s4 );
                System.out.println( "s5 = " + s5 );
                System.out.println( "s6 = " + s6 );
                System.out.println( "s7 = " + s7 );
                System.out.println( "s8 = " + s8 );
                System.out.println( "s9 = " + s9 );
                System.out.println( "s10 = " + s10 );
                System.out.println( "s11 = " + s11 );
                System.out.println( "s12 = " + s12 );
                System.out.println( "s13 = " + s13 );
                System.out.println( "s14 = " + s14 );
                System.out.println( "s15 = " + s15 );
                System.out.println();
            }
            rs.close();
            stmt.close();
            Palms.connection.close();
            /*
            return con.createQuery(sql)
                    //.addParameter("ANNEE_SCOLAIRE", annee)
                    //.addParameter("Code_Candidat", codeEleve)
                    .executeAndFetch(Palmopglobal2022.class);
            */
        }catch (Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }

        return h;
    }

}
