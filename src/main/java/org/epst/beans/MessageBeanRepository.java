package org.epst.beans;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.smallrye.common.annotation.NonBlocking;
import org.epst.chat.Message;
import org.epst.models.ModelMessage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.PathParam;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
public class MessageBeanRepository implements PanacheRepository<MessageBean> {

    ModelMessage modelMessage = new ModelMessage();

    //@NonBlocking
    public void saveData(Message message){
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");

                MessageBean ms = new MessageBean();
                /*
                ms.setAllst(message.getAll());
                ms.setCloset(message.getClose());
                ms.setClientIdt(message.getClientId());
                ms.setConversationt(message.getConversation());
                ms.setDatet(message.getDate());
                ms.setContentt(message.getContent());
                ms.setMatriculet(message.getMatricule());
                ms.setFromt(message.getFrom());
                ms.setHostIdt(message.getHostId());
                ms.setTot(message.getTo());
                ms.setVisiblet(message.getVisible());
                ms.setHeuret(message.getHeure());
                //ms.persist();

                 */
                modelMessage.save(ms);
                //

                try{
                    //persist(ms);
                }catch (Exception ex){
                    System.out.println("Erreur: "+ex.getMessage());
                }

                System.out.println("Cool c'est bon!");
            }
        };

        thread.start();

    }

    public List<MessageBean> getArchive1(@PathParam("matricule") String matricule, @PathParam("date") String date) {
        List<MessageBean> l = modelMessage.getArchiveConversation(matricule);

        System.out.println("matricule: " + matricule);
        System.out.println("taille: " + l.size());
        l.forEach((e) -> {
            System.out.println("matricule: " + e.getMatriculet());
        });
        Predicate<MessageBean> t = value -> value.getMatriculet().equals(matricule) && value.getDatet().contains(date);
        return l.stream().filter(t).collect(Collectors.toList());
    }
}
