import {Position} from "./mapAddress.model";
export interface StopInterface{
    address:string,
    position:Position,
    name:string
  }

export class Stop{
    public address: string;
    public position: Position;
    public name: string;

    constructor(address:string, position:Position, name:string){
        this.address = address
        this.position = position;
        this.name = name;
    }
}
