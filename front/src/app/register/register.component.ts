import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { RegisterForm } from '../model/registerForm.model';
import { User, Status, Role } from '../model/user.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: RegisterForm = {
    name:'',
    surname:'',
    email:'',
    phone:'',
    address: {
      city:'',
      street:'',
      number:''
    },
    password:'',
    checkPassword:'',
    role: Role.CLIENT
  }

  user: User = {
    name:'',
    surname:'',
    email:'',
    phone:'',
    address:{
      city:'',
      street:'',
      number:''
    },
    status: Status.ACTIVE,
    role: Role.CLIENT
  }

  constructor(private appService: AppService) { }

  ngOnInit(): void {
  }

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
    this.registerForm.name.charAt(0).toUpperCase();
    this.registerForm.surname.charAt(0).toUpperCase();
    // address ?
  }

  matchingPasswords(pass: string, check: string): boolean{
    if(pass===check)return true
    return false
  }

  register(){
    if(this.registerForm.name==='')alert("Unesite ime!")
    else if(this.registerForm.surname==='')alert("Unesite prezime!")
    else if(this.registerForm.email==='')alert("Unesite email!")
    else if(this.registerForm.phone==='')alert("Unesite telefon!")
    else if(this.registerForm.address.city==='')alert("Unesite grad!")
    else if(this.registerForm.address.street==='')alert("Unesite ulicu!")
    else if(this.registerForm.address.number==='')alert("Unesite broj!")
    else if(!this.validateEmail(this.registerForm.email))alert("Email nije validan!.")
    else if(!this.validateNums(this.registerForm.phone))alert("Telefon nije validan!")
    else if(!this.validateChars(this.registerForm.name))alert("Ime nije validno")
    else if(!this.validateChars(this.registerForm.surname))alert("Prezime nije validno")
    else if(!this.matchingPasswords(this.registerForm.password, this.registerForm.checkPassword))alert("Lozinke se ne poklapaju!")
    else{

    this.capitalizeForm();
    
    this.appService.register(this.registerForm).subscribe((resp: User) => {
        this.user = resp;
        alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
        console.log(this.user);
      })
    }
  }

}
