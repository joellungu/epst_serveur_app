package org.epst.controlleurs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.epst.beans.MessageBean;
import org.epst.beans.MessageBeanRepository;
import org.epst.beans.Utilisateur;
import org.epst.chat.Message;
import org.epst.models.ModelMessage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Path("archive")
public class ArchiveController {

    //@Inject
    //MessageBeanRepository messageBeanRepository;
    ModelMessage modelMessage = new ModelMessage();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get1llo() {

        // create a JSON string
        return "Allo";
    }

    @Path("/a1/{matricule}/{date}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getArchive1(@PathParam("matricule") String matricule, @PathParam("date") String date) {
        HashMap params = new HashMap();
        params.put("matricule_",matricule);
        params.put("date_",date);

        return Message.list("matricule_ =:matricule_ and date_ =:date_",params);
    }

    @Path("/conv/{matricule}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getConversation(@PathParam("matricule") String matricule) {
        List<Message> l = Message.list("hostId_",matricule);
        //List<MessageBean> listWithoutDuplicates = new ArrayList<>();
        Predicate<MessageBean> t = value -> value.getHostIdt().equals(matricule);// && value.getDatet().contains(date);
        return l;
    }

    @Path("/a2/{matricule}/{date}/{heure}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageBean> getArchive2(@PathParam("matricule") String matricule, @PathParam("date") String date,
                                   @PathParam("heure") String heure) {
        List<MessageBean> l = modelMessage.getArchiveConversation(matricule);
        Collections.reverse(l);
        Predicate<MessageBean> t = value -> value.getMatriculet().equals(matricule) && value.getDatet().contains(date)
                && value.getHeuret().contains(heure);
        //
        return l.stream().filter(t).collect(Collectors.toList());
    }

}
