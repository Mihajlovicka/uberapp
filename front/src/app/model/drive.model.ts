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
  clients:ClientsAccount[]
}

export class Drive implements DriveInterface{

  public stops:Stop[]
  public distance:number
  public duration:number
  public price:number
  public clients: ClientsAccount[];

  constructor(driveI:DriveInterface) {
    this.stops = driveI.stops
    this.distance = driveI.distance
    this.duration = driveI.duration
    this.price = driveI.price
    this.clients = driveI.clients;
  }
}

