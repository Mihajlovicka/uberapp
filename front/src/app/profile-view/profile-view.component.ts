import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { AppService } from '../app.service';
import { ClientsAccount } from '../model/clientsAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.css']
})
export class ProfileViewComponent implements OnInit {

  izmena:string="Izmeni podatke"
  email:string="";
  clientsAccount: ClientsAccount = {
    user:{
      name:'SRRRRR',
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
    phone:''
  }


  constructor(private appService: AppService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      
      this.email = params['email']
     });

     this.appService.getClient(this.email).subscribe((resp: ClientsAccount) => {
      this.clientsAccount = resp;
      //alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
      console.log(this.clientsAccount);
      console.log(resp);
    });
    //document.getElementById("inputFirstName")?.setAttribute('value',this.clientsAccount.user.email);
  }

}
