import {Component, OnInit, ElementRef, Output, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {UserRegistrationService} from "../user-registration.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatDialog} from '@angular/material/dialog';
import {RoutesDialogComponent} from "../dialog-template/routes-dialog/routes-dialog.component";
import {MapAddress, Position} from '../model/mapAddress.model'
import {MapService} from "../service/map.service";
import {firstValueFrom, lastValueFrom} from "rxjs";


L.Icon.Default.imagePath = 'assets/';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  private map!: L.Map;

  private stops: MapAddress[] = [{} as MapAddress, {} as MapAddress, {} as MapAddress, {} as MapAddress, {} as MapAddress];

  @ViewChild('address1') addr1: any;
  @ViewChild('address2') addr2: any;
  private allValid: boolean = false;

  public nextPage:boolean = false;

  public addressChanged(newAddress: any, addressNum: number) {
    console.log(this.stops)
    this.removeMarker(addressNum)
    this.stops[addressNum] = newAddress;
    if (addressNum === 0) {
      this.addMarker(addressNum, newAddress.position, "Pocenta stanica")
    } else if (addressNum === 4) {
      this.addMarker(addressNum, newAddress.position, "Krajnja stanica")
    } else {
      this.addMarker(addressNum, newAddress.position, "Stanica broj" + addressNum)
    }
    console.log(this.stops)
  }


  private initMap(): void {
    this.map = L.map('map');

    var title = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    })
    this.map.addLayer(title);
    this.map.setView([45.251787, 19.837155], 19);
    navigator.geolocation.getCurrentPosition(position => {
      const {latitude, longitude} = position.coords;
      console.log(latitude, longitude)
      this.map.panTo({lat: latitude, lng: longitude});
    }, () => {
      console.log("error")
    }, {
      enableHighAccuracy: true,
      maximumAge: 30000,
      timeout: 27000
    })
  }

  constructor(private el: ElementRef,
              public service: UserRegistrationService,
              public dialog: MatDialog,
              private mapService:MapService) {
  }

  ngOnInit(): void {
    this.initMap()
  }

  addMarker(number: number, coords: any, message: string) {
    let iconOptions = {
      icon: L.icon({
        iconUrl: "assets/startMark.png",
        iconSize: [30, 30]
      })
    }
    var marker = L.marker([coords.lat, coords.lng], iconOptions).bindPopup(message);
    this.map.addLayer(marker);
    this.map.panTo({lat: coords.lat, lng: coords.lng});
    this.setMarker(number, marker)
  }


  removeMarker(i: number) {
    if (this.stops[i].marker !== undefined)
      if (this.map.hasLayer(this.stops[i].marker))
        this.map.removeLayer(this.stops[i].marker)
  }

  updateMarker(i: number, lat: number, lng: number) {
    this.stops[i].marker.setLatLng({lat, lng});
  }

  setMarker(i: number, marker: any) {
    this.stops[i].marker = marker
  }

  makeAdditionalVisible() {
    var elem = this.el.nativeElement.querySelectorAll('.additionalAddresses');
    elem.forEach((el: any) => {
      return el.classList.remove('additionalStopsHidden');
    });
    elem = this.el.nativeElement.querySelector('#additionalButton');
    elem.classList.add('additionalButtonHidden');
  }

  dialogOC(): Promise<any> {
    const dialogRef = this.dialog.open(RoutesDialogComponent);
    return dialogRef.afterClosed().toPromise().then((value:string)=>{return value})
  }

  async openRouteDialog() {
    var result = await this.dialogOC()
    var stops:[number, number][] = this.getStops()
    if (result === 'duration' || result === 'distance') {
      var n = stops.length
      var combinations = this.mapService.permute(n - 2)//1->n-1
      const matrix$ = this.mapService.orderStops(stops, result);
      var matrix: Array<Array<number>> = await firstValueFrom(matrix$);
      var results: number[] = this.mapService.getRouteSums(matrix, combinations, n)
      stops = this.mapService.getBestRouteCombination(results, combinations, stops)
    }
    this.showRoutesPathSame(stops)

  }

//lng, lat
  getStops() :[number,number][] {
    var stops: [number,number][] = []
    for(let i = 0; i< this.stops.length;i++){
      if(this.stops[i].position !== undefined){
        stops.push([this.stops[i].position.lng, this.stops[i].position.lat])
      }
    }
    return stops
  }

  isValid(e: any) {
    this.allValid = e;
  }

  showRoutesPathSame(stops:[number,number][]) {
    this.mapService.showRoute(stops).subscribe((res) => {
      L.geoJson(res).addTo(this.map)
      this.map.fitBounds(L.geoJson(res).getBounds())
    })

  }


  showRoute(){
    this.addr1.formsubmit()
    this.addr2.formsubmit()
    if(this.allValid) {
      if (this.service.isLoggedIn()) {
        this.openRouteDialog()
      } else {
        this.showRoutesPathSame(this.getStops())
      }
      this.nextPage = true;
    }
    this.mapService.openErrorDialog("Adrese nisu unete.")
  }

  openNextPage(){

  }


}

