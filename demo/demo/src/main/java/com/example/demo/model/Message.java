package com.example.demo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reciever")
    private User reciever;

    @Column
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat")
    private Chat chat;

    public Message() {
    }

    public Message(Long id, User sender, User reciever, String message, Timestamp timeSent, Chat chat) {
        this.id = id;
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.chat = chat;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
