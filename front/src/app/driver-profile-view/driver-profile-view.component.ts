import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { DriversAccount } from '../model/driversAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-driver-profile-view',
  templateUrl: './driver-profile-view.component.html',
  styleUrls: ['./driver-profile-view.component.css']
})
export class DriverProfileViewComponent implements OnInit {

  
  disabled:boolean=true;
  izmena:string="Izmeni podatke"
  email:string="";
  
  driversAccount : DriversAccount = {
    user:{
      name:'SRRRRR',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    picture:'',
    phone:'',
    car:{
      brand:'',
      model:'',
      color:'',
      plateNumber:'',
      bodyType: CarBodyType.COUPE,
      fuelType: Fuel.AUTOGAS
    }
  }

  constructor(private appService: AppService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.loadDriver();
  }

  loadDriver(){
    this.route.queryParams.subscribe(params => {
      
      this.email = params['email']
     });

     this.appService.getDriver(this.email).subscribe((resp: DriversAccount) => {
      this.driversAccount = resp;
      //alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
      console.log(this.driversAccount);
      console.log(resp);
    });
  }
  
}