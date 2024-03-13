package org.quentin.gameoflife.model;

public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    public ChatMessage() {
    }

    public ChatMessage(MessageType type, String content, String sender) {
        this.type = type;
        this.content = content;
        this.sender = sender;
    }


    public ChatMessage(String type, String content, String sender) {
        this.type = MessageType.valueOf(type);
        this.content = content;
        this.sender = sender;
    }

    // Getters et Setters
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public enum MessageType {
        CHAT,
        RULE_CHANGE
    }
}
