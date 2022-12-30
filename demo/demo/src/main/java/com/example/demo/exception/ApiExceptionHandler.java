package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {EmailExistException.class})
    public ResponseEntity<Object> handlerEmailExistException(EmailExistException e){
        // 1. create payload containing exception details
        EmailExistBean emailExist = new EmailExistBean(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        // 2. return response entity
        return new ResponseEntity<>(emailExist, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(value = {PlateNumberExistException.class})
    public ResponseEntity<Object> handlerPlateNumberExistException(PlateNumberExistException e){
        PlateNumberExistBean plateNumberExist = new PlateNumberExistBean(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(plateNumberExist, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = {EmailNotFoundException.class})
    public ResponseEntity<Object> handlerEmailNotFoundExist(EmailNotFoundException e){
        EmailNotFountBean emailNotFount = new EmailNotFountBean(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(emailNotFount, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(value = {BankAccountNumberDoNotExistException.class})
    public ResponseEntity<Object> handlerBankAccountNumberDoNotExist(BankAccountNumberDoNotExistException e){
        BankAccountNumberDoNotExistBean bankAccountNumberDoNotExist = new BankAccountNumberDoNotExistBean(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(bankAccountNumberDoNotExist, HttpStatus.CONFLICT);
    }

}
