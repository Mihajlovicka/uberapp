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
  selectedClients: ClientsAccount[]|any=[];
 

 

  constructor(private service: AppService) {
    this.filteredUsers = this.userCtrl.valueChanges.pipe(
      startWith(''),
      map(user => (user ? this._filterUsers(user) : this.users.slice())),
    );
  }

  private _filterUsers(value: string): ClientsAccount[] {
    const filterValue = value.toLowerCase();

    return this.users.filter(user => user.user.email.toLowerCase().includes(filterValue) || user.user.name.toLowerCase().includes(filterValue) || user.user.surname.toLowerCase().includes(filterValue));
  }

  isSelected(client:string): boolean{
  
    for(let i = 0; i<this.selectedClients.length; i++){
      if(this.selectedClients[i].user.email === client) return true;
    }

     return false;
  }

  addSelected(client: string){

    if(!this.isSelected(client)){
     

      this.selectedClients.push(this.users.find(user => user.user.email === client));
      }
    
    else{
      alert("Vec ga imas majmune :D")
    }
    

  }

  clientChosen(e:any){
    if(this.selectedClients.length>7){
      alert("Ne moze dalje druuze");
    }
    else{
     
      this.addSelected(this.userCtrl.value);
         
    }
      

      }

  ngOnInit(): void {
    this.loadAllClients();
  }

  loadAllClients(){
    this.service.getAllClients().subscribe((resp: ClientsAccount[]) => {
     
      this.users = resp;
    
    })
  }

  removeClient(client: ClientsAccount){
    this.selectedClients.splice(this.selectedClients.indexOf(client), 1)
  }

}
