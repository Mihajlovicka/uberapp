import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { DriversAccount, DriverStatus } from '../model/driversAccount.model';
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
      username:'',
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
      fuelType: Fuel.AUTOGAS,
      numOfSeats:5
    },
    driverStatus: DriverStatus.AVAILABLE
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
  change(){
    if(this.izmena==="Izmeni podatke"){
      this.izmena="Sacuvaj podatke";
      this.disabled=false;
    }
    else{
      this.saveChanges();
      this.izmena="Izmeni podatke";
      this.disabled=true;
    }
    
  }
     
  cancelChange(){
    this.izmena="Izmeni podatke";
    this.disabled=true;
    this.loadDriver();
  }
  validateNums(str: string): boolean{
    var regex =  /^.+?\d{7,15}$/;
    return regex.test(str);
  }

  validateChars(str: string): boolean{
    var regex = /^[a-zA-Z]+\s?[a-zA-Z]+$/;
    return regex.test(str);
  }

  capitalizeForm(){
    this.driversAccount.user.name.charAt(0).toUpperCase();
    this.driversAccount.user.surname.charAt(0).toUpperCase();
    this.driversAccount.car.brand.charAt(0).toUpperCase();
    this.driversAccount.car.model.charAt(0).toUpperCase();
  }

  saveChanges(){
  if(this.driversAccount.user.name==='')alert("Unesite ime!")
  else if(!this.validateChars(this.driversAccount.user.name))alert("Ime nije validno")

  else if(this.driversAccount.user.surname==='')alert("Unesite prezime!")
  else if(!this.validateChars(this.driversAccount.user.surname))alert("Prezime nije validno")

  else if(this.driversAccount.phone==='')alert("Unesite telefon!")
  else if(!this.validateNums(this.driversAccount.phone))alert("Telefon nije validan!")

  else if(this.driversAccount.car.brand==='')alert("Unesite breand auta!")
  else if(this.driversAccount.car.model==='')alert("Unesite model auta!")
  else if(this.driversAccount.car.plateNumber==='')alert("Unesite broj tablica!")
  else if(!this.validateChars(this.driversAccount.car.brand)) alert("Brend auta nije validan!")
  else if(!this.validateChars(this.driversAccount.car.color)) alert("Boja auta nije validna!")

  else{

  this.capitalizeForm();

  this.appService.updateDriver(this.driversAccount).subscribe((resp: DriversAccount) => {
      this.driversAccount = resp;
      //alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
      
      alert("Zahtev za izmenu podataka je poslat administratoru sistema.");
    })
  }
}
}