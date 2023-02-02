package com.example.demo.service;

import com.example.demo.exception.NotificationNotFoundException;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationsRepository notificationsRepository;

    public List<Notification> getNotifications(String email){
        List<Notification> foundNotifications = new ArrayList<Notification>();
        for(Notification notification : notificationsRepository.findAll()){
            if(notification.getUserToNotify().getEmail().equals(email)) {
                foundNotifications.add(notification);
            }
        }
        Collections.reverse(foundNotifications);
        return foundNotifications;
    }

    public Notification addNotification(Notification notification){
        notification = notificationsRepository.save(notification);
        notification.setOpened(false);
        notifyUser(notification.getUserToNotify().getEmail());
        return notification;
    }

    public void addNotificationMultiple(Notification notification, List<User> users){
        for(User user : users){
            Notification newNotification = new Notification(notification);
            newNotification.setUserToNotify(user);
            addNotification(newNotification);
        }
    }



    public Notification openNotification(Long id) throws NotificationNotFoundException {
        for(Notification n : notificationsRepository.findAll()){
            if(n.getId().equals(id)){
                n.setOpened(true);
                notificationsRepository.save(n);
                notifyUser(n.getUserToNotify().getEmail());
                return n;
            }
        }
        throw new NotificationNotFoundException("Ne postoji notifikacija sa trazenim ID");

    }

    public void notifyUser(String email){
        simpMessagingTemplate.convertAndSend("/notify/"+email,"New notification.");
    }

}
