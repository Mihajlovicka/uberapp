import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { RegisterForm } from '../model/registerForm.model';
import { User, Status } from '../model/user.model';

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
    address:{
      city:'',
      street:'',
      number:''
    },
    password:'',
    checkPassword:''
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
    status: Status.ACTIVE
  }

  constructor(private appService: AppService) { }

  ngOnInit(): void {
  }

  register(){
    this.appService.register(this.registerForm).subscribe((resp: User) => {
      this.user = resp;
      console.log(this.user);

    })
  }
  

}
