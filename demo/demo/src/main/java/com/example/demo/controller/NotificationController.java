package com.example.demo.controller;

import com.example.demo.dto.MessageDTO;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotificationNotFoundException;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping(value="api/openNotification")
    public ResponseEntity openNotification(@RequestBody Long id) throws NotificationNotFoundException {
        notificationService.openNotification(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping(value = "/api/getNotifications")
    public ResponseEntity getDriver(@RequestParam String email) {
        return new ResponseEntity(notificationService.getNotifications(email), HttpStatus.OK);
    }
}
