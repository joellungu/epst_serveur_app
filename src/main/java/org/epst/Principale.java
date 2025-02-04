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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/educ-app")
public class Principale {

    Jdbi jdbi = SeConnecter.jdbi;

    @GET
    @Path("/politique")
    @Produces(MediaType.TEXT_HTML)
    public String get() {
        String result = "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Politique de Confidentialité - Educ-APP</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "        h1, h2 {\n" +
                "            color: #2c3e50;\n" +
                "        }\n" +
                "        ul {\n" +
                "            list-style-type: disc;\n" +
                "            margin-left: 20px;\n" +
                "        }\n" +
                "        a {\n" +
                "            color: #3498db;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        a:hover {\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <header>\n" +
                "        <h1>Politique de Confidentialité - Educ-APP</h1>\n" +
                "    </header>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>1. Introduction</h2>\n" +
                "        <p>Bienvenue sur <strong>Educ-APP</strong>, une application développée pour offrir des services éducatifs et administratifs en République Démocratique du Congo (RDC). La présente politique de confidentialité explique comment nous collectons, utilisons, et protégeons vos données personnelles dans le cadre de votre utilisation de l'application, conformément aux lois locales en matière de confidentialité, notamment la Loi n° 007/2002 en RDC. Nous nous engageons également à respecter les normes internationales telles que le RGPD (GDPR) ou le CCPA, si elles s'appliquent.</p>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>2. Données Collectées</h2>\n" +
                "        <p>En utilisant <strong>Educ-APP</strong>, nous pouvons collecter les types de données suivants :</p>\n" +
                "        <ul>\n" +
                "            <li><strong>Données personnelles :</strong> Nom, numéro de téléphone, adresse e-mail, ou toute autre information nécessaire pour l’inscription et l'utilisation des fonctionnalités.</li>\n" +
                "            <li><strong>Documents partagés :</strong> Les fichiers téléchargés dans les sections telles que « Demande de documents scolaires » ou « MGP Dépôt plainte ».</li>\n" +
                "            <li><strong>Informations d’appareil :</strong> Type de dispositif, système d’exploitation et journaux d’activité pour assurer le bon fonctionnement de l’application.</li>\n" +
                "        </ul>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>3. Utilisation des Données</h2>\n" +
                "        <p>Nous utilisons les données collectées pour :</p>\n" +
                "        <ul>\n" +
                "            <li>Assurer la communication entre le public et les agents du ministère via la fonctionnalité Chat.</li>\n" +
                "            <li>Traiter les demandes de documents scolaires et répondre dans les délais réglementés.</li>\n" +
                "            <li>Gérer les plaintes via le formulaire numérique et le processus de suivi.</li>\n" +
                "            <li>Envoyer des SMS de campagnes ciblées ou des enquêtes de satisfaction.</li>\n" +
                "            <li>Diffuser des actualités et des contenus éducatifs via les fonctionnalités Actualités et MAG de l'EPST.</li>\n" +
                "        </ul>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>4. Partage des Données</h2>\n" +
                "        <p>Vos données ne seront partagées qu’avec :</p>\n" +
                "        <ul>\n" +
                "            <li>Les agents habilités du ministère pour traiter vos demandes, plaintes et enquêtes.</li>\n" +
                "            <li>Les partenaires de service pour l’envoi des SMS ou la gestion des formulaires.</li>\n" +
                "            <li>Les autorités concernées dans le cadre de plaintes sensibles (comme les cas liés aux VBG ou à la corruption).</li>\n" +
                "        </ul>\n" +
                "        <p>Nous ne vendons ni ne louons vos informations à des tiers.</p>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>5. Sécurité des Données</h2>\n" +
                "        <p>Nous mettons en œuvre des mesures techniques et organisationnelles pour protéger vos données contre l’accès non autorisé, la perte ou la divulgation. Par exemple :</p>\n" +
                "        <ul>\n" +
                "            <li>Les messages échangés via la fonctionnalité Chat sont automatiquement archivés et ne peuvent ni être transférés, ni supprimés par les utilisateurs.</li>\n" +
                "            <li>Les plaintes sensibles sont directement redirigées vers des adresses électroniques sécurisées des responsables.</li>\n" +
                "        </ul>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>6. Vos Droits</h2>\n" +
                "        <p>Vous avez le droit de :</p>\n" +
                "        <ul>\n" +
                "            <li>Accéder à vos données personnelles.</li>\n" +
                "            <li>Corriger ou mettre à jour vos informations.</li>\n" +
                "            <li>Demander la suppression de vos données, sous réserve des obligations légales de conservation.</li>\n" +
                "        </ul>\n" +
                "        <p>Pour exercer ces droits, contactez-nous à l’adresse : <a href=\"mailto:contact@epst-app.cd\">contact@epst-app.cd</a>.</p>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>7. Conservation des Données</h2>\n" +
                "        <p>Les données sont conservées uniquement le temps nécessaire pour réaliser les objectifs décrits dans cette politique ou pour satisfaire aux exigences légales applicables.</p>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>8. Modifications de cette Politique</h2>\n" +
                "        <p>Nous pouvons mettre à jour cette politique de confidentialité. Toute modification sera publiée sur cette page avec une date de mise à jour. Nous vous encourageons à consulter régulièrement cette politique.</p>\n" +
                "    </section>\n" +
                "\n" +
                "    <section>\n" +
                "        <h2>9. Contact</h2>\n" +
                "        <p>Pour toute question ou préoccupation concernant cette politique de confidentialité, veuillez nous contacter à :</p>\n" +
                "        <ul>\n" +
                "            <li><strong>Email :</strong> <a href=\"mailto:sgc@eduquepsp.education\">sgc@eduquepsp.education</a></li>\n" +
                "            <li><strong>Téléphone :</strong> (+243) 840018006</li>\n" +
                "            <li><strong>Adresse :</strong> 02, Avenue des Ambassadeurs, Kinshasa-Gombe, B.P: 3163 KIN 1, RD Congo</li>\n" +
                "        </ul>\n" +
                "    </section>\n" +
                "\n" +
                "    <footer>\n" +
                "        <p>Dernière mise à jour : [Date de mise à jour]</p>\n" +
                "    </footer>\n" +
                "</body>\n";
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

}