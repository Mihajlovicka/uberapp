import { ClientsAccount } from "./clientsAccount.model";

export enum DrivePassengerStatus{
    WAITING='WAITING',
    REJECTED='REJECTED',
    ACCEPTED='ACCEPTED'
}

export enum PaymentPassengerStatus{
    WAITING='WAITING',
    REJECTED='REJECTED',
    ACCEPTED='ACCEPTED',
    NOT_PAYING = 'NOT_PAYING'
}

export interface PassengerInterface{
    passengerEmail: string,
    passengerName: string,
    passengerSurname: string,
    contribution: DrivePassengerStatus,
    payment: PaymentPassengerStatus
    debit: number;
    payingEnabled: boolean;
    transactionId: number;
    
}

export class Passenger implements PassengerInterface{
    public passengerEmail: string;
    public passengerName: string;
    public passengerSurname: string;
    public contribution: DrivePassengerStatus;
    public payment: PaymentPassengerStatus;
    public debit: number;
    public payingEnabled: boolean;
    public transactionId: number;
    constructor(passengerInt: PassengerInterface){
        this.passengerEmail = passengerInt.passengerEmail;
        this.passengerName = passengerInt.passengerName;
        this.passengerSurname = passengerInt.passengerSurname;
        this.contribution = passengerInt.contribution;
        this.payment = passengerInt.payment;
        this.debit = passengerInt.debit;
        this.payingEnabled = passengerInt.payingEnabled;
        this.transactionId = passengerInt.transactionId;
    }
}