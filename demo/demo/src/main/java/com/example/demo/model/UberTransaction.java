package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;




@Table
@Entity
public class UberTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double amount;

    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm")
    @Column
    private Date dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drive_id")
    private Drive drive;

    public UberTransaction(){}

    public UberTransaction(Long id, Double amount, Date dateTime, Drive drive) {
        this.id = id;
        this.amount = amount;
        this.dateTime = dateTime;
        this.drive = drive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }
}
