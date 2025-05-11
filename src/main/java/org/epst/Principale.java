package org.epst;

import org.epst.models.Agent.AgentDao;
import org.epst.models.SeConnecter;
import org.epst.models.autres.Autres;
import org.epst.models.document_scolaire.identification.DemandeIdentificationDao;
import org.epst.models.mutuelle.DemandeDao;
import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresDao;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Path("/educ-app")
public class Principale {

    // Stocker les CompletableFuture par un identifiant (par exemple, un ID de session ou de requête)
    private final Map<String, CompletableFuture<String>> waitingRequests = new ConcurrentHashMap<>();

    Jdbi jdbi = SeConnecter.jdbi;

    @GET
    @Path("/politique")
    @Produces(MediaType.TEXT_HTML)
    public String get() {
        String result = "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Politique de Confidentialité - EPST-APP / Educ-APP</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      line-height: 1.6;\n" +
                "      margin: 20px;\n" +
                "      color: #333;\n" +
                "    }\n" +
                "    h1, h2, h3 {\n" +
                "      color: #222;\n" +
                "    }\n" +
                "    ul {\n" +
                "      margin-bottom: 15px;\n" +
                "    }\n" +
                "    p {\n" +
                "      margin-bottom: 15px;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <h1>Politique de Confidentialité pour EPST-APP / Educ-APP</h1>\n" +
                "  <p><strong>Date d’entrée en vigueur :</strong> 10-02-2025</p>\n" +
                "  <p>Bienvenue sur <strong>EPST-APP / Educ-APP</strong>, une application développée pour offrir des services éducatifs et administratifs en République Démocratique du Congo (RDC). Cette politique de confidentialité a pour objectif de vous informer de manière claire et transparente sur la manière dont nous collectons, utilisons, protégeons et partageons vos données personnelles lors de votre utilisation de l’application.</p>\n" +
                "\n" +
                "  <h2>1. Introduction</h2>\n" +
                "  <p>En utilisant EPST-APP / Educ-APP, vous acceptez que vos informations soient traitées conformément à cette politique de confidentialité. Nous nous engageons à respecter votre vie privée en appliquant à la fois la législation locale (notamment la Loi n° 007/2002 en RDC) et, le cas échéant, les normes internationales telles que le GDPR et le CCPA.</p>\n" +
                "\n" +
                "  <h2>2. Données Collectées</h2>\n" +
                "  <p>Nous collectons différentes catégories de données en fonction des fonctionnalités proposées par l’application.</p>\n" +
                "  \n" +
                "  <h3>2.1. Fonctionnalités de Base</h3>\n" +
                "  <ul>\n" +
                "    <li>\n" +
                "      <strong>Inscription et gestion de compte :</strong>\n" +
                "      <ul>\n" +
                "        <li>Informations telles que le nom, le numéro de téléphone, l’adresse e-mail et toute autre donnée nécessaire pour l’inscription et l’utilisation de nos services.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>MGP – Dépôt de plainte :</strong>\n" +
                "      <ul>\n" +
                "        <li>Documents partagés et fichiers téléchargés dans la section « MGP Dépôt plainte ».</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>École en ligne :</strong>\n" +
                "      <ul>\n" +
                "        <li>Accès à la librairie pour récupérer des fichiers (Word, Excel, etc.) correspondant aux devoirs, interrogations ou autres tâches exigées par le corps enseignant, afin d’assurer la continuité scolaire, notamment pour les enfants déplacés des zones de conflit dans l’est de la RDC.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Informations d’appareil :</strong>\n" +
                "      <ul>\n" +
                "        <li>Données relatives au type de dispositif utilisé, au système d’exploitation et aux journaux d’activité, afin de garantir le bon fonctionnement de l’application.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <h3>2.2. Fonctionnalités Secondaires</h3>\n" +
                "  <ul>\n" +
                "    <li>\n" +
                "      <strong>Chat avec les agents :</strong>\n" +
                "      <ul>\n" +
                "        <li>Permet la communication sécurisée entre les usagers et les agents habilités du ministère.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Traitement des demandes :</strong>\n" +
                "      <ul>\n" +
                "        <li>Gestion et réponse aux demandes de documents scolaires dans les délais réglementaires.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Campagnes de communication :</strong>\n" +
                "      <ul>\n" +
                "        <li>Envoi de SMS ciblés et réalisation d’enquêtes de satisfaction.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Diffusion de contenus :</strong>\n" +
                "      <ul>\n" +
                "        <li>Publication d’actualités et de contenus éducatifs via les rubriques « Actualités » et « MAG de l’EPST ».</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <h2>3. Utilisation des Données</h2>\n" +
                "  <p>Les informations collectées sont utilisées dans le but de :</p>\n" +
                "  <ul>\n" +
                "    <li>\n" +
                "      <strong>Fournir et améliorer nos services :</strong>\n" +
                "      <ul>\n" +
                "        <li>Gérer votre inscription, permettre l’accès aux différentes fonctionnalités et améliorer l’expérience utilisateur.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Assurer la communication :</strong>\n" +
                "      <ul>\n" +
                "        <li>Faciliter les échanges via le chat, informer sur les mises à jour ou envoyer des notifications concernant des campagnes ciblées et des enquêtes de satisfaction.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Garantir la sécurité et le traitement approprié :</strong>\n" +
                "      <ul>\n" +
                "        <li>Transmettre certaines informations aux agents habilités du ministère ou aux partenaires de service pour le traitement des demandes, des plaintes et la gestion des formulaires.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Répondre aux obligations légales :</strong>\n" +
                "      <ul>\n" +
                "        <li>Conserver et partager certaines données lorsque la loi l’exige (par exemple, dans le cadre de plaintes sensibles telles que celles liées aux violences basées sur le genre ou à la corruption).</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "  </ul>\n" +
                "\n" +
                "  <h2>4. Partage des Données</h2>\n" +
                "  <p>Nous nous engageons à ne partager vos informations personnelles qu’avec :</p>\n" +
                "  <ul>\n" +
                "    <li>Les agents habilités du ministère, afin de traiter vos demandes, plaintes et enquêtes.</li>\n" +
                "    <li>Nos partenaires de service, qui interviennent pour l’envoi des SMS et la gestion des formulaires.</li>\n" +
                "    <li>Les autorités compétentes, dans le cadre de plaintes sensibles ou lorsque la loi l’exige.</li>\n" +
                "  </ul>\n" +
                "  <p>Nous ne vendons ni ne louons vos données à des tiers.</p>\n" +
                "\n" +
                "  <h2>5. Sécurité des Données</h2>\n" +
                "  <p>La protection de vos données personnelles est une priorité. À ce titre, nous avons mis en place des mesures techniques et organisationnelles adaptées, notamment :</p>\n" +
                "  <ul>\n" +
                "    <li>\n" +
                "      <strong>Archivage sécurisé des échanges :</strong>\n" +
                "      <ul>\n" +
                "        <li>Les messages échangés via le chat sont automatiquement archivés et ne peuvent être ni transférés ni supprimés par les utilisateurs.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "      <strong>Traitement des plaintes sensibles :</strong>\n" +
                "      <ul>\n" +
                "        <li>Les documents et informations relatifs aux plaintes sensibles sont directement acheminés vers des adresses électroniques sécurisées, accessibles uniquement aux responsables habilités.</li>\n" +
                "      </ul>\n" +
                "    </li>\n" +
                "  </ul>\n" +
                "  <p>Bien que nous nous efforcions de protéger vos données, aucune méthode de transmission ou de stockage électronique n’est infaillible. Nous ne pouvons donc garantir une sécurité absolue.</p>\n" +
                "\n" +
                "  <h2>6. Vos Droits</h2>\n" +
                "  <p>Conformément à la réglementation en vigueur, vous disposez des droits suivants concernant vos données personnelles :</p>\n" +
                "  <ul>\n" +
                "    <li><strong>Droit d’accès :</strong> Vous pouvez demander à consulter les informations que nous détenons à votre sujet.</li>\n" +
                "    <li><strong>Droit de rectification :</strong> Vous avez la possibilité de corriger ou de mettre à jour vos données personnelles.</li>\n" +
                "    <li><strong>Droit à l’effacement :</strong> Vous pouvez demander la suppression de vos données, sous réserve des obligations légales de conservation.</li>\n" +
                "  </ul>\n" +
                "  <p>Pour exercer ces droits ou pour toute question relative à vos données, veuillez nous contacter à l’adresse suivante : <strong>contact@epst-app.cd</strong></p>\n" +
                "\n" +
                "  <h2>7. Conservation des Données</h2>\n" +
                "  <p>Nous conservons vos informations personnelles uniquement pendant la durée nécessaire à la réalisation des finalités décrites dans cette politique ou pour satisfaire aux obligations légales applicables. Une fois ces objectifs atteints, vos données seront supprimées ou anonymisées.</p>\n" +
                "\n" +
                "  <h2>8. Modifications de cette Politique</h2>\n" +
                "  <p>Nous nous réservons le droit de mettre à jour cette politique de confidentialité. En cas de modification substantielle, nous publierons une version mise à jour sur cette page et, le cas échéant, nous vous en informerons via l’application. Nous vous recommandons de consulter régulièrement cette politique afin de rester informé(e) des éventuelles modifications.</p>\n" +
                "\n" +
                "  <h2>9. Contact</h2>\n" +
                "  <p>Pour toute question ou préoccupation concernant cette politique de confidentialité, veuillez nous contacter aux coordonnées suivantes :</p>\n" +
                "  <ul>\n" +
                "    <li><strong>Email :</strong> sgc@eduquepsp.education</li>\n" +
                "    <li><strong>Téléphone :</strong> (+243) 840018006</li>\n" +
                "    <li><strong>Adresse :</strong> 02, Avenue des Ambassadeurs</li>\n" +
                "  </ul>\n" +
                "  <p>Nous vous remercions de votre confiance et restons à votre disposition pour toute information complémentaire.</p>\n" +
                "</body>\n" +
                "</html>";
        return result;
    }

    @GET
    @Path("go")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        initDatabase();

        return "Hello from RESTEasy Reactive";
    }


    public void initDatabase(){
        //demande.setId(getId());
        jdbi.installPlugin(new SqlObjectPlugin());
        try(Handle handle = jdbi.open()){
            PalmaresDao v1 = handle.attach(PalmaresDao.class);
            Autres v2 = handle.attach(Autres.class);
            PalmaresDao v3 = handle.attach(PalmaresDao.class);
            DemandeDao v4 = handle.attach(DemandeDao.class);
            DemandeIdentificationDao v5 = handle.attach(DemandeIdentificationDao.class);
            AgentDao v6 = handle.attach(AgentDao.class);

            //
            try{
                v1.createTable();
                v3.createTable();
                v4.createTable();
                v5.createTable();
                v6.createTable();
                //
                v2.createTableCours();
                v2.createTableArchive();
                v2.createTableArchivechat();
                v2.createTableMagasin();
                v2.createTableDepotPlainte();
                v2.createTableMessagebean();
                v2.createTableNoteTraitement();
                v2.createTablePiecejointe();
                //
            }catch (Exception ex){
                System.out.println("Erreur du: "+ex);
            }
            //
        }catch (Exception ex){
            System.out.println("Erreur du: "+ex);
        }
    }

    ////////////////////////////////////////////////////////////////
    @GET
    @Path("/start")
    public Response startRequest() throws InterruptedException {
        String requestId = "req-" + System.currentTimeMillis(); // Générer un ID unique pour la requête

        // Créer un CompletableFuture pour cette requête
        CompletableFuture<String> future = new CompletableFuture<>();
        waitingRequests.put(requestId, future);

        try {
            // Attendre que la future soit complétée par une autre requête
            String result = future.get(); // Bloque jusqu'à ce que la future soit complétée
            return Response.ok("First request completed after: " + result).build();
        } catch (ExecutionException e) {
            return Response.serverError().entity("Error: " + e.getMessage()).build();
        } finally {
            // Nettoyer la future après utilisation
            waitingRequests.remove(requestId);
        }
    }

    @GET
    @Path("/trigger")
    public Response triggerRequest() {
        if (waitingRequests.isEmpty()) {
            return Response.ok("No requests are waiting.").build();
        }

        // Compléter la première future en attente (ou une spécifique selon votre logique)
        Map.Entry<String, CompletableFuture<String>> entry = waitingRequests.entrySet().iterator().next();
        String requestId = entry.getKey();
        CompletableFuture<String> future = entry.getValue();

        // Compléter la future pour débloquer la requête en attente
        future.complete("Triggered by another request! " + requestId);

        return Response.ok("Triggered request with ID: " + requestId).build();
    }

}