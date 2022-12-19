package org.epst.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.epst.beans.Utilisateur;

public class ModelUtilisateur {

    SeConnecter seConnecter = new SeConnecter();
    Connection con;
    //
    ResultSet résultats = null;
    //

    public ModelUtilisateur(){
        try{
            con = seConnecter.con;
        }catch(Exception ex){
            System.out.println("Erreur du à: "+ex.getMessage());
        }
    }
    //
    public Utilisateur getUtilisateur(int id){
        Utilisateur utilisateur = new Utilisateur();
        String requete = "SELECT * FROM agent where id = "+id;
        //
        try {
            Statement stmt = con.createStatement();
            résultats = stmt.executeQuery(requete);
            //
            boolean encore = résultats.next();

            while (encore) {
                utilisateur = new Utilisateur(
                    résultats.getLong("id"),
                    résultats.getString(2),
                    résultats.getString(3),
                    résultats.getString(4),
                    résultats.getString(5),
                    résultats.getString(6),
                    résultats.getString(7),
                    résultats.getInt(8),
                    résultats.getString(9),
                    résultats.getString(10),
                    résultats.getString(11),
                    résultats.getString(12),
                        résultats.getString(13),
                        résultats.getString(14)
                );
                //
                System.out.println();
                encore = résultats.next();
            }
            //
        } catch (SQLException e) {
            //traitement de l'exception
        };
        return utilisateur;
    }
    //
    public Utilisateur getUtilisateur(String matricule, String mdp){
        Utilisateur utilisateur = new Utilisateur();
        System.out.println("matricule = '"+matricule+"' and mdp = '"+mdp+"'");
        String requete = "SELECT * FROM agent where matricule = '"+matricule+"' and mdp = '"+mdp+"'";
        //
        try {
            Statement stmt = con.createStatement();
            résultats = stmt.executeQuery(requete);
            //
            boolean encore = résultats.next();

            while (encore) {
                System.out.println("Valeur: "+résultats.getString(13));
                utilisateur = new Utilisateur(
                    résultats.getLong("id"),
                    résultats.getString(2),
                    résultats.getString(3),
                    résultats.getString(4),
                    résultats.getString(5),
                    résultats.getString(6),
                    résultats.getString(7),
                    résultats.getInt(8),
                    résultats.getString(9),
                    résultats.getString(10),
                    résultats.getString(11),
                    résultats.getString(12),
                    résultats.getString(13),
                    résultats.getString(14)
                );
                //
                System.out.println();
                encore = résultats.next();
            }
            //
        } catch (SQLException e) {
            System.out.println("Erreur du à: "+e);
            //traitement de l'exception
        };
        return utilisateur;
    }
    //
    
    public List<Utilisateur> getAllUtilisateur(){
        List<Utilisateur> liste = new LinkedList<>();
        String requete = "SELECT * FROM agent";

        try {
            Statement stmt = con.createStatement();
            résultats = stmt.executeQuery(requete);
            //
            boolean encore = résultats.next();

            while (encore) {
                
                System.out.print("****id: "+résultats.getLong(1)+"_ ad:"+":\n__:\n"+
                résultats.getLong(1)+":__:id\n"+
                résultats.getString(2)+":__:adresse\n"+
                résultats.getString(3)+":__:email\n"+
                résultats.getString(4)+":__:nom\n"+
                résultats.getString(5)+":__:numero\n"+
                résultats.getString(6)+":__:postnom\n"+
                résultats.getString(7)+":__:prenom\n"+
                résultats.getString(8)+":__:role\n"+
                résultats.getString(10)+":__:matricule\n"+
                résultats.getString(11)+":__:id_statut\n"
                );
                
                liste.add(
                    new Utilisateur(
                        résultats.getLong(1),
                        résultats.getString(2),
                        résultats.getString(3),
                        résultats.getString(4),
                        résultats.getString(5),
                        résultats.getString(6),
                        résultats.getString(7),
                        résultats.getInt(8),
                        résultats.getString(9),
                        résultats.getString(10),
                        résultats.getString(11),
                        résultats.getString(12),
                            résultats.getString(13),
                            résultats.getString(14)
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
        liste.forEach((e)->{
            System.out.print(e.getId() + " ___ " + e.getEmail() + 
                "______" + e.getNom() + "____" + e.getRole() + "__");
        });

        return liste;
    }

    public String saveUtilisateur(Utilisateur utilisateur){
        String t = "";

        //
        try{
            String sql = "INSERT INTO agent (id,nom,postnom,prenom,numero,email, adresse,role,matricule,id_statut, date_de_naissance, mdp, province, district) "+
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
 
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, getId());
            statement.setString(2, utilisateur.getNom());
            statement.setString(3, utilisateur.getPostnom());
            statement.setString(4, utilisateur.getPrenom());
            statement.setString(5, utilisateur.getNumero());
            statement.setString(6, utilisateur.getEmail());
            statement.setString(7, utilisateur.getAdresse());
            statement.setInt(8, utilisateur.getRole());
            statement.setString(9, utilisateur.getMatricule());
            statement.setString(10, utilisateur.getId_statut());
            statement.setString(11, utilisateur.getDate_de_naissance());
            statement.setString(12, utilisateur.getMdp());
            statement.setString(13, utilisateur.getProvince());
            statement.setString(14, utilisateur.getDistrict());
            //
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
            t = "A new user was inserted successfully!";
        }catch(Exception ex){
            System.out.println("erreur du à: "+ex);
            System.out.println("erreur du à: "+ex.getMessage());
            System.out.println("erreur du à: "+ex.getLocalizedMessage());
            System.out.println("erreur du à: "+ex.getCause());
            System.out.println("erreur du à: "+ex.getStackTrace());
            t = ""+ex+"\n"+ex.getMessage()+"\n"+ex.getLocalizedMessage()+"\n"+ex.getCause()+"\n"+ex.getStackTrace();
        }

        return t;
    }

    public int supprimerUtilisateur(int id){
        int t = 0;
        System.out.println("______________________________: "+id);
        //
        try{
            String sql = "DELETE FROM agent WHERE id = ?";
 
            PreparedStatement statement = con.prepareStatement(sql);
            //statement.setInt(1, utilisateur.getId());
            statement.setLong(1, id);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("____________delete successfully!");
            }
            t = rowsInserted;
        }catch(Exception ex){
            System.out.println("______________erreur du à: "+ex.getMessage());
            t = 0;
        }

        return t;
    }
    
    public int miseaJourUtilisateur(Utilisateur utilisateur){
        int t = 0;
        System.out.print("****id: "+"_ ad:"+":\n__:\n"+
        utilisateur.getId()+":__:id\n"+
        utilisateur.getAdresse()+":__:adresse\n"+
        utilisateur.getEmail()+":__:email\n"+
        utilisateur.getNom()+":__:nom\n"+
        utilisateur.getNumero()+":__:numero\n"+
        utilisateur.getPostnom()+":__:postnom\n"+
        utilisateur.getPrenom()+":__:prenom\n"+
        utilisateur.getRole()+":__:role\n"+
        utilisateur.getMatricule()+":__:matricule\n"+
        utilisateur.getId_statut()+":__:id_statut\n"
                );
        try{
            String sql = "UPDATE agent SET nom = ?, postnom = ?, prenom = ?, numero = ?, email = ?, adresse = ?, role = ?, matricule = ?, id_statut = ?, date_de_naissance = ?, mdp = ?, province = ?, district = ?  WHERE id = ?";
            //,,,,, ,,,, 
            PreparedStatement statement = con.prepareStatement(sql);

            
            //
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPostnom());
            statement.setString(3, utilisateur.getPrenom());
            statement.setString(4, utilisateur.getNumero());
            statement.setString(5, utilisateur.getEmail());
            statement.setString(6, utilisateur.getAdresse());
            statement.setInt(7, utilisateur.getRole());
            statement.setString(8, utilisateur.getMatricule());
            statement.setString(9, utilisateur.getId_statut());
            statement.setString(10, utilisateur.getDate_de_naissance());
            statement.setString(11, utilisateur.getMdp());
            //
            statement.setLong(12, utilisateur.getId());
            statement.setString(13, utilisateur.getProvince());
            statement.setString(14, utilisateur.getDistrict());

            t = statement.executeUpdate();

        }catch(Exception ex){
            System.out.println("erreur du à: "+ex.getMessage());
            t = 0;
        }
        return t;
    }

    private Long getId(){
        Random random = new Random();
        long random63BitLong = random.nextLong();
        return random63BitLong;
    }

}
//
