import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
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

 @Input() participants: Passenger[]=[];

 @Output() setSplitEvent= new EventEmitter<boolean>();

 disableSelect = new FormControl(false);

 setSplit(e:any){
  this.setSplitEvent.emit(this.disableSelect.value)
 }

}
