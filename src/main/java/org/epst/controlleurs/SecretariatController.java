package org.epst.controlleurs;

import org.epst.models.secretariat.Arrete;
import org.epst.models.secretariat.Departement;
import org.epst.models.secretariat.Secretariat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("secretariat")
public class SecretariatController {

    @Inject
    ObjectMapper objectMapper;

    public static class UploadForm {
        @FormParam("file")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public byte[] file;
    }

    public static class DepartementPayload {
        public Long id;
        public String nom;
        public String responsable;
    }

    public static class ArretePayload {
        public String texte;
    }

    public static class SecretariatPayload {
        public String denomination;
        public String sigle;
        public String adresse;
        public String telephone;
        public String email;
        public String responsable;
        public String maps;
        public List<DepartementPayload> departements;
        public ArretePayload arrete;
        public String attributionMission;
        public String historique;
        public String realisation;
    }

    public static class DepartementView {
        public Long id;
        public String nom;
        public String responsable;
        public boolean hasPhoto;
    }

    public static class ArreteView {
        public String texte;
        public boolean hasPhoto;
    }

    public static class SecretariatView {
        public Long id;
        public String denomination;
        public String sigle;
        public String adresse;
        public String telephone;
        public String email;
        public String responsable;
        public String maps;
        public List<DepartementView> departements;
        public ArreteView arrete;
        public String attributionMission;
        public String historique;
        public String realisation;
    }

    public static class SecretariatFullForm {
        @FormParam("payload")
        @PartType(MediaType.APPLICATION_JSON)
        public String payload;

        @FormParam("photoProfil")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public byte[] photoProfil;

        @FormParam("photoArrete")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public byte[] photoArrete;

        @FormParam("departementIndexCsv")
        public String departementIndexCsv;

        @FormParam("departementPhoto")
        @PartType(MediaType.APPLICATION_OCTET_STREAM)
        public List<byte[]> departementPhoto;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("detail")
    @Transactional
    public Response getSecretariat(@QueryParam("id") Long id) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null) {
            return Response.status(404).build();
        }
        return Response.ok(toView(secretariat)).build();
    }

    @GET
    @Path("all")
    @Transactional
    public Response allSecretariat() {
        List<Secretariat> ss = Secretariat.listAll();
        List<HashMap> secretariats = new LinkedList<>();
        ss.forEach((s) -> {
            HashMap data = new HashMap();
            data.put("id", s.id);
            data.put("denomination", s.denomination);
            data.put("sigle", s.sigle);
            secretariats.add(data);
        });
        return Response.ok(secretariats).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response putSecretariat(@PathParam("id") Long id, SecretariatPayload payload) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null) {
            return Response.status(404).build();
        }
        applyPayload(secretariat, payload, false);
        return Response.ok(toView(secretariat)).build();
    }

    @PUT
    @Path("{id}/full")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response putSecretariatFull(@PathParam("id") Long id, MultipartFormDataInput input) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null) {
            return Response.status(404).build();
        }
        if (input == null) {
            return Response.status(400).build();
        }
        SecretariatPayload payload = readPayload(input);
        if (payload == null) {
            return Response.status(400).build();
        }
        applyPayload(secretariat, payload, false);
        byte[] photoProfil = readPartBytes(input, "photoProfil");
        if (photoProfil != null) {
            secretariat.photoProfil = photoProfil;
        }
        byte[] photoArrete = readPartBytes(input, "photoArrete");
        if (photoArrete != null) {
            if (secretariat.arrete == null) {
                secretariat.arrete = new Arrete();
            }
            secretariat.arrete.photo = photoArrete;
        }
        applyDepartementPhotos(
            secretariat,
            readPartString(input, "departementIndexCsv"),
            readPartBytesList(input, "departementPhoto")
        );
        return Response.ok(toView(secretariat)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response postSecretariat(SecretariatPayload payload) {
        if (payload == null) {
            return Response.status(400).build();
        }
        Secretariat secretariat = new Secretariat();
        applyPayload(secretariat, payload, true);
        secretariat.persist();
        return Response.ok(toView(secretariat)).build();
    }

    @POST
    @Path("full")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response postSecretariatFull(MultipartFormDataInput input) {
        if (input == null) {
            return Response.status(400).build();
        }
        SecretariatPayload payload = readPayload(input);
        if (payload == null) {
            return Response.status(400).build();
        }
        Secretariat secretariat = new Secretariat();
        applyPayload(secretariat, payload, true);
        byte[] photoProfil = readPartBytes(input, "photoProfil");
        if (photoProfil != null) {
            secretariat.photoProfil = photoProfil;
        }
        byte[] photoArrete = readPartBytes(input, "photoArrete");
        if (photoArrete != null) {
            if (secretariat.arrete == null) {
                secretariat.arrete = new Arrete();
            }
            secretariat.arrete.photo = photoArrete;
        }
        secretariat.persist();
        applyDepartementPhotos(
            secretariat,
            readPartString(input, "departementIndexCsv"),
            readPartBytesList(input, "departementPhoto")
        );
        return Response.ok(toView(secretariat)).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteSecretariat(@PathParam("id") Long id) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null) {
            return Response.status(404).build();
        }
        if (secretariat.departements != null) {
            secretariat.departements.clear();
        }
        secretariat.arrete = null;
        secretariat.delete();
        return Response.ok().build();
    }

    @POST
    @Path("{id}/photoProfil")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response uploadProfil(@PathParam("id") Long id, @MultipartForm UploadForm form) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null) {
            return Response.status(404).build();
        }
        if (form == null || form.file == null) {
            return Response.status(400).build();
        }
        secretariat.photoProfil = form.file;
        return Response.ok().build();
    }

    @POST
    @Path("{id}/arrete/photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response uploadArretePhoto(@PathParam("id") Long id, @MultipartForm UploadForm form) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null) {
            return Response.status(404).build();
        }
        if (form == null || form.file == null) {
            return Response.status(400).build();
        }
        if (secretariat.arrete == null) {
            secretariat.arrete = new Arrete();
        }
        secretariat.arrete.photo = form.file;
        return Response.ok().build();
    }

    @POST
    @Path("departement/{id}/photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response uploadDepartementPhoto(@PathParam("id") Long id, @MultipartForm UploadForm form) {
        Departement departement = Departement.findById(id);
        if (departement == null) {
            return Response.status(404).build();
        }
        if (form == null || form.file == null) {
            return Response.status(400).build();
        }
        departement.photo = form.file;
        return Response.ok().build();
    }

    @GET
    @Path("photoprofil/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response profilChef(@PathParam("id") Long id) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null || secretariat.photoProfil == null) {
            return Response.status(404).build();
        }
        return Response.ok(secretariat.photoProfil).build();
    }

    @GET
    @Path("departement/photo/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response photoDepartement(@PathParam("id") Long id) {
        Departement departement = Departement.findById(id);
        if (departement == null || departement.photo == null) {
            return Response.status(404).build();
        }
        return Response.ok(departement.photo).build();
    }

    @GET
    @Path("arrete/photo/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response photoArrete(@PathParam("id") Long id) {
        Secretariat secretariat = Secretariat.findById(id);
        if (secretariat == null || secretariat.arrete == null || secretariat.arrete.photo == null) {
            return Response.status(404).build();
        }
        return Response.ok(secretariat.arrete.photo).build();
    }

    private void applyPayload(Secretariat secretariat, SecretariatPayload payload, boolean isCreate) {
        if (payload == null) {
            return;
        }
        if (payload.denomination != null || isCreate) {
            secretariat.denomination = payload.denomination;
        }
        if (payload.sigle != null || isCreate) {
            secretariat.sigle = payload.sigle;
        }
        if (payload.adresse != null || isCreate) {
            secretariat.adresse = payload.adresse;
        }
        if (payload.telephone != null || isCreate) {
            secretariat.telephone = payload.telephone;
        }
        if (payload.email != null || isCreate) {
            secretariat.email = payload.email;
        }
        if (payload.responsable != null || isCreate) {
            secretariat.responsable = payload.responsable;
        }
        if (isCreate) {
            secretariat.maps = null;
        }
        if (payload.attributionMission != null || isCreate) {
            secretariat.attributionMission = payload.attributionMission;
        }
        if (payload.historique != null || isCreate) {
            secretariat.historique = payload.historique;
        }
        if (payload.realisation != null || isCreate) {
            secretariat.realisation = payload.realisation;
        }
        if (payload.arrete != null) {
            if (secretariat.arrete == null) {
                secretariat.arrete = new Arrete();
            }
            secretariat.arrete.texte = payload.arrete.texte;
        }
        if (payload.departements != null) {
            Map<Long, Departement> existing = new HashMap<>();
            for (Departement d : secretariat.departements) {
                if (d.id != null) {
                    existing.put(d.id, d);
                }
            }
            List<Departement> updated = new ArrayList<>();
            for (DepartementPayload dp : payload.departements) {
                Departement d = null;
                if (dp != null && dp.id != null) {
                    d = existing.get(dp.id);
                }
                if (d == null) {
                    d = new Departement();
                }
                d.nom = dp != null ? dp.nom : null;
                d.responsable = dp != null ? dp.responsable : null;
                d.secretariat = secretariat;
                updated.add(d);
            }
            secretariat.departements.clear();
            secretariat.departements.addAll(updated);
        }
    }

    private void applyDepartementPhotos(Secretariat secretariat, String indexCsv, List<byte[]> photos) {
        if (indexCsv == null || indexCsv.isEmpty() || photos == null) {
            return;
        }
        String[] parts = indexCsv.split(",");
        int count = Math.min(parts.length, photos.size());
        for (int i = 0; i < count; i++) {
            try {
                int idx = Integer.parseInt(parts[i].trim());
                if (idx >= 0 && idx < secretariat.departements.size()) {
                    Departement d = secretariat.departements.get(idx);
                    d.photo = photos.get(i);
                }
            } catch (NumberFormatException ex) {
                // ignore invalid index
            }
        }
    }

    private SecretariatPayload readPayload(MultipartFormDataInput input) {
        String payloadStr = readPartString(input, "payload");
        if (payloadStr == null) {
            return null;
        }
        try {
            return objectMapper.readValue(payloadStr, SecretariatPayload.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String readPartString(MultipartFormDataInput input, String name) {
        try {
            Map<String, List<InputPart>> map = input.getFormDataMap();
            List<InputPart> parts = map.get(name);
            if (parts == null || parts.isEmpty()) {
                return null;
            }
            return parts.get(0).getBodyAsString();
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] readPartBytes(MultipartFormDataInput input, String name) {
        try {
            Map<String, List<InputPart>> map = input.getFormDataMap();
            List<InputPart> parts = map.get(name);
            if (parts == null || parts.isEmpty()) {
                return null;
            }
            return parts.get(0).getBody(byte[].class, null);
        } catch (Exception e) {
            return null;
        }
    }

    private List<byte[]> readPartBytesList(MultipartFormDataInput input, String name) {
        List<byte[]> list = new ArrayList<>();
        try {
            Map<String, List<InputPart>> map = input.getFormDataMap();
            List<InputPart> parts = map.get(name);
            if (parts == null) {
                return list;
            }
            for (InputPart p : parts) {
                byte[] b = p.getBody(byte[].class, null);
                if (b != null) {
                    list.add(b);
                }
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    private SecretariatView toView(Secretariat secretariat) {
        SecretariatView view = new SecretariatView();
        view.id = secretariat.id;
        view.denomination = secretariat.denomination;
        view.sigle = secretariat.sigle;
        view.adresse = secretariat.adresse;
        view.telephone = secretariat.telephone;
        view.email = secretariat.email;
        view.responsable = secretariat.responsable;
        view.maps = null;
        view.attributionMission = secretariat.attributionMission;
        view.historique = secretariat.historique;
        view.realisation = secretariat.realisation;

        ArreteView av = new ArreteView();
        if (secretariat.arrete != null) {
            av.texte = secretariat.arrete.texte;
            av.hasPhoto = secretariat.arrete.photo != null;
        }
        view.arrete = av;

        List<DepartementView> dpts = new ArrayList<>();
        for (Departement d : secretariat.departements) {
            DepartementView dv = new DepartementView();
            dv.id = d.id;
            dv.nom = d.nom;
            dv.responsable = d.responsable;
            dv.hasPhoto = d.photo != null;
            dpts.add(dv);
        }
        view.departements = dpts;
        return view;
    }

    // byte[] is populated directly by resteasy-multipart-provider
}
