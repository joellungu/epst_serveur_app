package org.epst.models;

import org.epst.beans.Magasin;
import org.epst.beans.MessageBean;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ModelMessage {
    //
    SeConnecter seConnecter = new SeConnecter();
    //
    Connection con;
    //
    ResultSet résultats = null;
    //

    public ModelMessage(){
        try{
            con = seConnecter.con;
        }catch(Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }
    }

    public void save(MessageBean messageBean) {
        Long id_ = getId();
        try{
            /*
            String from, String to , String content, String hostId, String clientId, Boolean close,
            Boolean all, String visible, Boolean conversation, String matricule, String date
             */
            String sql = "INSERT INTO archive (fromt, tot, contentt, hostIdt, clientIdt, closet, allt, visiblet, conversationt, matriculet, datet, heure) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = seConnecter.con.prepareStatement(sql);
            //statement.setLong(1, id_);
            statement.setString(1, messageBean.getFromt());
            statement.setString(2, messageBean.getTot());
            statement.setString(3, messageBean.getContentt());
            statement.setString(4, messageBean.getHostIdt());
            statement.setString(5, messageBean.getClientIdt());
            statement.setBoolean(6, messageBean.getCloset());
            statement.setBoolean(7, messageBean.getAllst());
            statement.setString(8, messageBean.getVisiblet());
            statement.setBoolean(9, messageBean.getConversationt());
            statement.setString(10, messageBean.getMatriculet());
            statement.setString(11, messageBean.getDatet());
            statement.setString(12, messageBean.getHeuret());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
            //t = id_;
        }catch(Exception ex){
            System.out.println("erreur du à: "+ex);
            System.out.println("erreur du à: "+ex.getMessage());
            System.out.println("erreur du à: "+ex.getLocalizedMessage());
            System.out.println("erreur du à: "+ex.getCause());
            System.out.println("erreur du à: "+ex.getStackTrace());
            //t = 0l;//
        }
    }

    public List<MessageBean> getArchiveConversation(String matricule){
        List<MessageBean> liste = new LinkedList<>();
        //
        String requete = "SELECT * FROM archive where matriculet = '"+matricule+"'";

        try {
            Statement stmt = seConnecter.con.createStatement();
            résultats = stmt.executeQuery(requete);
            //
            boolean encore = résultats.next();

            while (encore) {
                System.out.println("C'est: "+résultats.getString(2));
                liste.add(
                        new MessageBean(
                                résultats.getString(1),
                                résultats.getString(2),
                                résultats.getString(3),
                                résultats.getString(4),
                                résultats.getString(5),
                                résultats.getBoolean(6),
                                résultats.getBoolean(7),
                                résultats.getString(8),
                                résultats.getBoolean(9),
                                résultats.getString(10),
                                résultats.getString(11),
                                résultats.getString(12)
                        )
                );

                System.out.println("La langueur: "+liste.size());
                encore = résultats.next();
            }
            //
        } catch (SQLException e) {
            //traitement de l'exception
            System.out.println(e);
        };
        //
        return liste;
    }

    public List<MessageBean> getArchiveConversation2(String matricule){
        List<MessageBean> liste = new LinkedList<>();
        //
        String requete = "SELECT * FROM archive where hostidt = '"+matricule+"'";

        try {
            Statement stmt = seConnecter.con.createStatement();
            résultats = stmt.executeQuery(requete);
            //
            boolean encore = résultats.next();

            while (encore) {
                System.out.println("C'est: "+résultats.getString(2));
                liste.add(
                        new MessageBean(
                                résultats.getString(1),
                                résultats.getString(2),
                                résultats.getString(3),
                                résultats.getString(4),
                                résultats.getString(5),
                                résultats.getBoolean(6),
                                résultats.getBoolean(7),
                                résultats.getString(8),
                                résultats.getBoolean(9),
                                résultats.getString(10),
                                résultats.getString(11),
                                résultats.getString(12)
                        )
                );

                System.out.println("La langueur: "+liste.size());
                encore = résultats.next();
            }
            //
        } catch (SQLException e) {
            //traitement de l'exception
            System.out.println(e);
        };
        //
        return liste;
    }

    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }

}
