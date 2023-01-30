package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String clientUsername;

    public Chat() {
    }

    public Chat(Long id, String client) {
        this.id = id;
        this.clientUsername = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String client) {
        this.clientUsername = client;
    }
}
