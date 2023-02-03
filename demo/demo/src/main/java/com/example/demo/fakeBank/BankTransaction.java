package com.example.demo.fakeBank;

import javax.persistence.*;

@Table
@Entity
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private TransactionType transactionType; //smer

    @Column
    private Double amount;

    @Column
    private TransactionStatus transactionStatus;

    @Column
    private String sender; //znaci ovo ako se placa uberu ovo je EMAIL ONOGA CIJI JE UBER NALOG A NE BANK
//SLAZEM SE DA JE BZVZ ALI NE DIRAJTE! :)
    @Column
    private String receiver;

    public BankTransaction(){}

    public BankTransaction(Long id, TransactionType transactionType, Double amount, TransactionStatus transactionStatus, String sender, String receiver) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
