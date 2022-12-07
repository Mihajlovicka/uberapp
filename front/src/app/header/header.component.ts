import { Component, OnInit } from '@angular/core';
import {UserRegistrationService} from "../user-registration.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(
    public service: UserRegistrationService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  public isLoggedIn(){
    return this.service.isLoggedIn();
  }

  public logout(){
    this.service.clear();
    this.router.navigate([''])
  }


}
