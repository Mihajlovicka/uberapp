import { AfterContentChecked, AfterViewChecked, AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { BankStatus } from '../model/clientsAccount.model';
import {Drive, DriveStatus, DriveType} from '../model/drive.model';
import { DriveReservationForm, PriceStart } from '../model/driveReservationForm.model';
import { DriverStatus } from '../model/driversAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit{

  constructor(private service: AppService) { }






  @Input() starting_price: PriceStart = PriceStart.seats5;


  @Input() drive:DriveReservationForm = {
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
    date: "",
    ownerDebit: 0
  };

  created: Drive={
    id: 0,
    stops: [],
    distance: 0,
    duration: 0,
    price: 0,
    passengers: [],
    baby: 0,
    babySeats: 0,
    pets: 0,
    driveStatus: DriveStatus.PASSENGERS_WAITING,
    owner: {
      user: {
        name: '',
        surname: '',
        email: '',
        username: '',
        role: Role.ROLE_CLIENT,
        status: Status.ACTIVE
      },
      picture: {
        name: '',
        type: '',
        picByte: ''
      },
      bankStatus: BankStatus.ACTIVE,
      address: {
        city: '',
        street: '',
        number: ''
      },
      phone: '',
      clientsBankAccount: {
        balance: 0,
        bankAccountNumber: '',
        ownerName: '',
        ownerSurname: '',
        verificationEmail: ''
      }
    },
    routeJSON: {},
    seats: 0,
    driver: {
      user: {
        name: '',
        surname: '',
        email: '',
        username: '',
        role: Role.ROLE_DRIVER,
        status: Status.ACTIVE
      },
      phone: '',
      picture: {
        name: '',
        type: '',
        picByte: ''
      },
      car: {
        brand: '',
        model: '',
        plateNumber: '',
        bodyType: CarBodyType.HATCHBACK,
        numOfSeats: 0,
        fuelType: Fuel.AUTOGAS,
        color: ''
      },
      driverStatus: DriverStatus.AVAILABLE,
    },
    date: '',
    splitBill: false,
    ownerDebit: 0,
    driveType: DriveType.NOW,
    startDate: '',
    endDate: ''
  }


  setSplitPayment(split: boolean){
    this.drive.splitBill=split;

  }




  ngOnInit(){}

  create(){
    this.drive.price = this.drive.price+this.starting_price;


    if(this.drive.splitBill === true){
      this.drive.passengers.forEach(passenger  => {
        passenger.debit = Number((this.drive.price/(this.drive.passengers.length+1)).toFixed(2));
      });
    }

    this.service.createDriveReservation(this.drive).subscribe(
      (resp: Drive) =>{
        this.created = resp;
        console.log(this.created);}

    )
    this.drive.price = this.drive.price - this.starting_price;
  }



}
