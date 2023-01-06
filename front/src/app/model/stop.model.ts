import {Position} from "./mapAddress.model";
export interface StopInterface{
    address:string,
    position:Position,
    name:string
  }

export class Stop implements StopInterface{
    public address: string;
    public position: Position;
    public name: string;

    constructor(stopInt: StopInterface){
        this.address = stopInt.address;
        this.position = stopInt.position;
        this.name = stopInt.name;
    }
}