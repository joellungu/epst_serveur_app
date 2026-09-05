package org.epst.models;

public class LoginRequest {
    public String matricule;
    public String mdp;

    // Constructeurs
    public LoginRequest() {}

    public LoginRequest(String matricule, String mdp) {
        this.matricule = matricule;
        this.mdp = mdp;
    }

    // Getters et Setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
