import {Component, Input, OnInit} from '@angular/core';
import {AppService} from '../app.service';
import {CarBodyType, Fuel} from '../model/car.model';
import {BankStatus, ClientsAccount} from '../model/clientsAccount.model';
import {Drive, DriveStatus, DriveType} from '../model/drive.model';
import {DriveReservationForm, PriceStart} from '../model/driveReservationForm.model';
import {DriverStatus} from '../model/driversAccount.model';
import {PaymentPassengerStatus} from '../model/passenger.model';
import {Role, Status, User} from '../model/user.model';
import {delay} from "rxjs";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit{

  constructor(private service: AppService) { }



  ngOnInit(){
    this.getLoggedUser();
  }


  @Input() starting_price: PriceStart = PriceStart.seats5;

  loggedUser: User = {
    username: '',
    name: '',
    surname: '',
    email: '',
    status: Status.ACTIVE,
    role: Role.ROLE_DRIVER
  }

  clientAcc: ClientsAccount={
    user: {
      username: '',
    name: '',
    surname: '',
    email: '',
    status: Status.ACTIVE,
    role: Role.ROLE_DRIVER
    },
    address: {
      city:'',
      street:'',
      number:''
    },
    picture: {
      picByte:null,
      name:'',
      type:''
    },
    phone: '',
    clientsBankAccount: {
      verificationEmail:'',
      balance:0,
      bankAccountNumber:'',
      ownerName:'',
      ownerSurname:''
    },
    bankStatus: BankStatus.ACTIVE,
    inDrive: false
  }

  getLoggedUser(){
    this.service.getLoggedUser().subscribe(
      (resp: User)=>{
        this.loggedUser = resp;
        console.log(this.loggedUser);
        this.getClientAccount(this.loggedUser.email)
      }
    )
  }

  getClientAccount(email: string){
    this.service.getClient(email).subscribe(
      (resp: ClientsAccount)=>{
        this.clientAcc = resp;
        console.log(this.clientAcc);
      }
    )
  }


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
      },
      inDrive:false
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
      driversAvailability:true,
    },
    date: '',
    splitBill: false,
    ownerDebit: 0,
    driveType: DriveType.NOW,
    startDate: '',
    endDate: '',
    ownerTransactionId:-1
  }

  numberOfPartition: number = 0;


  setNumOfPartition(){
    this.numberOfPartition = 0;
    this.drive.passengers.forEach(passenger => {
      if(passenger.payingEnabled===true)this.numberOfPartition=this.numberOfPartition+1;
    })
  }



  setSplitPayment(split: boolean){
    this.drive.splitBill=split;
    //pozvati ovo
    if(this.drive.splitBill===true)this.setNumOfPartition();

    //pogledati kpjim komponentama se vraca ovo
    //bil i ono za prikaz pass vljd

  }






  create(){
    this.drive.price = this.drive.price+this.starting_price;


    if(this.drive.splitBill === true){

      this.drive.passengers.forEach(passenger  => {
        if(passenger.payingEnabled===true){
        passenger.debit = Number((this.drive.price/(this.numberOfPartition+1)).toFixed(2));
        this.drive.ownerDebit = passenger.debit;
        }
        if(passenger.payingEnabled===false){
          passenger.debit = 0;
        }
      });



    }

    if(this.drive.splitBill===false){
      this.drive.passengers.forEach(passenger => {
        passenger.debit = 0;
        passenger.payment = PaymentPassengerStatus.NOT_PAYING;
      })
      this.drive.ownerDebit = this.drive.price;
    }

    this.service.createDriveReservation(this.drive).pipe(delay(1000)).subscribe(
      (resp: Drive) => {
        this.created = resp;
        console.log(this.created);
        if (resp.driveStatus != undefined) {
          if (resp.driveStatus == DriveStatus.DRIVE_FAILED)
            this.service.openErrorDialog("Voznja neuspesno rezervisana.");
          if (resp.driveStatus == DriveStatus.OWNER_PAYMENT_WAITING)
            this.service.openErrorDialog("Voznja uspesno rezervisana, proverite mail. transakcija:" + this.created.ownerTransactionId);
        }
      }

    )
    this.drive.price = this.drive.price - this.starting_price;
  }



}
