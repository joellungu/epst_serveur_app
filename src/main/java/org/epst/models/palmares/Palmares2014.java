package org.epst.models.palmares;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@ApplicationScoped
public class Palmares2014 {
    //
    // ✅ Chemin configurable depuis application.properties
    // Cache mémoire (clé = année + code)
    private final Map<String, String[]> cache = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        System.out.println("Chargement du fichier CSV en mémoire...");

        try (InputStream is = getClass().getResourceAsStream("/data/Resultat_Global_2014.csv")) {
            if (is == null) {
                throw new IllegalStateException("Fichier CSV introuvable dans resources/data !");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                // Lire toutes les lignes (en ignorant l’en-tête si nécessaire)
                List<String> lines = reader.lines().skip(1).collect(Collectors.toList());

                for (String line : lines) {
                    String[] parts = line.split(";", -1); // -1 pour garder les colonnes vides
                    if (parts.length >= 2) {
                        String annee = parts[17].trim();
                        String code = parts[11].trim();
                        cache.put(annee + "_" + code, parts);
                    }
                }
                System.out.println("✅ Chargement terminé : " + cache.size() + " lignes indexées.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Recherche rapide dans le cache sans relire le fichier.
     */
    public Optional<String[]> findByAnneeAndCode(String annee, String code) {
        return Optional.ofNullable(cache.get(annee + "_" + code));
    }

    /**
     * Exemple : retourne une ligne formatée en JSON ou texte.
     */
    public String getLineAsJson(String annee, String code) {
        return findByAnneeAndCode(annee, code)
                .map(arr -> Arrays.toString(arr))
                .orElse("{\"error\": \"Non trouvé\"}");
    }

}
