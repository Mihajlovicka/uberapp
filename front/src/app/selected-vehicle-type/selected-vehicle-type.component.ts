import { Component, Input, OnInit } from '@angular/core';
import { DriveReservationForm } from '../model/driveReservationForm.model';

@Component({
  selector: 'app-selected-vehicle-type',
  templateUrl: './selected-vehicle-type.component.html',
  styleUrls: ['./selected-vehicle-type.component.css']
})
export class SelectedVehicleTypeComponent implements OnInit {

  constructor() { }

  @Input() drive:DriveReservationForm = {
    stops: [],
    distance: 0,
    duration: 0,
    price: 0,
    passengers: [],
    seats: 5,
    baby: 0,
    babySeats:0,
    pets:0,
    owner: null,
    routeJSON:{},
    //driver:null,
    //driveStatus: DriveStatus.PASSENGERS_WAITING,
    splitBill: false,
    date: ""
  };

  ngOnInit(): void {
  }

}
