package org.epst.chat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;


public class Archive {
    public String from;
    public String to;
    public String content;
    public String hostId;
    public String clientId;
    public Boolean close;
    public Boolean all;
    public String visible;
    public Boolean conversation;
    public String matricule;
    public String date;
    public String heure;
}
