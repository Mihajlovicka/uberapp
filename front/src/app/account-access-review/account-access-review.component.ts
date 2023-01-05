import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route } from '@angular/router';
import { AppService } from '../app.service';
import { BankStatus, ClientsAccount } from '../model/clientsAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-account-access-review',
  templateUrl: './account-access-review.component.html',
  styleUrls: ['./account-access-review.component.css']
})
export class AccountAccessReviewComponent implements OnInit {

  email : string = '';

  clientsAccount: ClientsAccount={
    user:{
      email:'',
      role:Role.ROLE_CLIENT,
      name:'',
      surname:'',
      status:Status.ACTIVE
    },
    address:{
      city:'',
      street:'',
      number:''
    },
    phone:'',
    picture:'',
    clientsBankAccount:{
      balance:0,
      ownerName:'',
      ownerSurname:'',
      bankAccountNumber:'',
      verificationEmail:''
    },
    bankStatus:BankStatus.NOTCONFIRMED
  }
  

  constructor(private activatedRoute: ActivatedRoute, private service: AppService) { }

  ngOnInit(): void {
    this.loadData()
  }

  loadData(){
    //uzmi id iz url-a
    this.activatedRoute.params.subscribe(paramsId => {
      this.email = paramsId['email'];
      console.log(this.email);
    });

    this.service.getClient(this.email).subscribe((resp: ClientsAccount) => {
      this.clientsAccount = resp;
      //alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
      console.log(this.clientsAccount);
      console.log(resp);
    });
  }


  
  accept(){
    this.service.acceptBankAccountAccess(this.clientsAccount).subscribe((resp: ClientsAccount) => {
      this.clientsAccount = resp;
      //alert("Uspesno ste se registrovali. Pogledajte email za verifikaciju naloga.")
      console.log(this.clientsAccount);
      console.log(resp);
    });
  }

  decline(){
    this.service.declineBankAccountAccess(this.clientsAccount).subscribe((resp: ClientsAccount) => {
      this.clientsAccount = resp;
      console.log(this.clientsAccount);
      console.log(resp);
    })
  }
  
  }
