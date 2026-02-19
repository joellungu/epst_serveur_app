package org.epst.chat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;

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

    // Getters
    public String getFrom_() {
        return from_;
    }

    public String getTo_() {
        return to_;
    }

    public String getContent_() {
        return content_;
    }

    public String getHostId_() {
        return hostId_;
    }

    public String getClientId_() {
        return clientId_;
    }

    public Boolean getClose_() {
        return close_;
    }

    public Boolean getAll_() {
        return all_;
    }

    public String getVisible_() {
        return visible_;
    }

    public Boolean getConversation_() {
        return conversation_;
    }

    public String getMatricule_() {
        return matricule_;
    }

    public String getDate_() {
        return date_;
    }

    public String getHeure_() {
        return heure_;
    }

    // Setters
    public void setFrom_(String from_) {
        this.from_ = from_;
    }

    public void setTo_(String to_) {
        this.to_ = to_;
    }

    public void setContent_(String content_) {
        this.content_ = content_;
    }

    public void setHostId_(String hostId_) {
        this.hostId_ = hostId_;
    }

    public void setClientId_(String clientId_) {
        this.clientId_ = clientId_;
    }

    public void setClose_(Boolean close_) {
        this.close_ = close_;
    }

    public void setAll_(Boolean all_) {
        this.all_ = all_;
    }

    public void setVisible_(String visible_) {
        this.visible_ = visible_;
    }

    public void setConversation_(Boolean conversation_) {
        this.conversation_ = conversation_;
    }

    public void setMatricule_(String matricule_) {
        this.matricule_ = matricule_;
    }

    public void setDate_(String date_) {
        this.date_ = date_;
    }

    public void setHeure_(String heure_) {
        this.heure_ = heure_;
    }
}