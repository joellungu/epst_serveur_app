package org.epst.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import org.epst.beans.NoteTraitementBean;

public class ModelNoteTraitement {
    
    SeConnecter seConnecter = new SeConnecter();
    Connection con;
    //
    ResultSet résultats = null;
    //
    NoteTraitementBean noteTraitementBean;

    public ModelNoteTraitement(){
        try{
            con = seConnecter.con;
        }catch(Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }
    }

    /*
    public Long saveNote(NoteTraitementBean noteTraitementBean){
        Long t ;
        //
        Long id_ = getId();
        try{
            String sql = "INSERT INTO note_traitement (id, nom_admin, reference, note) "+
            "VALUES (?, ?, ?, ?)";
    
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, id_);
            statement.setString(2, noteTraitementBean.getNomIdmin());
            statement.setString(3, noteTraitementBean.getReference());
            statement.setString(4, noteTraitementBean.getNote());
            //
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
            t = id_;
        }catch(Exception ex){
            System.out.println("erreur du à: "+ex);
            System.out.println("erreur du à: "+ex.getMessage());
            System.out.println("erreur du à: "+ex.getLocalizedMessage());
            System.out.println("erreur du à: "+ex.getCause());
            System.out.println("erreur du à: "+ex.getStackTrace());
            t = 0l;//
        }
        //
        return t;
    }

    */
    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }

}
