import { Component, Input, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';

import L from 'leaflet';
import { DriveReservationForm, PriceStart} from '../model/driveReservationForm.model';
import { MatStepper } from '@angular/material/stepper';
import {DatePipe, formatDate} from "@angular/common";

@Component({
  selector: 'app-make-drive-reservation',
  templateUrl: './make-drive-reservation.component.html',
  styleUrls: ['./make-drive-reservation.component.css'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
})






export class MakeDriveReservationComponent implements OnInit {

  currentDrive: DriveReservationForm = {
    stops: [],
    distance: 2,
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
    date: '',
    ownerDebit: 0
  }

  starting_price: number = 0;

  @ViewChild('stepper') stepper: MatStepper|undefined;






  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });

  constructor(private _formBuilder: FormBuilder) { }

  ngOnInit(): void {

  }

  goToNext(){
    console.log(this.currentDrive)
    if(this.stepper !== undefined){
      this.stepper.next();
    }

  }

  setStartPrice(){
    if(this.currentDrive.seats === 4)this.starting_price=PriceStart.seats4;
    if(this.currentDrive.seats === 5)this.starting_price=PriceStart.seats5;
    if(this.currentDrive.seats === 6)this.starting_price=PriceStart.seats6;
    if(this.currentDrive.seats === 7)this.starting_price=PriceStart.seats7;
    if(this.currentDrive.seats === 8)this.starting_price=PriceStart.seats8;
    if(this.currentDrive.seats === 9)this.starting_price=PriceStart.seats9;
  }

  setDriveValues(drive: DriveReservationForm){
    this.currentDrive.distance = drive.distance;
    this.currentDrive.duration = drive.duration;
    this.currentDrive.price = drive.price;
    this.currentDrive.seats = drive.seats;

    this.currentDrive = drive;

    this.setStartPrice();
    console.log(this.currentDrive);
  }


}
