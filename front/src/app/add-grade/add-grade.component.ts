import {Component, OnInit} from '@angular/core';
import {Grade, GradeStatus} from "../model/grade.model";
import {Role, Status, User} from "../model/user.model";
import {Drive, DriveStatus, DriveType} from "../model/drive.model";
import {BankStatus} from "../model/clientsAccount.model";
import {CarBodyType, Fuel} from "../model/car.model";
import {DriverStatus} from "../model/driversAccount.model";
import {AppService} from "../app.service";
import {MatTableDataSource} from "@angular/material/table";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-add-grade',
  templateUrl: './add-grade.component.html',
  styleUrls: ['./add-grade.component.css']
})
export class AddGradeComponent implements OnInit {
  drive_id:number = 0;

  logged_user: User = {
    username: '',
    name: '',
    surname: '',
    email: '',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  };

  grade: Grade = {
    gradeDriver:0,
    gradeCar:0,
    comment:"",
    gradeStatus: GradeStatus.WAITING_FOR_GRADE,
    rater: {
      username:'',
      name:'',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    drive: {
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
        driversAvailability: true,
      },
      date: '',
      splitBill: false,
      ownerDebit: 0,
      driveType: DriveType.NOW,
      startDate: '',
      endDate: ''
    }
  }

  constructor(private appService: AppService,
              private route: ActivatedRoute,) { }

  ngOnInit(): void {
    this.getLoggedUser();
    this.loadDrive();
  }

  getLoggedUser(){

    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      this.grade.rater = this.logged_user;
      console.log(resp);
      console.log(this.logged_user.role)
    });
  }
  loadDrive(){
    this.route.queryParams.subscribe(params => {

      this.drive_id = params['driveID']
    });

    this.appService.getDriveForGrade(this.drive_id).subscribe((resp: Drive) => {
      this.grade.drive = resp;

      console.log(resp);
    });

  }

  saveGrade(){
    this.appService.addGrade(this.grade).subscribe((resp: any) => {

      console.log(resp.error.message)
      if(resp.error.message == "User already graded this drive."){
        this.appService.openErrorDialog(resp.error.message);
      }
      console.log(resp);
      console.log(this.logged_user.role)
    });
  }

}
