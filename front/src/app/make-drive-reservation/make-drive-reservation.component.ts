import { Component, Input, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';

import L from 'leaflet';
import { DriveReservationForm, PriceStart} from '../model/driveReservationForm.model';
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
    var driveDate =  new Date();
    var varDate='';
    if(driveDate.toString().split(" ")[1] === 'Jan'){
      varDate = driveDate.toString().split(" ")[2] + "-01-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }
    if(driveDate.toString().split(" ")[1] === 'Feb'){
      varDate = driveDate.toString().split(" ")[2] + "-02-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Mar'){
      varDate = driveDate.toString().split(" ")[2] + "-03-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Apr'){
      varDate = driveDate.toString().split(" ")[2] + "-04-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'May'){
      varDate = driveDate.toString().split(" ")[2] + "-06-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Jun'){
      varDate = driveDate.toString().split(" ")[2] + "-06-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Jul'){
      varDate = driveDate.toString().split(" ")[2] + "-07-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Avg'){
      varDate = driveDate.toString().split(" ")[2] + "-08-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Sep'){
      varDate = driveDate.toString().split(" ")[2] + "-12-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Oct'){
      varDate = driveDate.toString().split(" ")[2] + "-10-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Nov'){
      varDate = driveDate.toString().split(" ")[2] + "-11-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

    if(driveDate.toString().split(" ")[1] === 'Dec'){
      varDate = driveDate.toString().split(" ")[2] + "-12-" + driveDate.toString().split(" ")[3] +" "+ driveDate.toString().split(" ")[4].split(":")[0]+":"+driveDate.toString().split(" ")[4].split(":")[1]
    }

   this.currentDrive.date = varDate;

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
    this.currentDrive = drive;

    this.setStartPrice();
    
    console.log(this.currentDrive);
  }


}
