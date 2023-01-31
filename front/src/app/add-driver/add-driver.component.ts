import { Component, OnInit } from '@angular/core';
import { DriverCarInfo } from '../model/driverCarInfo.model';
import { Role } from '../model/user.model';
import { Fuel, CarBodyType, BabySeat } from '../model/car.model';
import { Router } from '@angular/router';
import { AppService } from '../app.service';

@Component({
  selector: 'app-add-driver',
  templateUrl: './add-driver.component.html',
  styleUrls: ['./add-driver.component.css']
})
export class AddDriverComponent implements OnInit {

  info: DriverCarInfo = {
    username:'',
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
      fuelType: Fuel.GASOLINE,
      numOfSeats:5,
    }

  }

  constructor(private router: Router, private service:AppService) { }

  validateEmail(email: string): boolean{
    var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  validateNums(str: string): boolean{
    var regex =  /^.+?\d{7,15}$/;
    return regex.test(str);
  }

  validateChars(str: string): boolean{
    var regex = /^[a-zA-Z]+$/;
    return regex.test(str);
  }

  capitalizeForm(){
    this.info.name.charAt(0).toUpperCase();
    this.info.surname.charAt(0).toUpperCase();
  }

  matchingPasswords(pass: string, check: string): boolean{
    if(pass===check)return true
    return false
  }

  ngOnInit(): void {
    this.service.currentData.subscribe(data => {
      console.log(data);
      this.info = data;
    });
  }

  next(){
    if(this.info.name==='')alert("Unesite ime!")
    else if(!this.validateChars(this.info.name))alert("Ime nije validno")

    else if(this.info.surname==='')alert("Unesite prezime!")
    else if(!this.validateChars(this.info.surname))alert("Prezime nije validno")

    else if(this.info.email==='')alert("Unesite email!")
    else if(!this.validateEmail(this.info.email))alert("Email nije validan!.")

    else if(this.info.phone==='')alert("Unesite telefon!")
    else if(!this.validateNums(this.info.phone))alert("Telefon nije validan!")

    else if(this.info.password==='')alert("Unesite lozinku!")
    else if(this.info.checkPassword==='')alert("Potvrdite lozinku")


    else if(!this.matchingPasswords(this.info.password, this.info.checkPassword))alert("Lozinke se ne poklapaju!")

    else{

      this.capitalizeForm();

      this.service.setData(this.info);
      const navigationPath: string[] = ['/car-info'];
      this.router.navigate(navigationPath);
    }
  }


}
