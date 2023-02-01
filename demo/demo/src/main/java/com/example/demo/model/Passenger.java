package com.example.demo.model;

import com.example.demo.dto.ClientAccountDTO;

import javax.persistence.*;

@Table
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column
    private String passengerEmail;

   @Column
   private String passengerName;

   @Column
   private String passengerSurname;

    @Column
    private DrivePassengerStatus contribution;



    @Column
    private PaymentPassengerStatus payment;

    @Column
    private Double debit;

    public Passenger(){}

    public Passenger(Long id, String passengerEmail, String passengerName, String passengerSurname,DrivePassengerStatus contribution, PaymentPassengerStatus payment, Double debit) {
        this.id = id;
        this.passengerEmail = passengerEmail;
        this.passengerName = passengerName;
        this.passengerSurname = passengerSurname;
        this.contribution = contribution;
        this.payment = payment;
        this.debit = debit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DrivePassengerStatus getContribution() {
        return contribution;
    }

    public void setContribution(DrivePassengerStatus contribution) {
        this.contribution = contribution;
    }

    public PaymentPassengerStatus getPayment() {
        return payment;
    }

    public void setPayment(PaymentPassengerStatus payment) {
        this.payment = payment;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerSurname() {
        return passengerSurname;
    }

    public void setPassengerSurname(String passengerSurname) {
        this.passengerSurname = passengerSurname;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }
}
