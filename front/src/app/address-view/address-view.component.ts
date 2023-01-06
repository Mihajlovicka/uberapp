import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MapAddress} from "../model/mapAddress.model";

@Component({
  selector: 'app-address-view',
  templateUrl: './address-view.component.html',
  styleUrls: ['./address-view.component.css']
})
export class AddressViewComponent{

  @Input() address = {} as MapAddress;
  @Output() remove = new EventEmitter<MapAddress>();

  constructor() { }

  removeAddress(){
    this.remove.emit()
  }

}
