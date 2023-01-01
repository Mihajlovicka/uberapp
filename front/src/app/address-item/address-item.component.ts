import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import * as L from 'leaflet';

export interface Position {
  lat:number,
  lng:number
}

export interface Address {
  name: string;
  position: Position,
  address: string,
  number:string,
  marker:L.Marker
}

@Component({
  selector: 'app-address-item',
  templateUrl: './address-item.component.html',
  styleUrls: ['./address-item.component.css']
})
export class AddressItemComponent implements OnInit {
// @ts-ignore
  myControl = new FormControl<string | Address>('');
  options: Address[] = [];
  filteredOptions: Observable<Address[]> | undefined;

  @Input() addressText = '';
  @Input() removeRequired:boolean = false;
  @Input() showErrors:boolean = false;

  address: Address | undefined;
  @Output() addressChange = new EventEmitter<Address>();
  @Output() isValid = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
    if(!this.removeRequired){
      this.myControl.setValidators([Validators.required]);
      this.myControl.updateValueAndValidity();
    }
    for(let i = 0; i<4;i++){
      var pos = {} as Position;
      pos.lat = 45.2608651+i
      pos.lng = 19.8319339+i*2
      var address = {} as Address;
      address.name = i.toString()
      address.position = pos
      address.address = i.toString() + "l"
      this.options.push(address)
      this.filteredOptions = this.myControl.valueChanges.pipe(
        startWith(''),
        map(value => {
          const name = typeof value === 'string' ? value : value?.name;
          return name ? this._filter(name as string) : this.options.slice();
        }),
      );
    }
  }

  displayFn(user: Address): string {
    return user && user.name ? user.name : '';
  }

  private _filter(name: string): Address[] {
    const filterValue = name.toLowerCase();

    return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  addressChosen(e:any){
    this.address = e.option.value;
    this.addressChange.emit(this.address);
  }

  changeAddress(e: any) {
    // var platform = new H.service.Platform({
    //   apikey
    // });
    // const places = platform.getSearchService();
    // places.autosuggest({
    //   at: '45.251787,19.837155',
    //   q: this.myControl.value
    // }, (data: any) => {
    //   console.log(data)
    //   this.options = []
    //   if(data.items.length !== 0){
    //   data.items.forEach((el: any) => {
    //     var pos = {} as Position;
    //     pos.lat = el.position.lat;
    //     pos.lng = el.position.lng;
    //     var address = {} as Address;
    //     address.name = el.title
    //     address.position = pos
    //     address.address = el.address.label
    //     this.options.push(address)
    //   })}
    //   else{
    //     var address = {} as Address;
    //     address.name = "Nema rezultata pretrage.."
    //     this.options.push(address)
    //   }
    //   this.filteredOptions = this.myControl.valueChanges.pipe(
    //     startWith(''),
    //     map(value => {
    //       const name = typeof value === 'string' ? value : value?.name;
    //       return name ? this._filter(name as string) : this.options.slice();
    //     }),
    //   );
    // }, (err: any) => {
    //   console.log(err)
    // })
  }

  formsubmit(){
    this.showErrors = true;
    this.isValid.emit(this.myControl.valid);
    // if(this.myControl.valid){
    //   //navigate
    // }
  }

}
