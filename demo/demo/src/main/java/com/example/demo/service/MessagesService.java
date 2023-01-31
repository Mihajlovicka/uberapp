package com.example.demo.service;

import com.example.demo.dto.MessageDTO;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.model.Chat;
import com.example.demo.model.Message;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.ChatsRepository;
import com.example.demo.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessagesService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ChatsRepository chatsRepository;

    public void notifySupport(){
        simpMessagingTemplate.convertAndSend("/message/support","New message arrived.");
    }

    public void addMessage(MessageDTO message) throws EmailNotFoundException {
        Message m = new Message();
        m.setMessage(message.getMessage());
        User sender = userService.getByEmail(message.getSender().getEmail());
        m.setSender(sender);
        User reciever = null;
        if(message.getSender().getRole().getName().equals("ROLE_CLIENT")){
            reciever = userService.getAdmin();
            notificationService.addNotificationMultiple(
                    new Notification(
                            "Nova poruka",
                            "Imate novu poruku od admina",
                            reciever,
                            "/messages-client"
                    ),userService.getAdmins()
            );
        }
        else {
            reciever = userService.getByEmail(message.getReciever().getEmail());

            notificationService.addNotification(
                    new Notification(
                            "Nova poruka",
                            "Imate novu poruku od admina",
                            reciever,
                            "/messages-client"
                    )
            );
        }
        m.setReciever(reciever);

        User client = getUserByRole(m,"ROLE_CLIENT");
        Chat chat = getChat(client);
        m.setChat(chat);

        messagesRepository.save(m);

    }

    private Chat getChat(User client){
        Chat foundChat = null;
        for(Chat chat : chatsRepository.findAll()){
            if(chat.getClientUsername().equals(client.getEmail())){
                foundChat = chat;
                break;
            }
        }
        if(foundChat!=null){
            return foundChat;
        }
        else{
            foundChat = new Chat();
            foundChat.setClientUsername(client.getEmail());
            foundChat = chatsRepository.save(foundChat);
            return foundChat;
        }


    }

    public List<Message> getMessagesForUser(String email){
        List<Message> messages = new ArrayList<Message>();

        for(Message message:messagesRepository.findAll()){
                if(message.getReciever().getEmail().equals(email) || message.getSender().getEmail().equals(email)){
                    messages.add(message);
                }
            }


        return messages;
    }

    private User getUserByRole(Message message, String role){
        if(message.getReciever().getRole().getName().equals(role)){
            return message.getReciever();
        }
        else{
            return message.getSender();
        }
    }


    public void readMessages(String senderEmail){
        for(Message message : getMessagesForUser(senderEmail)){
            if(message.getReciever().getEmail().equals(senderEmail)){

            }
        }
    }
}
