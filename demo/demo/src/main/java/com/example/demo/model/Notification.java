package com.example.demo.model;

import javax.persistence.*;

@Table
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_to_notify")
    private User userToNotify;

    @Column
    private String href;

    @Column
    private boolean opened;

    public Notification(Long id, String title, String message, User userToNotify, String href, boolean opened) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.userToNotify = userToNotify;
        this.href = href;
        this.opened = opened;
    }

    public Notification(String title, String message, User userToNotify, String href) {
        this.title = title;
        this.message = message;
        this.userToNotify = userToNotify;
        this.href = href;
    }

    public Notification() {
    }

    public Notification(Notification notification) {
        this.message = notification.message;
        this.userToNotify = notification.userToNotify;
        this.href = notification.href;
        this.title = notification.title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUserToNotify() {
        return userToNotify;
    }

    public void setUserToNotify(User userToNotify) {
        this.userToNotify = userToNotify;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
