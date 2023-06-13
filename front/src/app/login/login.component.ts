import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, NgForm, Validators} from '@angular/forms';
import {UserAuthService} from "../service/user-auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username = new FormControl('',[Validators.required, Validators.email])
  password = new FormControl('', [Validators.required]);
  hide = true;
  // @ts-ignore
  @ViewChild('loginForm', { static: true }) loginForm: NgForm;
  getErrorMessage() {
    if (this.username.hasError('required')) {
      return 'Popunite polje';
    }

    return this.username.hasError('email') ? 'Email nije validan' : '';
  }
  constructor(
    public service: UserAuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }

  login(loginForm: NgForm) {
    this.service.login(loginForm.value).subscribe(
      (response: any) => {
        this.service.setToken(response.accessToken);
        this.service.setRole(response.role);
        if (response.role === 'ROLE_CLIENT') {
          this.router.navigate(['/client'])
        } else if (response.role === 'ROLE_DRIVER') {
          this.router.navigate(['/driver'])
        } else if (response.role === 'ROLE_ADMINISTRATOR') {
          this.router.navigate(['/admin'])
        }
      }
    );
  }

}
