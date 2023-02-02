import { ClientsAccount } from "./clientsAccount.model";
import { DriversAccount } from "./driversAccount.model";
import { Passenger } from "./passenger.model";
import { Stop } from "./stop.model";

export enum DriveStatus{
    OWNER_PAYMENT_WAITING = 'OWNER_PAYMENT_WAITING',
    PASSENGERS_WAITING = 'PASSENGERS_WAITING',
    PAYMENT_WAITING = 'PAYMENT_WAITING',
    DRIVER_WAITING = 'DRIVER_WAITING',
    DRIVE_STARTED = 'DRIVE_STARTED',
    DRIVE_ENDED = 'DRIVE_ENDED',
    DRIVE_REJECTED = 'DRIVE_REJECTED',
    DRIVE_FAILED = 'DRIVE_FAILED'
  }

export interface DriveInterface{
    stops: Stop[];
    distance: number;
    duration: number;
    price: number;
    passengers: Passenger[];
    baby: number;
    babySeats: number;
    pets: number;
    seats: number;
    driveStatus: DriveStatus;
    owner: ClientsAccount;
    routeJSON: {};
    driver: DriversAccount;
    date: string;
    splitBill: boolean;
    ownerDebit: number;
}

export class Drive implements DriveInterface{
    public stops: Stop[];
    public distance: number;
    public duration: number;
    public price: number;
    public passengers: Passenger[];
    public baby: number;
    public babySeats: number;
    public pets: number;
    public driveStatus: DriveStatus;
    public owner: ClientsAccount;
    public routeJSON: {};
    public seats: number;
    public driver: DriversAccount;
    public date: string;
    public splitBill: boolean;
    public ownerDebit: number;
    constructor(driveInt: DriveInterface){
        this.stops = driveInt.stops;
        this.distance = driveInt.distance;
        this.duration = driveInt.duration;
        this.price = driveInt.price;
        this.passengers = driveInt.passengers;
        this.baby = driveInt.baby;
        this.babySeats = driveInt.babySeats;
        this.pets = driveInt.pets;
        this.driveStatus = driveInt.driveStatus;
        this.owner = driveInt.owner;
        this.routeJSON = driveInt.routeJSON;
        this.driver = driveInt.driver;
        this.seats = driveInt.seats;
        this.date = driveInt.date;
        this.splitBill = driveInt.splitBill;
        this.ownerDebit = driveInt.ownerDebit;
    }
    
}