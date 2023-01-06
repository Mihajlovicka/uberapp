import {Position} from "./mapAddress.model";

interface DriveInterface{
  stops:Stop[],
  distance:number,
  duration:number,
  price:number
}

export class Drive implements DriveInterface{
  public stops:Stop[]
  public distance:number
  public duration:number
  public price:number

  constructor(driveI:DriveInterface) {
    this.stops = driveI.stops
    this.distance = driveI.distance
    this.duration = driveI.duration
    this.price = driveI.price
  }
}

interface StopInterface{
  address:string,
  position:Position,
  name:string
}

export class Stop implements StopInterface{
  address:string
  position:Position
  name:string

  constructor(stopI:StopInterface) {
    this.address = stopI.address
    this.position = stopI.position
    this.name = stopI.name

  }

}
