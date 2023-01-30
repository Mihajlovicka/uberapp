import { AfterContentChecked, AfterViewChecked, AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { BankStatus } from '../model/clientsAccount.model';
import { Drive, DriveStatus } from '../model/drive.model';
import { DriveReservationForm } from '../model/driveReservationForm.model';
import { DriverStatus } from '../model/driversAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit{

  constructor(private service: AppService) { }

 

  @Input() drive:DriveReservationForm = {
    stops: [],
    distance: 0,
    duration: 0,
    price: 0,
    passengers: [],
    seats: 5,
    baby: 0,
    babySeats:0,
    pets:0,
    owner: null,
    routeJSON:{},
    //driver:null,
    //driveStatus: DriveStatus.PASSENGERS_WAITING,
    splitBill: false,
    date:""
  };

  created: Drive={
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
    date: ''
  }

 



  ngOnInit(){}

  create(){
    this.service.createDriveReservation(this.drive).subscribe(
      (resp: Drive) =>{
        this.created = resp;
        console.log(this.created)
      }
    )
  }



}
