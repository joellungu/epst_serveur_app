package org.epst.chat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Message extends PanacheEntity {

    public String from_;
    public String to_;
    public String content_;
    public String hostId_;
    public String clientId_;
    public Boolean close_;
    public Boolean all_;
    public String visible_;
    public Boolean conversation_;
    public String matricule_;
    public String date_;
    public String heure_;
    
    //from,to,content,hostId,clientId,close,all,visible,conversation

    Message(String from, String to , String content, String hostId, String clientId, Boolean close,
            Boolean all, String visible, Boolean conversation, String matricule, String date, String heure){}

    Message(){}

}