import { ClientsAccount } from "./clientsAccount.model";
import { Stop } from "./stop.model";


export enum PaymentStatus{
  PAID = 'PAID',
  NOT_PAID = 'NOT_PAID',


}

export interface DriveInterface{

  stops:Stop[],
  distance:number,
  duration:number,
  price:number,
  clients:ClientsAccount[],
  seats: number;
  baby: number;
  babySeats: number;
  pets: number;
  owner: ClientsAccount|null;
  routeJSON:{}
}

export class Drive implements DriveInterface{

  public stops:Stop[]
  public distance:number
  public duration:number
  public price:number
  public clients: ClientsAccount[];
  public seats: number;
  public baby: number;
  public babySeats: number;
  public pets: number;
  public owner: ClientsAccount|null;
  public routeJSON: {};

  constructor(driveI:DriveInterface) {
    this.stops = driveI.stops
    this.distance = driveI.distance
    this.duration = driveI.duration
    this.price = driveI.price
    this.clients = driveI.clients;
    this.seats = driveI.seats;
    this.baby = driveI.baby;
    this.babySeats = driveI.babySeats;
    this.pets = driveI.pets;
    this.owner = driveI.owner;
    this.routeJSON = driveI.routeJSON
  }
}

