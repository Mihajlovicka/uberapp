import * as L from "leaflet";

interface PositionInterface {
  lat:number,
  lng:number
}

export class Position implements PositionInterface{
  lat:number
  lng:number

  constructor(positionI:PositionInterface) {
    this.lat = positionI.lat
    this.lng = positionI.lng

  }

}

interface MapAddressInterface {
  name: string;
  position: Position,
  address: string,
  number:string,
  marker:L.Marker
}

export class MapAddress implements MapAddressInterface{
  name: string
  position: Position
  address: string
  number:string
  marker:L.Marker

  constructor(mapI:MapAddressInterface) {
    this.name = mapI.name
    this.position = mapI.position
    this.address = mapI.address
    this.number = mapI.number
    this.marker = mapI.marker

  }

}
