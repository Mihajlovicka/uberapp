import {Position} from "./mapAddress.model";
export interface StopInterface{
    address:string,
    location:Position,
    name:string
  }

export class Stop{
    public address: string;
    public location: Position;
    public name: string;

    constructor(address:string, location:Position, name:string){
        this.address = address
        this.location = location;
        this.name = name;
    }
}
