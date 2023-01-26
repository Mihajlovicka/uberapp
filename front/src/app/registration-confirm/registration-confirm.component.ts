import { Component, OnInit } from '@angular/core';
import {AppService} from "../app.service";
import {ClientsAccount} from "../model/clientsAccount.model";
import { ActivatedRoute } from '@angular/router'
import {UserAuthService} from "../service/user-auth.service";

@Component({
  selector: 'app-registration-confirm',
  templateUrl: './registration-confirm.component.html',
  styleUrls: ['./registration-confirm.component.css']
})
export class RegistrationConfirmComponent implements OnInit {

  constructor(private route: ActivatedRoute, private userRegistrationService: UserAuthService) { }

  ngOnInit(): void {
    if(this.route.snapshot.paramMap.get('email') != null)

      this.userRegistrationService.registerConfirm(this.route.snapshot.paramMap.get('email')).subscribe((res:any) => {
        console.log(res)
      })
  }


}
