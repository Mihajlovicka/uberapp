import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { BankAccount } from '../model/bankAccount.model';
import { BankTransaction, TransactionStatus, TransactionType } from '../model/bankTransaction.model';

@Component({
  selector: 'app-respond-to-payment-request',
  templateUrl: './respond-to-payment-request.component.html',
  styleUrls: ['./respond-to-payment-request.component.css']
})
export class RespondToPaymentRequestComponent implements OnInit {

  constructor(private service: AppService, private route: ActivatedRoute) { }

  price: number = 0;
  transactionId: number=0;
  transaction: BankTransaction = {
    amount: 0,
    receiver: '',
    sender: '',
    transactionStatus: TransactionStatus.FINALIZED,
    transactionType: TransactionType.INFLOW
  }

  clientsBankAccount: BankAccount = {
    balance: 0,
    bankAccountNumber: '',
    verificationEmail: '',
    ownerName: '',
    ownerSurname: ''
  }

  

  ngOnInit(): void {
   this.getTransactionId();
   //this.getTransactionData();
   //this.getBankAccount();
  }

  getTransactionId(){
    this.route.queryParams.subscribe(params => {

      this.transactionId = Number(this.route.snapshot.paramMap.get('id'))
      console.log(this.transactionId);
     
     });

     this.getTransactionData();
  }

 getTransactionData(){
  this.service.getTransictionInfo(this.transactionId).subscribe((resp: BankTransaction) => {
      this.transaction = resp;
      console.log(this.transaction);
      this.getBankAccount(this.transaction.sender);
  })
  
 }

 getBankAccount(s: string){
    this.service.getClientsBankAccount(s).subscribe(
      (resp: BankAccount)=>{
          this.clientsBankAccount = resp;
          console.log(this.clientsBankAccount);
      }
    )
}

 acceptTransaction(){
    this.service.acceptPayment(this.transactionId).subscribe((resp: BankTransaction)=>{
      this.transaction = resp;
      console.log(resp);
    })
 }

 declineTransaction(){
    this.service.declinePayment(this.transactionId, this.clientsBankAccount).subscribe(
      (resp: BankTransaction) => {
        this.transaction = resp;
        console.log(this.transaction);
      }

    )
 }

}
