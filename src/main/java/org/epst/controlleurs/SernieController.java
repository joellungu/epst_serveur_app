package org.epst.controlleurs;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.epst.models.Agent.Agent;
import org.epst.models.document_scolaire.identification.DemandeIdentification;
import org.epst.models.sernie.EcoleSernie;
import org.epst.models.sernie.Sernie;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Path("sernie")
public class SernieController {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveDemande(Sernie d) throws IOException {
        //System.out.println("La demande: "+d.getAdresse());
        //
        d.persist();
        /*
        DemandeIdentification d = new DemandeIdentification();
        d.setId(Long.parseLong(""+hashMap.get("id")));
        d.setNom(""+hashMap.get("Nom"));
        d.setPostnom(""+hashMap.get("Postnom"));
        d.setPrenom(""+hashMap.get("Prenom"));
        d.setSexe((char)hashMap.get("Sexe"));
        d.setLieuNaissance(""+hashMap.get("notes"));
        d.setDateNaissance(""+hashMap.get("postnom"));
        d.setTelephone(""+hashMap.get("prenom"));
        d.setNommere(""+hashMap.get("Nommere"));
        d.setNompere(""+hashMap.get("Nompere"));
        d.setAdresse(""+hashMap.get("Adresse"));
        d.setProvinceOrigine(""+hashMap.get("ProvinceOrigine"));
        d.setExt1("Ext1");
        d.setEcole(LocalDate.now().toString());
        d.setProvinceEcole(""+hashMap.get("ProvinceEcole"));
        d.setProvinceEducationnel((int)hashMap.get("provinceEducationnel"));
        d.setOption(""+hashMap.get("Option"));
        d.setTypeIdentification(""+hashMap.get("TypeIdentification"));
        //
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DataOutputStream out1 = new DataOutputStream(baos1);

        for (Object element : (ArrayList) hashMap.get("photo")) {
            System.out.print(element);
            out1.writeByte((int) element);
        }
        byte[] bytes = baos1.toByteArray();
        //
        d.setPhoto(bytes);
        //
        //---------------------------------
        //
        //ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        //DataOutputStream out2 = new DataOutputStream(baos2);

        //for (Object element : (ArrayList) hashMap.get("piecejointe")) {
        //    System.out.print(element);
        //    out2.writeByte((int) element);
        //}
        //byte[] bytes2 = baos2.toByteArray();
        //
        //d.setPiecejointe(bytes2);
        //---------------------------------
        */
        //demandeIdentificationMetier.saveDemande(d);
        //demandeMetier.saveDemande(demande);
        //
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateDemande(Sernie sernie) throws IOException {
        //System.out.println("La demande: "+d.getAdresse());
        Sernie sernie1 = Sernie.findById(sernie.id);
        if(sernie1 == null){
            return Response.serverError().build();
        }

        //
        sernie1.nom = sernie.nom;
        sernie1.postnom = sernie.postnom;
        sernie1.prenom = sernie.prenom;
        sernie1.sexe = sernie.sexe;
        sernie1.nompere = sernie.nompere;
        sernie1.nommere = sernie.nommere;
        sernie1.telephone = sernie.telephone;
        sernie1.adresse = sernie.adresse;
        sernie1.territoire = sernie.territoire;
        sernie1.secteur = sernie.secteur;
        sernie1.groupement = sernie.groupement;
        sernie1.village = sernie.village;
        sernie1.nationalite = sernie.nationalite;
        sernie1.code_antenne = sernie.code_antenne;
        sernie1.antenne = sernie.antenne;
        sernie1.annee = sernie.annee;
        sernie1.provinceOrigine = sernie.provinceOrigine;
        sernie1.lieuNaissance = sernie.lieuNaissance;
        sernie1.dateNaissance = sernie.dateNaissance;
        sernie1.ecole = sernie.ecole;
        sernie1.code_ecole = sernie.code_ecole;
        sernie1.reseaux = sernie.reseaux;
        sernie1.provinceEcole = sernie.provinceEcole;
        sernie1.provinceEducationnel = sernie.provinceEducationnel;
        sernie1.option = sernie.option;
        sernie1.niveau = sernie.niveau;
        sernie1.annee = sernie.annee;
        sernie1.classe = sernie.classe;
        sernie1.datedemande = sernie.datedemande;
        //sernie1.photo = sernie.photo;
        //sernie1.ext = sernie.ext;
        //sernie1.raison = sernie.raison;
        sernie1.valider = sernie.valider;

        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

    @Path("all/demande")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<DemandeIdentification> getAll(@QueryParam("province") String province, @QueryParam("district") String district,
                                              @QueryParam("valider") int valider,
                                              @QueryParam("role") int role){
        /*
        "Inspecteur exetat",8
    "Inspecteur tenassop",9
    "Inspecteur tenafepe",10
    "Inspecteur examen professionnel",12
    "Agent sernie",11
         */
        int code = role == 8 ? 0 : role == 9 ? 1 : role == 10 ? 2 : role == 11 ? 3 : 4;
        //
        HashMap params = new HashMap();
        params.put("provinceEcole",province);//Transfere
        params.put("provinceEducationnel",district);//Transfere
        params.put("valider",valider);//Transfere
        params.put("typeIdentificationcode",code);
        //
        return DemandeIdentification.list("provinceEcole =:provinceEcole and provinceEducationnel =:provinceEducationnel and valider =:valider and typeIdentificationcode =: typeIdentificationcode",params);
        //return demandeIdentificationMetier.getAll(province, district, valider, code);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("all")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAlls(){
        //
        var liste = DemandeIdentification.listAll();
        DemandeIdentification demandeIdentification = (DemandeIdentification) liste.get(liste.size() - 1);
        return Response.ok(demandeIdentification).build();
        //return demandeIdentificationMetier.getAll(province, district, valider, code);
        //return Response.status(Response.Status.CREATED).entity().build();
    }


    @Path("all/demandebymatricule")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public List<DemandeIdentification> getAll(@QueryParam("matricule") String matricule){
        //
        return DemandeIdentification.list("matricule",matricule);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("synchroniser")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getEcoleSernieSynchronise(){
        //C:\Users\Pierre\Documents\EPST-APP\REPERTOIRE DES ECOLES SELON SERNIE1.csv
        //
        EcoleSernie.deleteAll();
        //
        String line = "";
        String splitBy = ",";
        try
        {
            List<String[]> listeEcoles = readAllLines(java.nio.file.Path.of("REPERTOIRE DES ECOLES SELON SERNIE1.csv"));
            listeEcoles.forEach((e)->{
                try{
                    String[] ecole = e[0].split(";");
                    System.out.println(ecole[0]+" : "+ecole[1]);
                    EcoleSernie ecoleSernie = new EcoleSernie();
                    ecoleSernie.code_ecole = ecole[1];
                    ecoleSernie.denomination_ecole = ecole[0];
                    ecoleSernie.persist();
                }catch (Exception ex){
                    System.out.println(ex+" err ");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Response.ok(EcoleSernie.listAll()).build();
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("allecolesernie")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getAllEcoleSernie(){
        //C:\Users\Pierre\Documents\EPST-APP\REPERTOIRE DES ECOLES SELON SERNIE1.csv
        //

        return Response.ok(EcoleSernie.listAll()).build();
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@Transactional
    public DemandeIdentification getDemandeId(@PathParam("id") Long id){
        //
        System.out.println("Le id vaut: "+id);
        //
        return DemandeIdentification.findById(id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("piecejointe/{id}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getPiecejointe(@PathParam("id") Long id){
        //
        Sernie sernie = Sernie.findById(id);
        System.out.println("le nom: "+sernie.nom);
        System.out.println("le photo: "+sernie.photo.length);
        return sernie.photo;
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("update/{id}/{status}")
    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public void setStatus(@PathParam("id") Long id,@PathParam("status") int status, String raison){
        //
        DemandeIdentification demandeIdentification = DemandeIdentification.findById(id);
        if(demandeIdentification == null){
            return;
        }
        demandeIdentification.valider = status;
        demandeIdentification.raison = raison;
        //demandeIdentificationMetier.setStatus(status,id);
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    @Path("getallby/{province}/{provinceeducationnel}/{antenne}")
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    //@Transactional
    public Response getAllBy(@PathParam("province") String province,
                             @PathParam("provinceeducationnel") String provinceeducationnel,
                             @PathParam("antenne") String antenne){
        //
        HashMap params = new HashMap<>();
        params.put("provinceEducationnel",provinceeducationnel);
        params.put("provinceEcole",province);
        params.put("antenne",antenne);
        params.put("valider",0);

        List<Sernie> listeF = new LinkedList<>();
        List<Sernie> liste = Sernie.list("provinceEducationnel =: provinceEducationnel and provinceEcole =: provinceEcole and valider =: valider and antenne =: antenne",params);

        for(Sernie sernie : liste){
            //
            sernie.photo = new byte[0];
            //
            listeF.add(sernie);
        }

        /*
        liste.forEach((e)->{
            e.photo = new byte[0];
            listeF.add(e);
        });
        */

        return Response.ok(listeF).build();
    }

    @Path("statusdem")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DemandeIdentification getStatus(@QueryParam("id") Long id){
        //
        return DemandeIdentification.findById(id);
        //getAll
        //return Response.status(Response.Status.CREATED).entity().build();
    }

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            //.connectTimeout(Duration.ofSeconds(15))
            .build();

    @Path("testsend")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void getPhoto(){
        //
        Sernie sernie = Sernie.findById(35622L);

        //
        ObjectMapper obj = new ObjectMapper();
        //
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sernie.minepst.gouv.cd/create.php"))
                    .POST(HttpRequest.BodyPublishers.ofString(obj.writeValueAsString(sernie)))
                    .build();
            //
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print response headers
            HttpHeaders headers = response.headers();
            System.out.println("-- :: -- " + response.body());
            headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

            // print status code
            System.out.println(response.statusCode());

            // print response body
            System.out.println(response.body());
            //
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //
    }
    /**/

    public List<String[]> readAllLines(java.nio.file.Path filePath) throws Exception {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return csvReader.readAll();
            }
        }
    }

}
