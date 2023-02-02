import { Component, Input, OnInit, Output, EventEmitter, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';


import { DriveReservationForm, PriceStart} from '../model/driveReservationForm.model';


@Component({
  selector: 'app-find-type-of-vehicle',
  templateUrl: './find-type-of-vehicle.component.html',
  styleUrls: ['./find-type-of-vehicle.component.css']
})
export class FindTypeOfVehicleComponent implements OnInit {

  constructor() {}

  

  @Input() drive:DriveReservationForm={
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
    date: '',
    ownerDebit: 0
  }

  kids: string='clear';
  kidsDescription: string='Deca ne ucestvuju u voznji.';
  seatsDescription: string = 'Sedista nisu potrebna.';
  pets: string= 'clear';
  petsDescription: string = 'Kucni ljubimci ne ucestvuju u voznji.';

  @Output() setDrive = new EventEmitter<DriveReservationForm>();

  @Output() nextParentStep = new EventEmitter();

  @ViewChild(MatAccordion)
  accordion!: MatAccordion;
  step = 0;


  vehicleTypeTitle = 'Hatchback(5)';
  vehicleTypeDescription = '4 sedista za putnike + 2 kofera'

  //lista zivotinja
  setPets(pets: string){
    this.pets = pets;
    if(this.pets === 'clear'){
      this.petsDescription='Kucni ljubimci ne ucestvuju u voznji.';
      this.drive.pets = 0;
    }
    if(this.pets === 'check')this.petsDescription='Kucni ljubimci ucestvuju u voznji.';
    this.setDrive.emit(this.drive)
  }

  //lista zivotinja
  checkPetsNum(){
    if(this.drive.pets===0){
      this.pets = 'clear';
      this.petsDescription = 'Kucni ljubimci ne ucestvuju u voznji.';
      
    }
    if(this.drive.pets>0){
      this.pets = 'check';
      this.petsDescription = 'Kucni ljubimci ucestvuju u voznji.'
    }
    this.setDrive.emit(this.drive)
  }



//lista dece
  setKids(kids: string){
    this.kids = kids;
    if(this.kids === 'clear'){
      this.kidsDescription='Deca ne ucestvuju u voznji.';
      this.resetBabyInfo();
    }
    if(this.kids === 'check')this.kidsDescription='Deca ucestvuju u voznji.'
  }

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
    
  }

  goToNextParentStep(){
    this.nextParentStep.emit();
  }

  prevStep() {
    this.step--;
  }

  setVehicleTypeName(){
    if(this.drive.seats===4){
      this.vehicleTypeTitle='Coupe(4)'; 
      this.vehicleTypeDescription='3 sedista za putnike + 1 kofer'}
    if(this.drive.seats===5){
      this.vehicleTypeTitle='Hatchback(5)'; 
      this.vehicleTypeDescription='4 sedista za putnike + 2 kofera'}
    if(this.drive.seats===6){
      this.vehicleTypeTitle='Minivan(6)'; 
      this.vehicleTypeDescription='5 sedista za putnike + 4 kofera'}
    if(this.drive.seats===7){
      this.vehicleTypeTitle='Minivan(7)'; 
      this.vehicleTypeDescription='6 sedista za putnike + 4 kofera'}
    if(this.drive.seats===8){
      this.vehicleTypeTitle='Minivan(8)'; 
      this.vehicleTypeDescription='7 sedista za putnike + 6 kofera'}
    if(this.drive.seats===9){
      this.vehicleTypeTitle='Kombi(9)'; 
      this.vehicleTypeDescription='8 sedista za putnike + 8 kofera'}
  }


  ngOnInit(): void {
  }

  setNumberOfSeats(num: number){
    
    this.drive.seats = num;
   
    this.setVehicleTypeName();

    if(this.drive.seats<this.drive.baby+this.drive.pets+2){
      this.resetBabyInfo();
      this.drive.pets=0;
      this.pets='clear'
      this.petsDescription='Kucni ljubimci ne ucestvuju u voznji.';
    }

    this.setDrive.emit(this.drive);
    
  }

  setBaby(e: any){
    if(this.drive.babySeats>this.drive.baby){this.drive.babySeats=this.drive.baby}
    if(this.drive.babySeats > 0)this.seatsDescription = 'Sedista su potrebna.';
    if(this.drive.babySeats === 0) this.seatsDescription = 'Sedista nisu potrebna.';
    if(this.drive.baby === 0) {
      this.kidsDescription = "Deca ne ucestvuju u voznji.";
      this.kids='clear';
    }
    
  
    this.setDrive.emit(this.drive);
  }

  resetBabyInfo(){
    this.drive.baby = 0;
    this.drive.babySeats = 0;
    this.seatsDescription = 'Sedista nisu potrebna.';
    this.kids='clear';
    this.setDrive.emit(this.drive);
  }

}
