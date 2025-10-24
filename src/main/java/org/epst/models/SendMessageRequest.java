package org.epst.models;

public class SendMessageRequest {
    public Long conversationId;
    public String sender;
    public String content;

    // Constructeurs
    public SendMessageRequest() {}

    public SendMessageRequest(Long conversationId, String sender, String content) {
        this.conversationId = conversationId;
        this.sender = sender;
        this.content = content;
    }

    // Getters et Setters
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}