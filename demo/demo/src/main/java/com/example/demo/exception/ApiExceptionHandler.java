package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handlerEmailExistException(ApiRequestException e){
        // 1. create payload containing exception details
        EmailExist emailExist = new EmailExist(
                e.getMessage(),
                HttpStatus.MULTI_STATUS,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        // 2. return response entity
        return new ResponseEntity<>(emailExist, HttpStatus.MULTI_STATUS);

    }
}
