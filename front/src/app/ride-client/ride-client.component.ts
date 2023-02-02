import { Component, OnInit } from '@angular/core';
import * as L from "leaflet";
import {Stop} from "../model/stop.model";
import {AppService} from "../app.service";
import {DriversAccount} from "../model/driversAccount.model";
import {DatePipe} from "@angular/common";
import {Vehicle} from "../model/Vehicle";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {Ride} from "../model/Ride";
L.Icon.Default.imagePath = 'assets/';


@Component({
  selector: 'app-ride-client',
  templateUrl: './ride-client.component.html',
  styleUrls: ['./ride-client.component.css']
})
export class RideClientComponent implements OnInit {

  private map!: L.Map;

  options = {
    layers: [
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        // attribution: '...',
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      }),
    ],
    zoom: 14,
    center: L.latLng(45.253434, 19.831323),
  };
  id: number = -1;
  vehicle: any = {};
  ride: any = {};
  mainGroup: L.LayerGroup[] = [];
  private stompClient: any;
  stops: Stop[] = []
  timeLeft:number = 0;


  constructor(private appService: AppService) {
  }

  ngOnInit(): void {

    this.appService.getClientCurrentDrive().subscribe((res: Stop[]) => {
      this.stops = res;
    })
    this.makeAllRequests().then(r => {
      this.initializeWebSocketConnection();
    });

  }

  async makeAllRequests() {
    await this.appService.getClientCurrentCar().subscribe((res: Vehicle) => {
      this.id = res.id
    })
  }

  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe('/map-updates/update-car-position', (message: { body: string }) => {
      let vehicle: Vehicle = JSON.parse(message.body);
      if (this.id == vehicle.id) {
        let existingVehicle = this.vehicle[this.id];
        existingVehicle.setLatLng([vehicle.latitude, vehicle.longitude]);
        existingVehicle.update();
      }
    });
    this.stompClient.subscribe('/map-updates/new-existing-ride', (message: { body: string }) => {
      let ride: Ride = JSON.parse(message.body);
      if (ride.vehicle.id != this.id) return;
      let geoLayerRouteGroup: L.LayerGroup = new L.LayerGroup();
      let color = Math.floor(Math.random() * 16777215).toString(16);
      let json = JSON.parse(ride.routeJSON);
      let routeLayer = L.geoJSON(json['features'][0]['geometry']);
      routeLayer.setStyle({color: `#${color}`});
      routeLayer.addTo(geoLayerRouteGroup);
      this.ride[ride.id] = geoLayerRouteGroup;
      let markerLayer = L.marker([ride.vehicle.latitude, ride.vehicle.longitude], {
        icon: L.icon({
          iconUrl: 'assets/red.png',
          iconSize: [20, 20],
          iconAnchor: [10, 10],
        }),
      })
      markerLayer.addTo(geoLayerRouteGroup);
      this.vehicle[ride.vehicle.id] = markerLayer;
      this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
    });
    this.stompClient.subscribe('/map-updates/ended-ride', (message: { body: string }) => {
      let ride: Ride = JSON.parse(message.body);
      this.mainGroup = this.mainGroup.filter((lg: L.LayerGroup) => lg !== this.ride[ride.id]);
      delete this.ride[ride.id];
      this.stops = []
    });
    this.stompClient.subscribe('/map-updates/update-time-left', (message: { body: string }) => {
      let time:any = JSON.parse(message.body)
      if(time['id'] == this.id)
        this.timeLeft = Number((time['time']*60).toFixed(2))
    });
  }

  onMapReady(map: L.Map) {
    this.map = map
  }

}
