package com.example.demo.converter;

import com.example.demo.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.model.Message;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageConverter {
    @Autowired
    UserConverter userConverter;


    public MessageDTO toDTO(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(message.getMessage());
        messageDTO.setSender(userConverter.toDTO(message.getSender()));
        messageDTO.setReciever(userConverter.toDTO(message.getReciever()));
        messageDTO.setRead(message.isOpened());
        return messageDTO;
    }
    public List<MessageDTO> toDTOs(List<Message> messages){
        List<MessageDTO>messageDTOS = new ArrayList<>();
        for(Message message : messages){
            messageDTOS.add(this.toDTO(message));
        }
        return messageDTOS;
    }
}
