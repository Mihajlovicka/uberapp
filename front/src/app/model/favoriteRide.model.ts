import * as L from "leaflet";
import {MapAddress, Position} from "./mapAddress.model";

export interface FavoriteRide{
  routeJSON: any;
  realAddress: {
    name: string;
    location: {
      latitude:number
      longitude:number
    },
    address: string,
  }[];
}
