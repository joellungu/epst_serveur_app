package org.epst.models;

public class StartConversationRequest {
    public String clientName;

    // Constructeurs
    public StartConversationRequest() {}

    public StartConversationRequest(String clientName) {
        this.clientName = clientName;
    }

    // Getters et Setters
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}