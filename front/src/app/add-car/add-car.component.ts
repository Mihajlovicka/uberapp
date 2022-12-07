import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from '../app.service';
import { CarBodyType, Fuel } from '../model/car.model';
import { DriverCarInfo } from '../model/driverCarInfo.model';
import { DriversAccount } from '../model/driversAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-add-car',
  templateUrl: './add-car.component.html',
  styleUrls: ['./add-car.component.css']
})
export class AddCarComponent implements OnInit {

  constructor(private service: AppService, private router: Router) { }

  selectedFuelType:string='';
  selectedBodyType:string='';


  info: DriverCarInfo = {
    name: '',
    surname: '',
    email: '',
    phone: '',
    password: '',
    checkPassword: '',
    role: Role.ROLE_DRIVER,
    car: {
      brand:'',
      model:'',
      color:'',
      plateNumber:'',
      bodyType: CarBodyType.HATCHBACK,
      fuelType: Fuel.GASOLINE

    }

  }

  driversAcc: DriversAccount = {
    user:{
      name:'',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_DRIVER
    },
    picture:'',
    phone:'',
    car:{
      brand:'',
      model:'',
      color:'',
      plateNumber:'',
      bodyType: CarBodyType.HATCHBACK,
      fuelType: Fuel.GASOLINE
    }
  }

  ngOnInit(): void {
    this.service.currentData.subscribe(data => {
      console.log(data);
      this.info = data;
      this.selectedBodyType = this.info.car.bodyType;
      this.selectedFuelType = this.info.car.fuelType;

    });
  }


  validateChars(str: string): boolean{
    var regex = /^[a-zA-Z]+\s?[a-zA-Z]+$/;
    return regex.test(str);
  }

  validateRegistrationPlateNumber(plateNum: string): boolean{
    var regex = /^[a-zA-Z]{2}\d{3}[a-zA-Z]{2}$/
    return regex.test(plateNum);
  }

  capitalizeForm(){
    this.info.car.brand.charAt(0).toUpperCase();
    this.info.car.model.charAt(0).toUpperCase();
    this.info.car.plateNumber.toUpperCase();
    this.info.car.color.toUpperCase();
  }


  mappFuelOptions(){
    if(this.selectedFuelType === 'GASOLINE')this.info.car.fuelType=Fuel.GASOLINE;
    if(this.selectedFuelType === 'DIESEL')this.info.car.fuelType=Fuel.DIESEL;
    if(this.selectedFuelType === 'AUTOGAS')this.info.car.fuelType=Fuel.AUTOGAS;
  }

  mappBodyOptions(){
    if(this.selectedBodyType === 'COUPE')this.info.car.bodyType=CarBodyType.COUPE;
    if(this.selectedBodyType === 'JEEP')this.info.car.bodyType=CarBodyType.JEEP;
    if(this.selectedBodyType === 'SEDAN')this.info.car.bodyType=CarBodyType.SEDAN;
    if(this.selectedBodyType === 'SUV')this.info.car.bodyType=CarBodyType.SUV;
    if(this.selectedBodyType === 'HATCHBACK')this.info.car.bodyType=CarBodyType.HATCHBACK;
  }

 //fja za kad idemo nazad i da ona sacuiva u servisu te podatke(znaci samo set data postavi na ovo trenutno odavde)
  back(){

    this.mappBodyOptions();
    this.mappFuelOptions();

    this.service.setData(this.info);
    const navigationPath: string[] = ['/driver-info'];
    this.router.navigate(navigationPath);

  }

  save(){

    if(this.info.car.brand==='')alert('Unesite marku!')
    else if(!this.validateChars(this.info.car.brand))alert("Marka nije validna")

    else if(this.info.car.model==='')alert('Unesite model!')

    else if(this.info.car.color==='')alert('Unesite boju!')
    else if(!this.validateChars(this.info.car.color))alert("Boja nije validna")

    else if(this.info.car.plateNumber==='')alert('Unesite registracioni broj!')
    else if(!this.validateRegistrationPlateNumber(this.info.car.plateNumber))alert("Registracija nije validna!")
    else{

    this.mappBodyOptions();
    this.mappFuelOptions();

    this.capitalizeForm();


    this.service.setData(this.info);

    console.log("ovo pre cuvankja")
    console.log(this.info);


    this.service.addDriverCarAccount(this.info).subscribe((resp: DriversAccount) => {
      this.driversAcc = resp;
      //alert("Uspesno ste dodali novog vozaca u sistem!")
      console.log(this.driversAcc);
    })


    }
  }

}
