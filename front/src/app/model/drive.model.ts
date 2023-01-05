import {Position} from "./mapAddress.model";

export interface Drive{
  stops:Stop[],
  distance:number,
  duration:number,
  price:number
}

export interface Stop{
  address:string,
  position:Position,
  name:string
}
