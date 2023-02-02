package com.example.demo.fakeBank;

public class BankTransactionDTO {
    private TransactionType transactionType;
    private Double amount;
    private TransactionStatus transactionStatus;
    private String sender;
    private String receiver;

    public BankTransactionDTO(){

    }

    public BankTransactionDTO(TransactionType transactionType, Double amount, TransactionStatus transactionStatus, String sender, String receiver) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.sender = sender;
        this.receiver = receiver;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
