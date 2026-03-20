package org.epst.online;

import org.epst.models.Classe;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public final class ClassLabelUtil {
    private static final Pattern NON_ALNUM = Pattern.compile("[^a-z0-9]+");
    private static final Pattern MULTI_SPACE = Pattern.compile("\\s+");

    private ClassLabelUtil() {
    }

    public static Classe resolveClass(String classIdOrLabel) {
        if (classIdOrLabel == null || classIdOrLabel.isBlank()) {
            return null;
        }

        Classe byId = resolveById(classIdOrLabel);
        if (byId != null) {
            return byId;
        }

        String target = normalize(classIdOrLabel);
        List<Classe> classes = Classe.listAll();
        for (Classe c : classes) {
            String label = buildLabel(c);
            if (!label.isBlank() && normalize(label).equals(target)) {
                return c;
            }
        }
        return null;
    }

    public static boolean matchesLabel(Classe classe, String label) {
        if (classe == null || label == null || label.isBlank()) {
            return false;
        }
        String classLabel = buildLabel(classe);
        if (classLabel.isBlank()) {
            return false;
        }
        return normalize(classLabel).equals(normalize(label));
    }

    public static String buildLabel(Classe classe) {
        if (classe == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        appendPart(sb, classe.niveau);
        appendPart(sb, classe.cycle);
        if (classe.option != null && !classe.option.isBlank()) {
            appendPart(sb, classe.option);
        } else {
            appendPart(sb, classe.section);
        }
        appendPart(sb, classe.nom);
        return sb.toString().trim();
    }

    private static Classe resolveById(String classId) {
        try {
            UUID uuid = UUID.fromString(classId);
            return Classe.findById(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static void appendPart(StringBuilder sb, String value) {
        if (value != null && !value.isBlank()) {
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(value.trim());
        }
    }

    private static String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        normalized = normalized.toLowerCase(Locale.ROOT);
        normalized = NON_ALNUM.matcher(normalized).replaceAll(" ");
        normalized = MULTI_SPACE.matcher(normalized).replaceAll(" ").trim();
        return normalized;
    }
}
