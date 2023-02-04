import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { BankStatus } from '../model/clientsAccount.model';
import { Drive, DriveStatus, DriveType } from '../model/drive.model';
import { DriverStatus } from '../model/driversAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-drive-changed-review',
  templateUrl: './drive-changed-review.component.html',
  styleUrls: ['./drive-changed-review.component.css']
})
export class DriveChangedReviewComponent implements OnInit {

  constructor(private route: ActivatedRoute, private service: AppService) { }

  ngOnInit(): void {
    this.getDriveId();
    this.getDriveData(this.driveId);
  }

  driveId: number=0;

  drive: Drive={
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
      driverStatus: DriverStatus.AVAILABLE,
      driversAvailability: true
    },
    date: '',
    splitBill: false,
    ownerDebit: 0,
    driveType: DriveType.NOW,
    startDate: '',
    endDate: '',
    ownerTransactionId: -1
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

  driveCanceled(){
    this.service.cancelDrive(this.driveId).subscribe(
      (resp:Drive) => {
        this.drive=resp;
        console.log(this.drive);
      }
    )
  }

  continueDrive(){
    this.service.continueDrive(this.driveId).subscribe(
      (resp: Drive) => {
        this.drive = resp;
        console.log(this.drive);
      }
    )
  }

}