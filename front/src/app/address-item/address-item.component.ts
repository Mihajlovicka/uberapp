import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {MapAddress, Position} from '../model/mapAddress.model'
import {MapService} from "../service/map.service";
import {MatDialog} from "@angular/material/dialog";
import {ErrorDialogComponent} from "../dialog-template/error-dialog/error-dialog.component";

@Component({
  selector: 'app-address-item',
  templateUrl: './address-item.component.html',
  styleUrls: ['./address-item.component.css']
})
export class AddressItemComponent implements OnInit {
// @ts-ignore
  myControl = new FormControl<string | MapAddress>('');
  options: MapAddress[] = [];
  filteredOptions: Observable<MapAddress[]> | undefined;

  private noResult:string = 'Nema rezultata pretrage..'

  @Input() addressText = '';
  @Input() removeRequired:boolean = false;
  @Input() showErrors:boolean = false;

  address: MapAddress | undefined;
  @Output() addressChange = new EventEmitter<MapAddress>();
  @Output() isValid = new EventEmitter<boolean>();


  constructor(private mapService:MapService,
              public dialog: MatDialog, ) { }

  ngOnInit(): void {
    if(!this.removeRequired){
      this.myControl.setValidators([Validators.required]);
      this.myControl.updateValueAndValidity();
    }
  }

  displayFn(user: MapAddress): string {
    return user && user.name ? user.name : '';
  }

  private _filter(name: string): MapAddress[] {
    const filterValue = name.toLowerCase();

    return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  addressChosen(e:any){
    this.address = e.option.value;
    this.addressChange.emit(this.address);
  }

  changeAddress() {
    if(this.myControl.value.length % 5 === 0)
      this.makeRequest()
  }

  makeRequest(){
    const places = this.mapService.getHPlatform().getSearchService();
    places.autosuggest({
      at: '45.251787,19.837155',
      q: this.myControl.value
    }, (data: any) => {
      if(data !== null){
        this.options = []
        if(data.items.length !== 0){
          data.items.forEach((el: any) => {
            var pos = {} as Position;
            pos.lat = el.position.lat;
            pos.lng = el.position.lng;
            var address = {} as MapAddress;
            address.name = el.title
            address.position = pos
            address.address = el.address.label
            this.options.push(address)
          })}
        else{
          var address = {} as MapAddress;
          address.name = this.noResult
          this.options.push(address)
        }
        this.filteredOptions = this.myControl.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.name;
            return name ? this._filter(name as string) : this.options.slice();
          }),
        );
      }
    }, (err) => {
      this.mapService.openErrorDialog("Doslo je do greske pri povezivanju sa serverom. molimo pokusajte ponovo kasnije.")
    })

  }


  formsubmit(){
    this.showErrors = true;
    if(this.address?.name !== this.noResult)
      this.isValid.emit(this.myControl.valid);
    else
      this.myControl.setValue('')
  }

}

