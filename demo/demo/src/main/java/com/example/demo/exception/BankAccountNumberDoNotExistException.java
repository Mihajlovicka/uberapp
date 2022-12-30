package com.example.demo.exception;

public class BankAccountNumberDoNotExistException extends Exception{
    public BankAccountNumberDoNotExistException(String message){
        super(message);
    }

}
