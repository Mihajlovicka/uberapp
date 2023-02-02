package com.example.demo.notifclock;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo.model.Drive;
import com.example.demo.model.Notification;
import com.example.demo.service.DriveService;
import com.example.demo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private DriveService driveService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 1000*60)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        //ako je 15 min pred salji vozacu i driveru
        //ako je 10 i 5 min pre a=salji klijentu
        for(Drive d: driveService.getAllDrives()){
            Date starting = d.getDate();
            Date nowTimePlus14 = new Date(System.currentTimeMillis() + 14*60*1000);
            Date nowTimePlus15 = new Date(System.currentTimeMillis() + 15*60*1000);
            if(starting.after(nowTimePlus14) && starting.before(nowTimePlus15)
            || starting.equals(nowTimePlus15)){
                notificationService.addNotification(new Notification("Imate voznju.", "Nova voznja pocinje za 15 minuta.", d.getDriver().getUser(),"/rides-dr"));
                notificationService.addNotificationMultiple(new Notification("Zakazana voznja.", "Zakazana voznja pocinje za 15 minuta.", null,"/rides-cl"),
                        driveService.makeUsersFromPassengersForNotification(d));
            }
            Date nowTimePlus9 = new Date(System.currentTimeMillis() + 9*60*1000);
            Date nowTimePlus10 = new Date(System.currentTimeMillis() + 10*60*1000);
            if(starting.after(nowTimePlus9) && starting.before(nowTimePlus10)
                    || starting.equals(nowTimePlus10)){
                notificationService.addNotificationMultiple(new Notification("Zakazana voznja.", "Zakazana voznja pocinje za 10 minuta.", null,"/rides-cl"),
                        driveService.makeUsersFromPassengersForNotification(d));
            }
            Date nowTimePlus4 = new Date(System.currentTimeMillis() + 4*60*1000);
            Date nowTimePlus5 = new Date(System.currentTimeMillis() + 5*60*1000);
            if(starting.after(nowTimePlus4) && starting.before(nowTimePlus5)
                    || starting.equals(nowTimePlus5)){
                notificationService.addNotificationMultiple(new Notification("Zakazana voznja.", "Zakazana voznja pocinje za 5 minuta.", null,"/rides-cl"),
                        driveService.makeUsersFromPassengersForNotification(d));
            }
        }
    }
}
