import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DriveReservationForm, PriceStart } from '../model/driveReservationForm.model';
import { Passenger } from '../model/passenger.model';

@Component({
  selector: 'app-drive-participants',
  templateUrl: './drive-participants.component.html',
  styleUrls: ['./drive-participants.component.css']
})
export class DriveParticipantsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  @Input() starting_price: PriceStart = PriceStart.seats5;

 @Input() drive: DriveReservationForm ={
   stops: [],
   distance: 0,
   duration: 0,
   price: 0,
   passengers: [],
   seats: 0,
   baby: 0,
   babySeats: 0,
   pets: 0,
   owner: null,
   routeJSON: {},
   splitBill: false,
   date: '',
   ownerDebit: 0
 }

 @Output() setSplitEvent= new EventEmitter<boolean>();

 disableSelect = new FormControl(false);

 setSplit(e:any){
  this.setSplitEvent.emit(this.disableSelect.value)
 }

}
