import { ClientsAccount } from "./clientsAccount.model";
import { DriversAccount } from "./driversAccount.model";
import { Passenger } from "./passenger.model";
import { Stop } from "./stop.model";




export enum PriceStart{
  seats4 = 200,
  seats5 = 200,
  seats6 = 500,
  seats7 = 500,
  seats8 = 500,
  seats9 = 1000
}

export interface DriveReservationFormInterface{
  stops:Stop[],
  distance:number,
  duration:number,
  price:number,
  passengers:Passenger[],
  seats: number;
  baby: number;
  babySeats: number;
  pets: number;
  owner: ClientsAccount|null;
  routeJSON:{};
  //driveStatus: DriveStatus;
  //driver: DriversAccount|null;
  splitBill: boolean;
  date: string;
  ownerDebit: number;
}

export class DriveReservationForm implements DriveReservationFormInterface{

  public stops:Stop[]
  public distance:number
  public duration:number
  public price:number
  public passengers: Passenger[];
  public seats: number;
  public baby: number;
  public babySeats: number;
  public pets: number;
  public owner: ClientsAccount|null;
  public routeJSON: {};
  //public driveStatus: DriveStatus;
  //public driver: DriversAccount|null;
  public splitBill: boolean;
  public date: string;
  public ownerDebit: number;

  constructor(driveI:DriveReservationFormInterface) {
    this.stops = driveI.stops
    this.distance = driveI.distance
    this.duration = driveI.duration
    this.price = driveI.price
    this.passengers = driveI.passengers;
    this.seats = driveI.seats;
    this.baby = driveI.baby;
    this.babySeats = driveI.babySeats;
    this.pets = driveI.pets;
    this.owner = driveI.owner;
    this.routeJSON = driveI.routeJSON
    //this.driveStatus = driveI.driveStatus;
    //this.driver = driveI.driver;
    this.splitBill = driveI.splitBill;
    this.date = driveI.date;
    this.ownerDebit = driveI.ownerDebit;
  }
}

