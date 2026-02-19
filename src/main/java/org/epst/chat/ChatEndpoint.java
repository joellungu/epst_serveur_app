package org.epst.chat;


//import jakarta.inject.Inject;
//import jakarta.ws.rs.PathParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReferenceArray;

import jakarta.inject.Inject;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.epst.beans.MessageBeanRepository;


@ServerEndpoint(
  value = "/chat/{username}/{role}",
  decoders = MessageDecoder.class, 
  encoders = MessageEncoder.class
)
public class ChatEndpoint {

    private static Set<String> listeConvAss = new HashSet<>();
    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    TransactionManager transactionManager;

    private Session session;
    private static Set<ChatEndpoint> chatEndpoints 
      = new CopyOnWriteArraySet<>();
      private static Set<Session> sessions 
      = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    private static List<HashMap<String, String>> listeUsers = new LinkedList<HashMap<String, String>>(); 

    @OnOpen
    public void onOpen(
      Session session, 
      @PathParam("username") String username, @PathParam("role") String role) throws IOException, EncodeException {
 
        //
        HashMap<String, String> user = new HashMap<>();
        user.put("clientId_", session.getId());
        user.put("username_", username);
        user.put("role_", role);
        user.put("visible_", "oui");
        listeUsers.add(user);
        System.out.println("Votre role est: ____________________________: "+username);
        System.out.println("Votre role est: ____________________________: "+role);
        System.out.println("Votre role est: ____________________________: "+session.getId());
        System.out.println("Votre role est: ____________________________:");
        
        //
        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);
        //
        sessions.add(session);
        //
        //UserChat userChat = new UserChat(session.getId(),username,session);
        //if(!username.equals("0")){//&& !username.equals("4")
        //  listeUsers.add(user);
        //}
        

        //Message message = new Message();
        //message.setFrom(username);
        //message.setContent("Connected!");
        //broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) 
      throws IOException, EncodeException {
        ObjectMapper obj = new ObjectMapper();
        System.out.println(obj.writeValueAsString(message));

        if(message.getAll_()){
            //La tout le monde sauf les agent du epst
          //listeUsers
          listeUsers.forEach((e)->{
            e.put("hostId_",session.getId());
          });
          List<HashMap<String, String>> li = new LinkedList<>();
          for(HashMap<String, String> m : listeUsers){
            if(!m.get("role_").equals("admin") && m.get("visible_").equals("oui")){//visible
              li.add(m);
              System.out.println("votre truc: "+m.get("role_"));
            }
          }
          //listeUsers.stream()
          //.filter(c -> !c.get("role").equals("admin"))
          //.collect(Collectors.toList());
          HashMap<String, List<HashMap<String, String>>> ls = new HashMap<>();
          ls.put("liste", li);
          String rep = obj.writeValueAsString(ls);
          session.getAsyncRemote().sendText(rep);
          //
        }else if(message.getClose_()){
            //Fin de la conversation
            //Ce qu'il y a eut bel et bien une conversation
            managedExecutor.submit(()->{
                try{
                    transactionManager.begin();
                    message.persist();
                    transactionManager.commit();
                }catch (Exception ex){
                    System.out.println(ex);
                }
            });
            sessions.forEach((s)->{
                if(s.getId().equals(message.getHostId_())) {
                    listeConvAss.forEach((c)-> {
                        String[] b = c.split(":");
                        if (b[0].equals(session.getId()) || b[1].equals(session.getId())) {
                            sessions.forEach((ss) -> {
                                if (ss.getId().equals(b[0])) {
                                    try {
                                        Message m = new Message();
                                        m.setContent_("Fin de la conversation");
                                        m.setClose_(true);
                                        s.getAsyncRemote().sendText(obj.writeValueAsString(m));
                                    } catch (Exception ex) {
                                        ss.getAsyncRemote().sendText("Erreur du: " + ex);
                                        System.out.println("Erreur du::: " + ex);
                                    }
                                }
                            });
                            //
                            sessions.forEach((ss) -> {
                                if (ss.getId().equals(b[1])) {
                                    try {
                                        Message m = new Message();
                                        m.setContent_("Fin de la conversation");
                                        m.setClose_(true);
                                        s.getAsyncRemote().sendText(obj.writeValueAsString(m));
                                    } catch (Exception ex) {
                                        ss.getAsyncRemote().sendText("Erreur du:/// " + ex);
                                        System.out.println("Erreur du: " + ex);
                                    }
                                }
                            });
                        }
                    });
                }
            });
            //
            sessions.remove(session);
            //
            users.remove(users.get(session.getId()));
            //
            listeUsers.forEach((r)->{if(r.get("clientId_").equals(session.getId())){listeUsers.remove(r);}});
            //
        }else if(message.getTo_().equals("hote")){
            //La conversation
            managedExecutor.submit(()->{
                try{
                    transactionManager.begin();
                    message.persist();
                    transactionManager.commit();
                }catch (Exception ex){
                    System.out.println(ex);
                }
            });
            sessions.forEach((s)->{
              //J'enregistre la conversation...
              //
              System.out.println("Id1: "+s.getId()+" == "+message.getClientId_()+" == "+s.getId().equals(message.getClientId_()));
              if(s.getId().equals(message.getClientId_())){
              listeUsers.forEach((u)->{//
                  if(u.get("clientId_").equals(message.getClientId_())){
                      u.put("visible_", message.getVisible_());
                      //u.put("clientId", session.getId());
                  }
              });
              try {
                //
                listeConvAss.add(message.getHostId_()+":"+message.getClientId_());
                //Vérification si la conversation est finit
                  if(message.content_.equals("Fin de la conversation") || message.getClose_()){
                      closeConv(message.clientId_);
                      closeConv(message.hostId_);
                  } else {
                      //__________________________
                      s.getAsyncRemote().sendText(obj.writeValueAsString(message));
                      System.out.println("Cool c'est bon!");
                      //
                  }
                //
                System.out.println("Cool c'est bon! 1");
                //
                //messageBeanRepository.saveData(message);
              } catch (JsonProcessingException e) {
                // 
                e.printStackTrace();
              }
            }else{
                System.out.println("Pas de session pour lui: "+s.getId());
            }
          });
        }else{
            //La conversation
            managedExecutor.submit(()->{
                try{
                    transactionManager.begin();
                    message.persist();
                    transactionManager.commit();
                }catch (Exception ex){
                    System.out.println(ex);
                }
            });
          sessions.forEach((s)->{
            if(s.getId().equals(message.getHostId_())){
              listeUsers.forEach((u)->{//
                if(u.get("clientId_").equals(message.getClientId_())){
                  u.put("visible_", message.getVisible_());
                  //u.put("clientId", session.getId());
                }
              });
              try {
                  //
                  listeConvAss.add(message.getHostId_()+":"+message.getClientId_());
                  //messageBeanRepository.saveData(message);
                  //Vérification si la conversation est finit
                  if(message.content_.equals("Fin de la conversation") || message.getClose_()){
                      closeConv(message.clientId_);
                      closeConv(message.hostId_);
                  } else {
                      s.getAsyncRemote().sendText(obj.writeValueAsString(message));
                      System.out.println("Cool c'est bon!");
                      //
                  }
                  message.persist();
                    //messageBeanRepository.saveData(message);
              } catch (JsonProcessingException e) {
                //
                e.printStackTrace();
                //
              }
            }
          });
        }
         
/*
          try {
              session.getAsyncRemote().sendText(obj.writeValueAsString(message));
              HashMap<String, String> ac = new HashMap<>();
              //action.put("requete", "start");
              //action.put("idSessionHote", session.getId());
              //action.put("contenu", "Salut comment ?");
              //session.getAsyncRemote().sendText(obj.writeValueAsString(ac));//Accué de réception
          }catch (Exception ex){
              System.out.println(ex);
          }

 */

    }

    @OnClose
    @Transactional
    public void onClose(Session session) throws IOException, EncodeException {
 
        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom_(users.get(session.getId()));
        message.setContent_("Disconnected!");
        message.persist();
        //broadcast(message);
        String[] l_conv = new String[2];
        listeConvAss.forEach((e)->{
            if(e.contains(session.getId())){
                var t = new AtomicReferenceArray<>(e.split(":"));
                l_conv[0] = t.get(0);
                l_conv[1] = t.get(1);
                System.out.println(l_conv[0]);
                System.out.println(l_conv[1]);
            }
        });
        closeConv(l_conv[0]);//
        //
        closeConv(l_conv[1]);//
        //
        users.remove(users.get(session.getId()));
        //
        listeUsers.forEach((r)->{if(r.get("clientId_") == session.getId()){listeUsers.remove(r);}});
        //
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom_(users.get(session.getId()));
        message.setContent_("Disconnected!");
        message.persist();
        //broadcast(message);
        String[] l_conv = new String[2];
        listeConvAss.forEach((e)->{
            if(e.contains(session.getId())){
                var t = new AtomicReferenceArray<>(e.split(":"));
                l_conv[0] = t.get(0);
                l_conv[1] = t.get(1);
                System.out.println(l_conv[0]);
                System.out.println(l_conv[1]);
            }
        });
        closeConv(l_conv[0]);//
        //
        closeConv(l_conv[1]);//
        //
        users.remove(users.get(session.getId()));
        //
        listeUsers.forEach((r)->{if(r.get("clientId_") == session.getId()){listeUsers.remove(r);}});
        //
        sessions.remove(session);
    }

    private static void broadcast(Message message) 
      throws IOException, EncodeException {
 
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                      sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void closeConv(String session){
        //
        sessions.forEach((s)->{
            if(s.getId().equals(session)){
                listeUsers.forEach((u)->{//
                    if(u.get("clientId_").equals(session)){
                        u.put("conversation_", "false");
                        listeUsers.remove(u);
                        //u.put("clientId", session.getId());
                    }else{
                        u.put("conversation_", "false");
                    }
                });
                try {
                    ObjectMapper obj = new ObjectMapper();
                    //
                    Message message = new Message();
                    message.setConversation_(false);
                    s.getAsyncRemote().sendText(obj.writeValueAsString(message));
                    System.out.println("Cool c'est bon!");
                    //
                    //message.persist();
                    //messageBeanRepository.saveData(message);
                } catch (JsonProcessingException e) {
                    //
                    e.printStackTrace();
                    //
                }
            }
        });
    }
}

/*
abstract

        //
        ObjectMapper obj = new ObjectMapper();
        
        if(message.getFrom().equals("0") || message.getFrom().equals("4")){
          if(message.getTo().equals("system")){//l'utilisateur veut faire une action dans le systeme
          
          String r = message.getContent();
          String comm = "";
          //System.out.println("conn: "+r);
            String[] result = r.split(",");
            for (String str : result) {
              System.out.println(str + ": ");
            }
            comm = result[0];
            System.out.println("comm: "+comm);
            
            if(message.getContent().equals("getall")){
              System.out.println(message.getContent());
              System.out.println(message.getFrom());
              System.out.println(message.getTo());
              
              //
              Predicate<HashMap<String, String>> byStatut = u -> u.get("statut") == "en-attente";
              listeUsers = listeUsers.stream().filter(byStatut)
              .collect(Collectors.toList());
              //
              String rep = obj.writeValueAsString(listeUsers);
              /*
              try{
              JsonArrayBuilder jsonStr = Json.createArrayBuilder(listeUsers);
              System.out.println(rep);
              } catch (Exception ex){
                System.out.println("Du: "+ex);
              }
              
              
              listeUsers.forEach((e)->{
                System.out.println("le truc: "+e.get("sessionId"));
                System.out.println("le truc: "+e.get("username"));
                System.out.println("le truc: "+e.get("session"));
              });
                          

              //session.getBasicRemote().sendObject("users");
              session.getAsyncRemote().sendText("{\"idsession\":\""+session.getId()+"\",\"liste\":"+rep+" }");
              
            }else if(message.getContent().split(",")[0].equals("communique")){//Etablissement de la connexion par epst agent
              //
              String[] commandes = message.getContent().split(",");
              String c1 = commandes[0];
              String c2 = commandes[1];
              String c3 = commandes[2];
              String c4 = commandes[3];
              //regle sementhyque: 1:code, 2:idsessionhote, 3:idsessionclient 4:nomdelhote
              //Je dois maintenant renvoyer au client une commande pour commencer la communication...
              System.out.println("la commande: "+commandes.length);
              System.out.println("c1: "+c1);
              System.out.println("c2: "+c2);
              System.out.println("c3: "+c3);
              //System.out.println("c4: "+c4);
              //
              sessions.forEach((s)->{
                if(s.getId().equals(c3)){
                  HashMap<String, String> action = new HashMap<>();
                  action.put("requete", "start");
                  action.put("idSessionHote", c2);
                  action.put("contenu", "Salut comment ?");
                  try{
                    s.getAsyncRemote().sendText(obj.writeValueAsString(action));
                    HashMap<String, String> ac = new HashMap<>();
                    action.put("requete", "start");
                    action.put("idSessionHote", session.getId());
                    //action.put("contenu", "Salut comment ?");
                    //session.getAsyncRemote().sendText(obj.writeValueAsString(ac));//Accué de réception
                    //
                    listeUsers.forEach((u)->{
                      //
                      if(u.get("sessionId").equals(c3)){
                        u.replace("statut", "en-communication");
                      }
                    });
                  }catch(Exception ex){
                    sessions.forEach((ss)->{
                      if(s.getId().equals(c3)){
                        ss.getAsyncRemote().sendText(ex.getMessage());
                      }
                    });
                  }
                }
              });
              
            }else if(message.getContent() == "close"){

            }else{
              System.out.println("conn: "+message.getContent());
            }
            
          }else{//epst: Tu t'adresse à quelqu'un...
            //
            HashMap<String, String> action = new HashMap<>();
            action.put("requete", "start");
            action.put("idSessionHote", session.getId());
            action.put("contenu", message.getContent());
            //
            sessions.forEach((tc)->{
              if(tc.getId().equals(message.getTo())){
                try{
                  tc.getAsyncRemote().sendText(obj.writeValueAsString(action));
                  //
                  HashMap<String, String> ac = new HashMap<>();
                  action.put("requete", "start");
                  action.put("idSessionHote", session.getId());
                  action.put("contenu", "");
                  //session.getAsyncRemote().sendText(obj.writeValueAsString(ac));//Accué de réception
                }catch(Exception ex){

                }
              }
            });
          }
        }else{
          //{"from":"0","to":"system","content":"getall"}
          HashMap<String, String> action = new HashMap<>();
          action.put("requete", "start");
          action.put("idSessionHote", session.getId());
          action.put("contenu", message.getContent());
          //
          sessions.forEach((tc)->{
            if(tc.getId().equals(message.getTo())){
              System.out.println("Message envoyé à: "+message.getContent());
              try{
                tc.getAsyncRemote().sendText(obj.writeValueAsString(action));
              }catch(Exception ex){

              }
            }
          });
        }
       
*/
