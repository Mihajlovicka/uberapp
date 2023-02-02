package com.example.demo.exception;

public class TransactionIdDoesNotExistException extends Exception{
    public TransactionIdDoesNotExistException (String message){
        super(message);
    }
}
