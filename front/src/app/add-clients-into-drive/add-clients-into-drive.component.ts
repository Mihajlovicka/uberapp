import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable  } from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { AppService } from '../app.service';
import { ClientsAccount } from '../model/clientsAccount.model';



@Component({
  selector: 'app-add-clients-into-drive',
  templateUrl: './add-clients-into-drive.component.html',
  styleUrls: ['./add-clients-into-drive.component.css']
})



export class AddClientsIntoDriveComponent implements OnInit {
  users: ClientsAccount[] = [];
  userCtrl = new FormControl('');
  filteredUsers: Observable<ClientsAccount[]>;

 

  constructor(private service: AppService) {
    this.filteredUsers = this.userCtrl.valueChanges.pipe(
      startWith(''),
      map(user => (user ? this._filterUsers(user) : this.users.slice())),
    );
  }

  private _filterUsers(value: string): ClientsAccount[] {
    const filterValue = value.toLowerCase();

    return this.users.filter(user => user.user.email.toLowerCase().includes(filterValue));
  }

  clientChosen(e:any){
  
    console.log(this.userCtrl.value)
  }

  ngOnInit(): void {
    this.loadAllClients();
  }

  loadAllClients(){
    this.service.getAllClients().subscribe((resp: ClientsAccount[]) => {
     
      this.users = resp;
      console.log(resp);
    })
  }

}
