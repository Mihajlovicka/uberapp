import {Component, OnInit, ElementRef, Output, ViewChild, ChangeDetectorRef, AfterViewInit, Input, EventEmitter} from '@angular/core';
import * as L from 'leaflet';
import {UserAuthService} from "../service/user-auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatDialog} from '@angular/material/dialog';
import {RoutesDialogComponent} from "../dialog-template/routes-dialog/routes-dialog.component";
import {MapAddress, Position} from '../model/mapAddress.model'
import {MapService} from "../service/map.service";
import {firstValueFrom, lastValueFrom, Observable} from "rxjs";
import {DriveReservationForm} from "../model/driveReservationForm.model";
import {Stop} from '../model/stop.model';
import {Vehicle} from '../model/Vehicle';
import {Ride} from '../model/Ride';

import {FormBuilder, Validators} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {DriverStatus} from "../model/driversAccount.model";
import {AppService} from "../app.service";
import {Router} from "@angular/router";
import {
  FavoriteRoutesDialogComponent
} from "../dialog-template/favorite-routes-dialog/favorite-routes-dialog.component";
import {FavoriteRide} from "../model/favoriteRide.model";



L.Icon.Default.imagePath = 'assets/';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
})
export class MapComponent implements AfterViewInit, OnInit {


  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });

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
  vehicles: any = {};
  rides: any = {};
  mainGroup: L.LayerGroup[] = [];
  private stompClient: any;

  private start: MapAddress | undefined
  private end: MapAddress | undefined
  public stops: MapAddress[] = [];
  private finalStopsOrder: MapAddress[] = []

  @ViewChild('address1') addr1: any;
  @ViewChild('address2') addr2: any;
  private allValid: boolean = true;

  public nextPage: boolean = false;
  public showAdditional: boolean = false


  private route: any = undefined
  public showSummary: boolean = false
  public openDialog: boolean = false
  public routeChosen: boolean = false
  private optimalRouteLayer: any = undefined

  @Input() drive:DriveReservationForm = {
    stops: [],
    distance: 0,
    duration: 0,
    price: 0,
    passengers: [],
    seats: 5,
    baby: 0,
    babySeats: 0,
    pets: 0,
    owner: null,
    routeJSON: {},
    //driver:null,
    //driveStatus: DriveStatus.PASSENGERS_WAITING,
    splitBill: false,
    date: "",
    ownerDebit: 0
  };

  @Output() setDrive = new EventEmitter<DriveReservationForm>();

  @Output() nextStep = new EventEmitter();



  public draggingIndex: number = -1;

  private addressesLayer: L.LayerGroup = new L.LayerGroup();
  private routeLayer: L.LayerGroup = new L.LayerGroup();


  constructor(private el: ElementRef,
              public service: UserAuthService,
              public dialog: MatDialog,
              private mapService: MapService,
              private cdr: ChangeDetectorRef, private _formBuilder: FormBuilder,
              private appService:AppService,
              private router: Router) {
  }

  ngOnInit(): void {
    // this.initializeWebSocketConnection();
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
      let existingVehicle = this.vehicles[vehicle.id];
      existingVehicle.setLatLng([vehicle.longitude, vehicle.latitude]);
      existingVehicle.update();
    });
    this.stompClient.subscribe('/map-updates/new-ride', (message: { body: string }) => {
      let ride: Ride = JSON.parse(message.body);
      let geoLayerRouteGroup: L.LayerGroup = new L.LayerGroup();
      this.rides[ride.id] = geoLayerRouteGroup;
      let markerLayer = L.marker([ride.vehicle.longitude, ride.vehicle.latitude], {
        icon: L.icon({
          iconUrl: 'assets/green.png',
          iconSize: [20, 20],
          iconAnchor: [10, 10],
        }),
      }).bindPopup('Vozilo ' + ride.vehicle.licensePlateNumber);
      markerLayer.addTo(geoLayerRouteGroup);
      this.vehicles[ride.vehicle.id] = markerLayer;
      this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
    });
    this.stompClient.subscribe('/map-updates/new-existing-ride', (message: { body: string }) => {
      let ride: Ride = JSON.parse(message.body);
      let geoLayerRouteGroup: L.LayerGroup = new L.LayerGroup();
      this.rides[ride.id] = geoLayerRouteGroup;
      let markerLayer = L.marker([ride.vehicle.longitude, ride.vehicle.latitude], {
        icon: L.icon({
          iconUrl: 'assets/red.png',
          iconSize: [20, 20],
          iconAnchor: [10, 10],
        }),
      }).bindPopup('Vozilo ' + ride.vehicle.licensePlateNumber);
      markerLayer.addTo(geoLayerRouteGroup);
      this.vehicles[ride.vehicle.id] = markerLayer;
      this.mainGroup = [...this.mainGroup, geoLayerRouteGroup];
    });
    this.stompClient.subscribe('/map-updates/ended-ride', (message: { body: string }) => {
      let ride: Ride = JSON.parse(message.body);
      this.mainGroup = this.mainGroup.filter((lg: L.LayerGroup) => lg !== this.rides[ride.id]);
      delete this.vehicles[ride.vehicle.id];
      delete this.rides[ride.id];
    });
    this.stompClient.subscribe('/map-updates/delete-ride', (message: { body: any }) => {
      let carId = JSON.parse(message.body).carId;
      let rideId = JSON.parse(message.body).rideId;
      this.mainGroup = this.mainGroup.filter((lg: L.LayerGroup) => lg !== this.rides[rideId]);
      delete this.vehicles[carId];
      delete this.rides[rideId];
    });
  }

  ngAfterViewInit(): void {

    //console.log(this.drive)
  
    //this.addCurrentLocation()

    // this.addCurrentLocation()
  }

  onMapReady(map: L.Map) {
    this.map = map
    this.mainGroup = [this.addressesLayer, this.routeLayer];
  }

  public addressChanged(newAddress: any, addressNum: string = '') {
    if(this.start === undefined && this.end=== undefined)
      if(this.routeChosen) this.searchAgain()
    else this.removePath()
    this.showSummary = false
    this.openDialog = false
    this.optimalRouteLayer = undefined
    if (addressNum === 'start') {
      if (this.start) {
        this.removeMarker(0)
      }
      if (newAddress) {
        this.start = newAddress
        this.addMarker(0, newAddress.location)
      }
    } else if (addressNum === 'end') {
      if (this.end) {
        this.removeMarker(1)
      }
      if (newAddress) {
        this.end = newAddress
        this.addMarker(1, newAddress.location)
      }
    } else {
      this.addr1.formsubmit()
      this.addr2.formsubmit()
      if (this.allValid && newAddress && newAddress.location !== undefined) {
        this.stops.push(newAddress)
        this.cdr.detectChanges()
        this.addMarker(this.stops.length - 1, newAddress.location)
      }
    }

  }

  public removeAddressFromAdditional(index: number) {
    this.removeMarker(index)
    this.stops.splice(index, 1)
    this.cdr.detectChanges()
  }

  private addCurrentLocation() {
    navigator.geolocation.getCurrentPosition(position => {
      const {latitude, longitude} = position.coords;
      this.addMarkerToMap({lat: latitude, lng: longitude})
    }, () => {
      console.log("error")
    }, {
      enableHighAccuracy: true,
      maximumAge: 30000,
      timeout: 27000
    })
  }


  addMarker(number: number, coords: any) {
    this.showSummary = false
    this.nextPage = false
    this.openDialog = false
    var marker = this.addMarkerToMap(coords);
    if (number === 0 && this.start)
      this.start.marker = marker
    else if (number === 1 && this.end)
      this.end.marker = marker
    else
      this.stops[this.stops.length - 1].marker = marker
  }

  private addMarkerToMap(coords: any): L.Marker {
    let iconOptions = {
      icon: L.icon({
        iconUrl: "assets/startMark.png",
        iconSize: [30, 30],
        iconAnchor: [15, 15],
      })
    }
    var marker = L.marker([coords.latitude, coords.longitude], iconOptions);
    marker.addTo(this.addressesLayer);
    this.map.panTo({lat: coords.latitude, lng: coords.longitude});
    return marker
  }


  removeMarker(i: number) {
    var marker: any;
    if (i === 0 && this.start)
      marker = this.start.marker
    else if (i === 1 && this.end)
      marker = this.end.marker
    else marker = this.stops[i].marker

    this.addressesLayer.removeLayer(marker);
  }

  removePath() {
    this.routeLayer.eachLayer((ly) => this.routeLayer.removeLayer(ly))
  }

  dialogOC(): Promise<any> {
    const dialogRef = this.dialog.open(RoutesDialogComponent, {
      data: { showSameStopsOption: this.finalStopsOrder.length != 2 },
    });
    return dialogRef.afterClosed().toPromise().then((value: string) => {
      return value
    })
  }

  async openRouteDialog() {
    this.openDialog = false
    var result = await this.dialogOC()
    if (this.finalStopsOrder.length > 2 && (result === 'duration' || result === 'distance')) {
      var n = this.finalStopsOrder.length
      var combinations = this.mapService.permute(n - 2)//1->n-1
      const matrix$ = this.mapService.orderStops(this.getCoordinates(), result);
      var matrix: Array<Array<number>> = await firstValueFrom(matrix$);
      var results: number[] = this.mapService.getRouteSums(matrix, combinations, n)
      this.finalStopsOrder = this.mapService.getBestRouteCombination(results, combinations, this.finalStopsOrder)
      this.reorderAdditionalStops()
    }
    this.showRoutesPathSame()

  }

  //lng, lat
  getCoordinates(): [number, number][] {
    var coord: [number, number][] = []
    this.finalStopsOrder.forEach(el => {
      coord.push([el.location.longitude, el.location.latitude])
    })
    return coord
  }

  getAllStops() {
    this.finalStopsOrder = []
    if (this.start)
      this.finalStopsOrder.push(this.start)
    for (let i = 0; i < this.stops.length; i++) {
      this.finalStopsOrder.push(this.stops[i])
    }
    if (this.end)
      this.finalStopsOrder.push(this.end)
  }

  isValid(e: any) {
    this.allValid = e && this.allValid;
  }

  showRoutesPathSame() {
    this.removePath()
    this.showSummary = true
    this.nextPage = true
    this.mapService.showRoute(this.getCoordinates()).subscribe((res) => {
      let color = Math.floor(Math.random() * 16777215).toString(16);
      this.drive.distance = Number((res.features[0].properties.summary.distance / 1000).toFixed(2))
      this.drive.duration = Number((res.features[0].properties.summary.duration / 60).toFixed(2))
      this.drive.price =Number((this.drive.distance * 120).toFixed(2))
      this.drive.stops = this.finalStopsOrder
      this.drive.routeJSON = res
      let routeLayer = L.geoJSON(res.features[0].geometry)
        .bindPopup("Razdaljina: " + this.drive.distance + "km \nTrajanje: " + this.drive.duration + "min")
        .on('mouseover', function (e) {
          e.target.openPopup();
        },)
        .on('mouseout', function (e) {
          e.target.closePopup();
        });
      routeLayer.setStyle({color: `#${color}`, weight: 5});
      routeLayer.addTo(this.routeLayer);
      this.map.fitBounds(routeLayer.getBounds())
      this.optimalRouteLayer = routeLayer
      this.routeChosen = true
    })
  }

  showMultipleRoutesNotLoggedIn() {
    this.removePath()
    let coordinates: [number, number][] = this.getCoordinates()
    this.mapService.showMultipleRoutes(coordinates).subscribe((res) => {

      for (let route of res.features) {
        let color = Math.floor(Math.random() * 16777215).toString(16);
        let distance = Number((route.properties.summary.distance / 1000).toFixed(2))
        let duration = Number((route.properties.summary.duration / 60).toFixed(2))
        let routeLayer = L.geoJSON(route.geometry)
          .bindPopup("Razdaljina: " + distance + "km \nTrajanje: " + duration + "min")
          .on('mouseover', function (e) {
            e.target.openPopup();
          },)
          .on('mouseout', function (e) {
            e.target.closePopup();
          });
        routeLayer.setStyle({color: `#${color}`, weight: 5});
        routeLayer.addTo(this.routeLayer);
        this.map.fitBounds(routeLayer.getBounds())
        if (this.optimalRouteLayer === undefined) {
          this.drive.distance = distance
          this.drive.duration = duration
          this.drive.price = Number((distance * 120).toFixed(2))
          this.optimalRouteLayer = routeLayer
        } else {
          let new_price = distance * 120
          if (new_price < this.drive.price) {
            this.drive.distance = distance
            this.drive.duration = duration
            this.drive.price = Number((distance * 120).toFixed(2))
            this.optimalRouteLayer = routeLayer
          }
        }
      }
    })
  }

  getOptimalRoute() {
    this.showSummary = true
    if(this.optimalRouteLayer != undefined){
    this.routeLayer.eachLayer((ly) => {
      if (ly != this.optimalRouteLayer)
        this.routeLayer.removeLayer(ly)
    })
    this.map.fitBounds(this.optimalRouteLayer.getBounds())}
  }

  showRoute() {
    this.addr1.formsubmit()
    this.addr2.formsubmit()
    if (this.allValid) {
      this.getAllStops()
      if (this.service.isLoggedIn()) {
        this.showMultipleRoutesLoggedIn()
      } else {
        this.showMultipleRoutesNotLoggedIn()
      }
      this.nextPage = true
    } else
      this.mapService.openErrorDialog("Adrese nisu unete.")
  }

  showMultipleRoutesLoggedIn() {
    this.removePath()
    this.openDialog = true
    let coordinates: [number, number][] = this.getCoordinates()
    for (let i = 0; i < coordinates.length - 1; i++)
      this.mapService.showMultipleRoutes([coordinates[i], coordinates[i + 1]]).subscribe((res) => {
        for (let route of res.features) {
          let color = Math.floor(Math.random() * 16777215).toString(16);
          let distance = Number((route.properties.summary.distance / 1000).toFixed(2))
          let duration = Number((route.properties.summary.duration / 60).toFixed(2))
          let routeLayer = L.geoJSON(route.geometry)
            .bindPopup("Razdaljina: " + distance + "km \nTrajanje: " + duration + "min")
            .on('mouseover', function (e) {
              e.target.openPopup();
            },)
            .on('mouseout', function (e) {
              e.target.closePopup();
            });
          routeLayer.setStyle({color: `#${color}`, weight: 5});
          routeLayer.addTo(this.routeLayer);
          this.map.fitBounds(routeLayer.getBounds())
        }
      })
  }

  getRealStops() {
    var s: Stop[] = []
    this.finalStopsOrder.forEach(el => {
      s.push(new Stop(el.address, el.location, el.name))
    })
    return s
  }

  openNextPage() {
    if (this.drive.stops !== undefined) {
      //cena je tip_vozila + km*120 pise u specifikaciji

      this.drive.price = this.drive.distance * 120;

      //console.log(this.drive);
      let addresses:any = []
      this.finalStopsOrder.forEach((el) => {addresses.push({
        name:el.name,
        address:el.address,
        location:el.location
      })})

      this.drive.stops=addresses;
      this.drive.routeJSON = JSON.stringify(this.drive.routeJSON);
      

      this.setDrive.emit(this.drive);

      //emitovati za sl stranicu
      this.nextStep.emit();


    }
  }

  private reorderAdditionalStops() {
    this.stops = this.finalStopsOrder.slice(1, this.finalStopsOrder.length - 1)
  }

  private _reorderItem(fromIndex: number, toIndex: number): void {
    const itemToBeReordered = this.stops.splice(fromIndex, 1)[0];
    this.stops.splice(toIndex, 0, itemToBeReordered);
    this.draggingIndex = toIndex;
  }

  onDragStart(index: number): void {
    this.draggingIndex = index;
  }

  onDragEnter(index: number): void {
    if (this.draggingIndex !== index) {
      this._reorderItem(this.draggingIndex, index);
    }
  }

  onDragEnd(): void {
    this.draggingIndex = -1;
  }

  addToFavorites(){
    if(this.optimalRouteLayer != undefined){
      let addresses:any = []
      this.finalStopsOrder.forEach((el) => {addresses.push({
        name:el.name,
        address:el.address,
        location:el.location
      })})
      this.appService.addFavoriteRide({
        routeJSON:JSON.stringify(this.drive.routeJSON),
        realAddress:addresses,
      }).subscribe(() => {
        this.appService.openErrorDialog("Ruta uspesno dodata.");
      })
    }
  }

  showFavorites(){
    const dialogRef = this.dialog.open(FavoriteRoutesDialogComponent);
    dialogRef.afterClosed().subscribe((route: FavoriteRide) => {
      if(route==undefined) return
      if(this.routeChosen) this.searchAgain()
      this.showSummary = true
      this.nextPage = true
      this.routeChosen = true
      let color = Math.floor(Math.random() * 16777215).toString(16);
      let json = JSON.parse(route.routeJSON)
      this.drive.distance = Number((json.features[0].properties.summary.distance / 1000).toFixed(2))
      this.drive.duration = Number((json.features[0].properties.summary.duration / 60).toFixed(2))
      this.drive.price = Number((this.drive.distance * 120).toFixed(2))
      this.drive.stops = route.realAddress
      this.drive.routeJSON = json.features[0].geometry
      let routeLayer = L.geoJSON(json.features[0].geometry)
      routeLayer.setStyle({color: `#${color}`, weight: 5});
      routeLayer.addTo(this.routeLayer);
      this.map.fitBounds(routeLayer.getBounds())
      this.optimalRouteLayer = routeLayer
      this.drive.stops.forEach((el) => {this.addMarkerToMap(el.location)})
    })
  }

  searchAgain() {
    this.removePath()
    this.addressesLayer.eachLayer((ly) => this.addressesLayer.removeLayer(ly))
    this.routeChosen = false
    this.optimalRouteLayer = undefined
    this.drive = {
      stops: [],
      distance: 0,
      duration: 0,
      price: 0,
      passengers: [],
      seats:0,
      baby:0,
      babySeats:0,
      pets:0,
      owner:null,
      routeJSON:{},
      //driver:null,
      //driveStatus: DriveStatus.PASSENGERS_WAITING,
      splitBill:false,
      date:'',
      ownerDebit:0
    }
    this.start = undefined
    this.end = undefined
    this.finalStopsOrder = []
    this.showSummary = false
    this.nextPage = false
    this.openDialog = false
  }
}

