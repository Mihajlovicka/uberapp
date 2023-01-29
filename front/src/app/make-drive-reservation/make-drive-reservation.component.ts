import { Component, Input, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';

import L from 'leaflet';
import { Drive } from '../model/drive.model';
import { MatStepper } from '@angular/material/stepper';

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

  currentDrive: Drive = {
    stops: [],
    distance: 2,
    duration: 0,
    price: 0,
    clients: [],
    seats: 5,
    baby: 0,
    babySeats:0,
    pets:0,
    owner: null
  }

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


  setDriveValues(drive: Drive){
    this.currentDrive.distance = drive.distance;
    this.currentDrive.duration = drive.duration;
    this.currentDrive.price = drive.price;
    this.currentDrive.seats = drive.seats;
  
    console.log(this.currentDrive);
  }


}
