package com.example.demo.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    @Async
    void send(EmailDetails details);
}
