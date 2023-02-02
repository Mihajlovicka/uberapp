import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { BankStatus, ClientsAccount } from '../model/clientsAccount.model';
import {Drive, DriveStatus, DriveType} from '../model/drive.model';
import { DriverStatus } from '../model/driversAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-respond-drive-request',
  templateUrl: './respond-drive-request.component.html',
  styleUrls: ['./respond-drive-request.component.css']
})
export class RespondDriveRequestComponent implements OnInit {

  driveId: number=0;

  logged:ClientsAccount={
    user: {
      name: '',
      surname: '',
      email: '',
      username: '',
      role: Role.ROLE_CLIENT,
      status: Status.ACTIVE
    },
    address: {
      city: '',
      street: '',
      number: ''
    },
    picture: {
      picByte: '',
      name: '',
      type: ''
    },
    phone: '',
    clientsBankAccount: {
      balance: 0,
      bankAccountNumber: '',
      verificationEmail: '',
      ownerName: '',
      ownerSurname: ''
    },
    bankStatus: BankStatus.ACTIVE
  }

  drive: Drive={
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
      address: {
        city: '',
        street: '',
        number: ''
      },
      phone: '',
      picture: {
        picByte: '',
        name: '',
        type: ''
      },
      bankStatus: BankStatus.ACTIVE,
      clientsBankAccount: {
        balance: 0,
        bankAccountNumber: '',
        verificationEmail: '',
        ownerName: '',
        ownerSurname: ''
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
        role: Role.ROLE_CLIENT,
        status: Status.ACTIVE
      },
      picture: {
        picByte: '',
        name: '',
        type: ''
      },
      phone: '',
      car: {
        color: '',
        plateNumber: '',
        bodyType: CarBodyType.HATCHBACK,
        fuelType: Fuel.AUTOGAS,
        brand: '',
        model: '',
        numOfSeats: 5
      },
      driverStatus: DriverStatus.AVAILABLE
    },
    date: '',
    splitBill: false,
    ownerDebit: 0,
    driveType: DriveType.NOW,
    startDate: '',
    endDate: ''
  }

  constructor(private route: ActivatedRoute, private service: AppService) { }

  ngOnInit(): void {
    this.getDriveId();
    this.getDriveData(this.driveId);
    this.getLoggedIn();
  }

  getDriveId(){
    this.route.queryParams.subscribe(params => {

      this.driveId = Number(this.route.snapshot.paramMap.get('id'))
      console.log(this.driveId);
     });
  }

  getDriveData(id: number){
    this.service.getDrive(id).subscribe(
      (resp: Drive)=>{
        this.drive = resp;
        console.log(this.drive);
      }
    )
  }

  getLoggedIn(){
    this.service.getLogged().subscribe((resp: ClientsAccount) =>{
        this.logged = resp;
        console.log(this.logged);
    }
    )
  }

}
