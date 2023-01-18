import * as L from "leaflet";

interface PositionInterface {
  lat:number,
  lng:number
}

export class Position{
  lat:number
  lng:number

  constructor( lat:number, lng:number) {
    this.lat = lat
    this.lng = lng
  }

}

interface MapAddressInterface {
  name: string;
  position: Position,
  address: string,
  number:string,
  marker:L.Marker
}

export class MapAddress{
  name: string
  position: Position
  address: string
  marker:L.Marker|undefined

  constructor(name:string, position:Position, address:string, marker:L.Marker|undefined) {
    this.name = name
    this.position = position
    this.address = address
    this.marker = marker

  }

}
