import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { BankStatus, ClientsAccount } from '../model/clientsAccount.model';
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
    role: Role.ROLE_CLIENT,
    bankAccountNumber: ''
  }



  clientsAccount: ClientsAccount = {
    user:{
      name:'',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    address:{
      city:'',
      street:'',
      number:''
    },
    picture:'',
    phone:'',
    clientsBankAccount:{
      balance:0,
      bankAccountNumber:''
    },
    bankStatus: BankStatus.EMPTY
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
    var regex = /^[a-zA-Z]+\s?[a-zA-Z]+$/;
    return regex.test(str);
  }

  validateBankAccountNumber(str: string): boolean {
    var regex =  /^.+?\d{18}$/;
    return regex.test(str);
  }

  capitalizeForm(){
    this.registerForm.name.charAt(0).toUpperCase();
    this.registerForm.surname.charAt(0).toUpperCase();
    this.registerForm.address.city.charAt(0).toUpperCase();
    this.registerForm.address.street.charAt(0).toUpperCase();
  }

  matchingPasswords(pass: string, check: string): boolean{
    if(pass===check)return true
    return false
  }

  register(){
    if(this.registerForm.name==='')alert("Unesite ime!")
    else if(!this.validateChars(this.registerForm.name))alert("Ime nije validno")

    else if(this.registerForm.surname==='')alert("Unesite prezime!")
    else if(!this.validateChars(this.registerForm.surname))alert("Prezime nije validno")

    else if(this.registerForm.email==='')alert("Unesite email!")
    else if(!this.validateEmail(this.registerForm.email))alert("Email nije validan!.")

    else if(this.registerForm.phone==='')alert("Unesite telefon!")
    else if(!this.validateNums(this.registerForm.phone))alert("Telefon nije validan!")

    else if(this.registerForm.address.city==='')alert("Unesite grad!")
    else if(this.registerForm.address.street==='')alert("Unesite ulicu!")
    else if(this.registerForm.address.number==='')alert("Unesite broj!")
    else if(!this.validateChars(this.registerForm.address.city) || !this.validateChars(this.registerForm.address.street))alert("Adresa nije validna!")

    else if(this.registerForm.password==='')alert("Unesite lozinku!")
    else if(this.registerForm.checkPassword==='')alert("Potvrdite lozinku")
    else if(!this.matchingPasswords(this.registerForm.password, this.registerForm.checkPassword))alert("Lozinke se ne poklapaju!")
    else if(!this.validateBankAccountNumber(this.registerForm.bankAccountNumber))alert("Broj racuna nije u validnom formatu.")
    else{

    this.capitalizeForm();

    this.appService.register(this.registerForm).subscribe((resp: ClientsAccount) => {
        this.clientsAccount = resp;
        //alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
        console.log(this.clientsAccount);
      })
    }
  }

}
