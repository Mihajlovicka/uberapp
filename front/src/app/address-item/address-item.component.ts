import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {MapAddress, Position} from '../model/mapAddress.model'
import {MapService} from "../service/map.service";
import {MatDialog} from "@angular/material/dialog";
import {ErrorDialogComponent} from "../dialog-template/error-dialog/error-dialog.component";
import {AppService} from "../app.service";
import { UserAuthService } from '../service/user-auth.service';

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
  @Input() isRequired:boolean = false;
  @Input() showErrors:boolean = false;
  isRealAddress:boolean = false;
  clearField:boolean = true
  frequent : MapAddress[] = [];

  address: MapAddress | undefined;
  @Output() addressChange = new EventEmitter<MapAddress>();
  @Output() isValid = new EventEmitter<boolean>();


  constructor(private mapService:MapService,
              public dialog: MatDialog,
              private appService:AppService, private authService: UserAuthService) { }

  ngOnInit(): void {
    if(this.isRequired){
      this.myControl.setValidators([Validators.required]);
      this.myControl.updateValueAndValidity();
    }
    if(this.authService.isLoggedIn())
    this.appService.getFrequentAddresses().subscribe((res:MapAddress[]) => {
      this.options = res;
      this.frequent  = res
      this.filteredOptions = this.myControl.valueChanges.pipe(
        startWith(''),
        map(value => {
          const name = typeof value === 'string' ? value : value?.name;
          return name ? this._filter(name as string) : this.options.slice();
        }),
      );
    })
  }

  displayFn(user: MapAddress): string {
    return user && user.name ? user.name : '';
  }

  private _filter(name: string): MapAddress[] {
    const filterValue = name.toLowerCase();

    return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  addressChosen(e:any){
    if(e.option.value === '')
      return;
    this.address = e.option.value;
    this.isRealAddress = true
    this.addressChange.emit(this.address);
    this.clearField = false
  }

  changeAddress() {
    if(this.myControl.value.length % 5 === 0 && this.myControl.value.length !== 0)
      this.makeRequest()
  }

  remove(){
    this.myControl.setValue('');
    this.isRealAddress = false;
    this.addressChange.emit(undefined);
  }

  makeRequest(){
    this.isRealAddress = false
    const places = this.mapService.getHPlatform().getSearchService();
    places.autosuggest({
      at: '45.251787,19.837155',
      q: this.myControl.value
    }, (data: any) => {
      if(data !== null){
        this.options = []
        if(data.items.length !== 0){
          data.items.forEach((el: any) => {
            this.options.push(new MapAddress(el.title, new Position(el.position.lat, el.position.lng),
              el.address.label,undefined))
          })}
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
    if(this.isRealAddress) {
      this.isValid.emit(this.myControl.valid);
    }
    else {
      this.myControl.setValue('')
      this.myControl.markAllAsTouched()
      this.isValid.emit(false)
    }
  }

  clear(){
    this.isRealAddress = false
    this.address = undefined
    this.filteredOptions = undefined
    this.options = []
    this.myControl.setValue('')
    this.clearField = false
  }
}

