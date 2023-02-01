package com.example.demo.service;

import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotificationNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationsRepository notificationsRepository;


    @Autowired
    UserService userService;

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



    public void addedToDriveNotify(Set<Passenger> passengers, Long id) throws EmailNotFoundException {
        for(Passenger passenger: passengers){
            addNotification(new Notification(
                    "Nova voznja",
                    "Cao " + passenger.getPassengerName()+"!"+"Dodati ste u voznju. Udjite na link kako bi ste videli vise o ovome.",
                    userService.findByEmail(passenger.getPassengerEmail()),
                    "/passenger/accept-drive/"+id));
        }
    };



    public void addNotificationMultiple(Notification notification, List<User> users){
        for(User user : users){
            notification.setUserToNotify(user);
            addNotification(notification);
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
