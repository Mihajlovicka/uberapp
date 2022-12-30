package com.example.demo.fakeBank;

public class BankAccountNumberDoNotExistException extends Exception{
    public BankAccountNumberDoNotExistException(String message){
        super(message);
    }

}
