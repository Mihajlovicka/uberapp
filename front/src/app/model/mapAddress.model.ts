import * as L from "leaflet";

export interface Position {
  lat:number,
  lng:number
}

export interface MapAddress {
  name: string;
  position: Position,
  address: string,
  number:string,
  marker:L.Marker
}
