import { Component, Input, OnInit } from '@angular/core';
import { DriveReservationForm, PriceStart } from '../model/driveReservationForm.model';

@Component({
  selector: 'app-drive-bill',
  templateUrl: './drive-bill.component.html',
  styleUrls: ['./drive-bill.component.css']
})
export class DriveBillComponent implements OnInit {

  constructor() { }

  @Input() starting_price: PriceStart = PriceStart.seats5;

  @Input() numOfPartitions: number = 0;

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

  ngOnInit(): void {
  }

}
