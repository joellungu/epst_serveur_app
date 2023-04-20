package org.epst.controlleurs;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
