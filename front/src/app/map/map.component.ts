import {Component, OnInit, ElementRef, Output, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {UserRegistrationService} from "../user-registration.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatDialog} from '@angular/material/dialog';
import {RoutesDialogComponent} from "../dialog-template/routes-dialog/routes-dialog.component";
import {MapAddress, Position} from '../model/mapAddress.model'
import {MapService} from "../service/map.service";
import {firstValueFrom, lastValueFrom} from "rxjs";
import {Drive, Stop} from "../model/drive.model";


L.Icon.Default.imagePath = 'assets/';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  private map!: L.Map;

  private stops: MapAddress[] = [{} as MapAddress, {} as MapAddress, {} as MapAddress, {} as MapAddress, {} as MapAddress];
  private finalStopsOrder: Stop[] = []

  @ViewChild('address1') addr1: any;
  @ViewChild('address2') addr2: any;
  private allValid: boolean = false;

  public nextPage:boolean = false;
  private drive:Drive = {} as Drive;

  private route:any = undefined

  public addressChanged(newAddress: any, addressNum: number) {
    this.nextPage = false
    this.removeMarker(addressNum)
    this.stops[addressNum] = newAddress;
    if (addressNum === 0) {
      this.addMarker(addressNum, newAddress.position, "Pocenta stanica")
    } else if (addressNum === 4) {
      this.addMarker(addressNum, newAddress.position, "Krajnja stanica")
    } else {
      this.addMarker(addressNum, newAddress.position, "Stanica broj" + addressNum)
    }
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
    if (result === 'duration' || result === 'distance') {
      var n = this.finalStopsOrder.length
      var combinations = this.mapService.permute(n - 2)//1->n-1
      const matrix$ = this.mapService.orderStops(this.getCoordinates(), result);
      var matrix: Array<Array<number>> = await firstValueFrom(matrix$);
      var results: number[] = this.mapService.getRouteSums(matrix, combinations, n)
      this.finalStopsOrder = this.mapService.getBestRouteCombination(results, combinations, this.finalStopsOrder)
    }
    this.showRoutesPathSame()

  }

  //lng, lat
  getCoordinates():[number, number][]{
    var coord:[number, number][] = []
    this.finalStopsOrder.forEach(el => {
      coord.push([el.position.lng, el.position.lat])
    })
    return coord
  }

  getStops(){
    for(let i = 0; i< this.stops.length;i++){
      if(this.stops[i].position !== undefined){
        var s = {} as Stop;
        var pos = {} as Position;
        pos.lat = this.stops[i].position.lat;
        pos.lng = this.stops[i].position.lng;
        s.position = pos
        s.address = this.stops[i].address
        s.name = this.stops[i].name
        this.finalStopsOrder.push(s)
      }
    }
  }

  isValid(e: any) {
    this.allValid = e;
  }

  showRoutesPathSame() {
    this.mapService.showRoute(this.getCoordinates()).subscribe((res) => {
      this.drive.distance = res.features[0].properties.summary.distance
      this.drive.duration = res.features[0].properties.summary.duration
      this.drive.stops = this.finalStopsOrder
      // L.geoJson(res).addTo(this.map)
      this.map.fitBounds(L.geoJson(res).getBounds())
      this.map.addLayer(L.geoJson(res))
    })

  }


  showRoute(){
    this.addr1.formsubmit()
    this.addr2.formsubmit()
    if(this.allValid) {
      this.getStops()
      // if (this.service.isLoggedIn()) {
      if (true){
        if(this.finalStopsOrder.length > 2)
          this.openRouteDialog()
        else this.showRoutesPathSame()
      } else {
        this.showRoutesPathSame()
      }
      this.nextPage = true;
    }
    else
      this.mapService.openErrorDialog("Adrese nisu unete.")
  }

  openNextPage(){
    if(this.drive.stops !== undefined){
      //cena je tip_vozila + km*120 pise u specifikaciji
      this.drive.price = this.drive.distance*120
    }
  }


}

