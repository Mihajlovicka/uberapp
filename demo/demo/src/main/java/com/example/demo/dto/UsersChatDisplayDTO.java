package com.example.demo.dto;

public class UsersChatDisplayDTO {
    private UserDTO user;
    private ImageDTO image;
    private boolean hasMessages;
    private boolean isClient;

    public UsersChatDisplayDTO() {
    }

    public UsersChatDisplayDTO(UserDTO userDTO, ImageDTO imageDTO, boolean hasMessages, boolean isClient) {
        this.user = userDTO;
        this.image = imageDTO;
        this.hasMessages = hasMessages;
        this.isClient = isClient;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public boolean isHasMessages() {
        return hasMessages;
    }

    public void setHasMessages(boolean hasMessages) {
        this.hasMessages = hasMessages;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }
}
