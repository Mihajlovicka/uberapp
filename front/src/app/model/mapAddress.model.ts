import * as L from "leaflet";

interface PositionInterface {
  latitude:number,
  longitude:number
}

export class Position{
  latitude:number
  longitude:number

  constructor( latitude:number, longitude:number) {
    this.latitude = latitude
    this.longitude = longitude
  }

}

interface MapAddressInterface {
  name: string;
  location: Position,
  address: string,
  number:string,
  marker:L.Marker
}

export class MapAddress{
  name: string
  location: Position
  address: string
  marker:L.Marker|undefined

  constructor(name:string, location:Position, address:string, marker:L.Marker|undefined) {
    this.name = name
    this.location = location
    this.address = address
    this.marker = marker

  }

}
