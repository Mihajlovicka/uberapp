import {Component, OnInit, ElementRef, Output, ViewChild, ChangeDetectorRef, AfterViewInit, Input, EventEmitter} from '@angular/core';
import * as L from 'leaflet';
import {UserRegistrationService} from "../user-registration.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatDialog} from '@angular/material/dialog';
import {RoutesDialogComponent} from "../dialog-template/routes-dialog/routes-dialog.component";
import {MapAddress, Position} from '../model/mapAddress.model'
import {MapService} from "../service/map.service";
import {firstValueFrom, lastValueFrom, Observable} from "rxjs";
import {Drive} from "../model/drive.model";
import {Stop} from '../model/stop.model';

import {FormBuilder, Validators} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';



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
export class MapComponent implements AfterViewInit {


  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });

  private map!: L.Map;

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

  @Input() drive:Drive = {
    stops: [],
    distance: 0,
    duration: 0,
    price: 0,
    clients: [],
    seats: 5,
    baby: 0,
    babySeats:0,
    pets:0,
    owner: null
  };

  @Output() setDrive = new EventEmitter<Drive>();

  @Output() nextStep = new EventEmitter();



  public draggingIndex: number = -1;

  private title = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  })

  constructor(private el: ElementRef,
              public service: UserRegistrationService,
              public dialog: MatDialog,
              private mapService: MapService,
              private cdr: ChangeDetectorRef, private _formBuilder: FormBuilder) {
  }

  ngAfterViewInit(): void {
    //console.log(this.drive)
    this.initMap()
    this.addCurrentLocation()
  }

  public addressChanged(newAddress: any, addressNum: string = '') {
    this.nextPage = false
    if (addressNum === 'start') {
      if (this.start) {
        this.removeMarker(0)
      }
      if(newAddress)
      {this.start = newAddress
      this.addMarker(0, newAddress.position)}
    } else if (addressNum === 'end') {
      if (this.end) {
        this.removeMarker(1)
      }
      if(newAddress) {
        this.end = newAddress
        this.addMarker(1, newAddress.position)
      }
    } else {
      this.addr1.formsubmit()
      this.addr2.formsubmit()
      if (this.allValid && newAddress && newAddress.position !== undefined) {
        this.stops.push(newAddress)
        this.cdr.detectChanges()
        this.addMarker(this.stops.length - 1, newAddress.position)
      }
    }

  }

  public removeAddressFromAdditional(index: number) {
    this.removeMarker(index)
    this.stops.splice(index, 1)
    this.cdr.detectChanges()
  }

  private initMap(): void {
    this.map = L.map('map');
    this.map.addLayer(this.title);
    this.map.setView([45.251787, 19.837155], 19);
  }

  private addCurrentLocation() {
    navigator.geolocation.getCurrentPosition(position => {
      const {latitude, longitude} = position.coords;
      this.map.panTo({lat: latitude, lng: longitude});
    }, () => {
      console.log("error")
    }, {
      enableHighAccuracy: true,
      maximumAge: 30000,
      timeout: 27000
    })
  }


  addMarker(number: number, coords: any) {
    let iconOptions = {
      icon: L.icon({
        iconUrl: "assets/startMark.png",
        iconSize: [30, 30],
        iconAnchor: [15, 30],
      })
    }
    var marker = L.marker([coords.lat, coords.lng], iconOptions);
    this.map.addLayer(marker);
    this.map.panTo({lat: coords.lat, lng: coords.lng});
    if (number === 0 && this.start)
      this.start.marker = marker
    else if (number === 1 && this.end)
      this.end.marker = marker
    else
      this.stops[this.stops.length - 1].marker = marker
  }


  removeMarker(i: number) {
    var marker: any;
    if (i === 0 && this.start)
      marker = this.start.marker
    else if (i === 1 && this.end)
      marker = this.end.marker
    else marker = this.stops[i].marker

    this.map.removeLayer(marker)
  }

  restoreMarkers() {
    if (this.start)
      this.addMarker(0, this.start.position)
    if (this.end)
      this.addMarker(1, this.end.position)
    this.stops.forEach(el => this.addMarker(3, el.position))

  }

  removePath() {
    if (this.route !== undefined) {
      this.map.eachLayer((layer) => {
        this.map.removeLayer(layer);
      });
      this.map.addLayer(this.title);
      this.restoreMarkers()
    }
  }

  dialogOC(): Promise<any> {
    const dialogRef = this.dialog.open(RoutesDialogComponent);
    return dialogRef.afterClosed().toPromise().then((value: string) => {
      return value
    })
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
      this.reorderAdditionalStops()
    }
    this.showRoutesPathSame()

  }

  //lng, lat
  getCoordinates(): [number, number][] {
    var coord: [number, number][] = []
    this.finalStopsOrder.forEach(el => {
      coord.push([el.position.lng, el.position.lat])
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
    this.mapService.showRoute(this.getCoordinates()).subscribe((res) => {
      this.drive.distance = res.features[0].properties.summary.distance
      this.drive.duration = res.features[0].properties.summary.duration
      this.drive.stops = this.getRealStops()
      this.route = L.geoJson(res).addTo(this.map)
      this.map.fitBounds(L.geoJson(res).getBounds())
      this.route = res
    })
  }

  showRoute() {
    this.addr1.formsubmit()
    this.addr2.formsubmit()
    if (this.allValid) {
      this.getAllStops()
      if (this.service.isLoggedIn()) {
        if (this.finalStopsOrder.length >= 2)
          this.openRouteDialog()
      }
      this.showRoutesPathSame()
      this.nextPage = true;
    } else
      this.mapService.openErrorDialog("Adrese nisu unete.")
  }

  getRealStops() {
    var s: Stop[] = []
    if (this.start)
      s.push(new Stop(this.start.address, this.start.position, this.start.name))
    this.finalStopsOrder.forEach(el => {
      s.push(new Stop(el.address, el.position, el.name))
    })
    if (this.end)
      s.push(new Stop(this.end.address, this.end.position, this.end.name))
    return s
  }

  openNextPage() {
    if (this.drive.stops !== undefined) {
      //cena je tip_vozila + km*120 pise u specifikaciji

      this.drive.price = this.drive.distance * 120 / 1000;

      //console.log(this.drive);

      

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

}

