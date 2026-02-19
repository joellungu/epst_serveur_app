package org.epst.controlleurs;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.epst.beans.NoteTraitementBean;
import org.epst.models.ModelNoteTraitement;
import org.epst.models.Note_traitement;

@Path("/note")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NoteControlleur {

    private static final ObjectMapper mapper = new ObjectMapper();


    //@Transactional
    @Path("/ajouter")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response noterMa(Note_traitement noteTraitement){
        noteTraitement.persist();
        ObjectNode json = mapper.createObjectNode();
        json.put("status", 1);
        return Response.status(Response.Status.CREATED).entity("ok").build();
    }

}
