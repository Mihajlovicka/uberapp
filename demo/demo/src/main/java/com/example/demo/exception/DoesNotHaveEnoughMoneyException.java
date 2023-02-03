package com.example.demo.exception;

public class DoesNotHaveEnoughMoneyException extends Exception{
    public DoesNotHaveEnoughMoneyException(String message){
        super(message);
    }
}
