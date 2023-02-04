import {Component, OnInit} from '@angular/core';
import * as L from "leaflet";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {Vehicle} from "../model/Vehicle";
import {Ride} from "../model/Ride";
import {AppService} from "../app.service";
import {DriversAccount, DriverStatus} from "../model/driversAccount.model";
import {Stop} from '../model/stop.model';
import {DatePipe} from "@angular/common";
import {MatDialog} from '@angular/material/dialog';
import {
  CancelDriveReasonDialogComponent
} from "../dialog-template/cancel-drive-reason-dialog/cancel-drive-reason-dialog.component";
import {Drive} from "../model/drive.model";

L.Icon.Default.imagePath = 'assets/';


@Component({
  selector: 'app-rides-driver',
  templateUrl: './rides-driver.component.html',
  styleUrls: ['./rides-driver.component.css']
})
export class RidesDriverComponent implements OnInit {

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
  status = '';
  nextStops: Stop[] = []
  nextDate: string = ''
  stops: Stop[] = []
  arrived:boolean=true
  drive: Drive | undefined
  hideStartButton: boolean = false;

  constructor(private appService: AppService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.appService.getDriverStatus().subscribe((res: DriversAccount) => {
      this.status = res.driverStatus
    })

    this.appService.getCurrentRide().subscribe((res: any) => {
      this.drive = res;
      this.stops = res.stops
      let json: any = JSON.parse(res.routeJSON)
      this.appService.getDriverCar().subscribe((car: any) => {
        let geoLayerRouteGroup: L.LayerGroup = new L.LayerGroup();
        let color = Math.floor(Math.random() * 16777215).toString(16);
        let routeLayer = L.geoJSON(json['features'][0]['geometry']);
        routeLayer.setStyle({color: `#${color}`});
        routeLayer.addTo(geoLayerRouteGroup);
        this.ride[res.id] = geoLayerRouteGroup;
        let markerLayer = L.marker([car.latitude,car.longitude], {
          icon: L.icon({
            iconUrl: 'assets/red.png',
            iconSize: [20, 20],
            iconAnchor: [10, 10],
          }),
        })
        markerLayer.addTo(geoLayerRouteGroup);
        this.vehicle[res.driver.car.id] = markerLayer;
        this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
      })
      //sve ovde
    })
    this.appService.getFirstFutureRide().subscribe((res: any) => {
      if (res.stops == undefined) return
      var pipe = new DatePipe('sr-RS');
      var myFormattedDate = pipe.transform(res.date, 'dd-MM-yyyy HH:mm');
      if (myFormattedDate)
        this.nextDate = myFormattedDate;
      this.nextStops = res.stops;
    })
    this.makeAllRequests().then(r => {

      this.initializeWebSocketConnection();
    });

  }

  async makeAllRequests() {
    await this.appService.getDriverCar().subscribe((res: Vehicle) => {
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
    this.stompClient.subscribe('/map-updates/new-ride', (message: { body: string }) => {
      let ride: Ride = JSON.parse(message.body);
      if (ride.vehicle.id != this.id) return;
      this.mainGroup = []
      let geoLayerRouteGroup: L.LayerGroup = new L.LayerGroup();
      let color = Math.floor(Math.random() * 16777215).toString(16);
      for (let step of JSON.parse(ride.routeJSON)['routes'][0]['legs'][0]['steps']) {
        let routeLayer = L.geoJSON(step.geometry);
        routeLayer.setStyle({color: `#${color}`});
        routeLayer.addTo(geoLayerRouteGroup);
        this.ride[ride.id] = geoLayerRouteGroup;
      }
      let markerLayer = L.marker([ride.vehicle.latitude, ride.vehicle.longitude], {
        icon: L.icon({
          iconUrl: 'assets/green.png',
          iconSize: [20, 20],
          iconAnchor: [10, 10],
        }),
      });
      markerLayer.addTo(geoLayerRouteGroup);
      this.vehicle[ride.vehicle.id] = markerLayer;
      this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
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
    });
    this.stompClient.subscribe('/map-updates/delete-ride', (message: { body: any }) => {
      let rideId = JSON.parse(message.body).rideId;
      this.mainGroup = this.mainGroup.filter((lg: L.LayerGroup) => lg !== this.ride[rideId]);
      delete this.ride[rideId];
    });
  }

  onMapReady(map: L.Map) {
    this.map = map
  }

  goToNewStartLocation() {
    this.appService.startNextRide().subscribe((res: Stop[]) => {
      this.stops = res
      this.status = 'GOING_TO_LOCATION'
    })
    this.getFirstFutureRide()
  }

  startRide() {
    this.appService.startRide().subscribe((res: any) => {
      this.status = 'BUSY'
      this.appService.openErrorDialog(res);
      this.hideStartButton = true;
    })
  }

  endRide() {
    this.appService.endRide().subscribe((res: any) => {
      this.stops = []
      this.status = 'AVAILABLE'
      this.appService.openErrorDialog(res);
    })
    this.getFirstFutureRide()
  }

  getFirstFutureRide() {
    this.appService.getFirstFutureRide().subscribe((res: any) => {
      if (res.stops == undefined) return
      var pipe = new DatePipe('sr-RS');
      var myFormattedDate = pipe.transform(res.date, 'dd-MM-yyyy HH:mm');
      if (myFormattedDate)
        this.nextDate = myFormattedDate;
      this.nextStops = res.stops;
    })
  }

  cancelRide() {
    const dialogRef = this.dialog.open(CancelDriveReasonDialogComponent, {
      data: {reason: ''},
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result !== '' && result !== undefined) {
        this.appService.cancelRide(result).subscribe((res:any) => {
          this.appService.openErrorDialog("Voznja otkazana.");
          this.stops = []
          this.status = 'AVAILABLE'
        })
      }
      else if(result !== undefined){
        this.appService.openErrorDialog("Razlog nije unet. Voznja nece biti otkazana.");
      }
    });

  }

  notifyPassengers() {
    this.appService.notifyPassengers().subscribe((res:any) => {
      this.arrived = !this.arrived;
      this.hideStartButton=true;
    })
  }

}
