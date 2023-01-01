import {Component, OnInit, ElementRef, Output, ViewChild} from '@angular/core';
import * as Leaflet from 'leaflet';
import H from '@here/maps-api-for-javascript';
import { Address } from '../address-item/address-item.component';
import {UserRegistrationService} from "../user-registration.service";

Leaflet.Icon.Default.imagePath = 'assets/';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  private map!: Leaflet.Map;

  private stops:Address[] = [{} as Address, {} as Address, {} as Address, {} as Address, {} as Address];

  @ViewChild('address1') addr1:any;
  @ViewChild('address2') addr2:any;
  private allValid:boolean = false;

  public addressChanged(newAddress:any, addressNum:number){
    console.log(this.stops)
    this.removeMarker(addressNum)
    this.stops[addressNum] = newAddress;
    if(addressNum === 0) {
      this.addMarker(addressNum, newAddress.position, "Pocenta stanica")
    }
    else if(addressNum === 4) {
      this.addMarker(addressNum, newAddress.position, "Krajnja stanica")
    }
    else{
      this.addMarker(addressNum, newAddress.position, "Stanica broj" + addressNum)
    }
    console.log(this.stops)
  }


  private initMap(): void {
    this.map = Leaflet.map('map');

    var title = Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
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

  constructor(private el:ElementRef,public service:UserRegistrationService) {
  }

  ngOnInit(): void {
    this.initMap()
  }

  addMarker(number: number, coords: any, message: string) {
    let iconOptions = {
      icon: Leaflet.icon({
        iconUrl: "assets/startMark.png",
        iconSize: [30, 30]
      })
    }
    var marker = Leaflet.marker([coords.lat, coords.lng], iconOptions).bindPopup(message);
    this.map.addLayer(marker);
    this.map.panTo({lat: coords.lat, lng: coords.lng});
    this.setMarker(number, marker)
  }


  removeMarker(i: number) {
    if(this.stops[i].marker !== undefined)
      if(this.map.hasLayer(this.stops[i].marker))
        this.map.removeLayer(this.stops[i].marker)
  }

  updateMarker(i: number, lat: number, lng: number) {
    this.stops[i].marker.setLatLng({lat, lng});
  }

  setMarker(i:number, marker:any){
    this.stops[i].marker = marker
  }
//validator na klik dalje proveriti samo da li su start i kraj ispravni
  makeAdditionalVisible(){
    var elem = this.el.nativeElement.querySelectorAll('.additionalAddresses');
    elem.forEach((el:any) => {
      return el.classList.remove('additionalStopsHidden');
    });
    elem = this.el.nativeElement.querySelector('#additionalButton');
    elem.classList.add('additionalButtonHidden');
  }

  save(){
    this.addr1.formsubmit()
    this.addr2.formsubmit()
    if(this.allValid){
      console.log("Gotojoooo")
    }
  }

  isValid(e:any){
    this.allValid = e;
  }
}


export const apikey = '4p_yH-oIdvknqcL_qWc-67Qub-dzK1i9CfagdSkB6s0';
export const app_id = 'wxW8BsS6K2RLXNO0HmsWJg';
export const app_code = 'lVVj72KFDijaAV7MSAXtXFsruhRejqV4S_0402zNzSE-7MQSfYL0WbzkGQQRaVSDi4bTGRWNOyuaWEtESH2omA'


