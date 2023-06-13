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
    username:'hehe',
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
    user: {
      username: '',
      name: '',
      surname: '',
      email: '',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    address: {
      city: '',
      street: '',
      number: ''
    },
    picture: {
      name: '',
      type: '',
      picByte: null
    },
    phone: '',
    clientsBankAccount: {
      balance: 0,
      bankAccountNumber: '',
      verificationEmail: '',
      ownerName: '',
      ownerSurname: ''
    },
    bankStatus: BankStatus.EMPTY,
    inDrive: false
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
    var regex =  /^.+?\d{17}$/;
    return regex.test(str);
  }

  capitalizeForm() {
    this.registerForm.name = this.capitalizeFirstLetter(this.registerForm.name);
    this.registerForm.surname = this.capitalizeFirstLetter(this.registerForm.surname);
    this.registerForm.address.city = this.capitalizeFirstLetter(this.registerForm.address.city);
    this.registerForm.address.street = this.capitalizeFirstLetter(this.registerForm.address.street);
  }

  capitalizeFirstLetter(s: string) {
    return s.charAt(0).toUpperCase() + s.slice(1);
  }


  matchingPasswords(pass: string, check: string): boolean{
    if(pass===check)return true
    return false
  }

  register(){
    if(this.registerForm.name==='')this.appService.openErrorDialog("Unesite ime!")
    else if(!this.validateChars(this.registerForm.name))this.appService.openErrorDialog("Ime nije validno!");

    else if(this.registerForm.surname==='')this.appService.openErrorDialog("Unesite prezime!");
    else if(!this.validateChars(this.registerForm.surname))this.appService.openErrorDialog("Prezime nije validno!");

    else if(this.registerForm.email==='')this.appService.openErrorDialog("Unesite email");
    else if(!this.validateEmail(this.registerForm.email))this.appService.openErrorDialog("Email nije validan!");

    else if(this.registerForm.phone==='')this.appService.openErrorDialog("Unesite telefon");
    else if(!this.validateNums(this.registerForm.phone))this.appService.openErrorDialog("Telefon nije validan!");

    else if(this.registerForm.address.city==='')this.appService.openErrorDialog("Unesite grad!");
    else if(this.registerForm.address.street==='')this.appService.openErrorDialog("Unesite ulicu!");
    else if(this.registerForm.address.number==='')this.appService.openErrorDialog("Unesite broj!");
    else if(!this.validateChars(this.registerForm.address.city) || !this.validateChars(this.registerForm.address.street))this.appService.openErrorDialog("Adresa nije validna!");

    else if(this.registerForm.password==='')this.appService.openErrorDialog("Unesite lozinku!");
    else if(this.registerForm.checkPassword==='')this.appService.openErrorDialog("Potvrdite lozinku!");
    else if(!this.matchingPasswords(this.registerForm.password, this.registerForm.checkPassword))this.appService.openErrorDialog("Lozinke se ne poklapaju!");
    else if(this.registerForm.bankAccountNumber != "" && !this.validateBankAccountNumber(this.registerForm.bankAccountNumber))this.appService.openErrorDialog("Broj racuna nije u validnom formatu.");
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
