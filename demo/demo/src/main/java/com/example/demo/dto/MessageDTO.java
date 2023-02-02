package com.example.demo.dto;

import java.sql.Timestamp;

public class MessageDTO {
    private UserDTO sender;

    private UserDTO reciever;

    private String message;

    private boolean read;

    public MessageDTO() {
    }

    public MessageDTO(UserDTO sender, UserDTO receiver, String message, Timestamp timeSent, boolean read) {

        this.sender = sender;
        this.reciever = receiver;
        this.message = message;
        this.read = read;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getReciever() {
        return reciever;
    }

    public void setReciever(UserDTO reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
