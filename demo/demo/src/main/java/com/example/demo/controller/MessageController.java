package com.example.demo.controller;

import com.example.demo.converter.MessageConverter;
import com.example.demo.dto.DriverAccountDTO;
import com.example.demo.dto.MessageDTO;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.Image;
import com.example.demo.model.Message;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {
    @Autowired
    MessagesService messagesService;

    @Autowired
    MessageConverter messageConverter;

    @PostMapping(value="api/newMessage")
    public ResponseEntity newMessage(@RequestBody MessageDTO message) throws EmailNotFoundException {
        messagesService.addMessage(message);
        messagesService.notifySupport();
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping(value="api/openMessage")
    public ResponseEntity openMessage() throws EmailNotFoundException {

        messagesService.seeMessages();
        messagesService.notifySupport();
        return new ResponseEntity("",HttpStatus.OK);
    }
    @PostMapping(value="api/openMessageForChat")
    public ResponseEntity openMessage(@RequestBody String chat) throws EmailNotFoundException {

        messagesService.seeMessages(chat);
        messagesService.notifySupport();
        return new ResponseEntity("",HttpStatus.OK);
    }
    @GetMapping(value = "/api/getMessages")
    public ResponseEntity getDriver(@RequestParam String email) {


            return new ResponseEntity(messageConverter.toDTOs(messagesService.getMessagesForUser(email)), HttpStatus.OK);

    }
}
