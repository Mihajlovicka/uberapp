import { Component, OnInit } from '@angular/core';
import {FormControl, NgForm, Validators} from '@angular/forms';
import {UserRegistrationService} from "../user-registration.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username = new FormControl('', [Validators.required, Validators.email]);
  password = new FormControl('', [Validators.required]);
  hide = true;

  getErrorMessage() {
    if (this.username.hasError('required')) {
      return 'Popunite polje';
    }

    return this.username.hasError('email') ? 'Email nije validan' : '';
  }
  constructor(
    private service: UserRegistrationService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  login(loginForm: NgForm){
    this.service.login(loginForm.value).subscribe(
      (response:any) => {
        this.service.setToken(response.accessToken);
        this.service.setRole(response.role);
        if(response.role === 'ROLE_CLIENT'){
          this.router.navigate(['/client'])
        }
        else if(response.role === 'ROLE_DRIVER'){
          this.router.navigate(['/driver'])
        }
        },
      (error) => {console.log(error);}
    );
  }

}
