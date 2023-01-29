import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable  } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { AppService } from '../app.service';
import { ClientsAccount } from '../model/clientsAccount.model';
import { Drive } from '../model/drive.model';



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


  @Input() drive:Drive={
    stops: [],
    distance: 0,
    duration: 0,
    price: 0,
    clients: [],
    seats: 5,
    baby: 0,
    babySeats:0,
    pets:0,
    owner: null
  }
 

  @Output() setDrive = new EventEmitter<Drive>();

  constructor(private service: AppService) {
    this.filteredUsers = this.userCtrl.valueChanges.pipe(
      startWith(''),
      map(user => (user ? this._filterUsers(user) : this.users.slice())),
    );
  }

  private _filterUsers(value: string): ClientsAccount[] {
    const filterValue = value.toLowerCase();

    if(value.includes(" ")){
     return this.users.filter(user => (user.user.name.toLowerCase().includes(filterValue.split(" ")[0]) && user.user.surname.toLowerCase().includes(filterValue.split(" ")[1])) || (user.user.name.toLowerCase().includes(filterValue.split(" ")[1]) && user.user.surname.toLowerCase().includes(filterValue.split(" ")[0])))
    }

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
    if(this.selectedClients.length>this.drive.seats-this.drive.baby-this.drive.pets-3){
      alert("Ne moze dalje druuze");
    }
    else{
      this.addSelected(this.userCtrl.value);
      this.drive.clients = this.selectedClients;
      this.setDrive.emit(this.drive);
         
    }
  }

  ngOnInit(): void {
    this.loadAllClients();
    
  }

  //get logged client - drives owner
  getDriveOwner(){
    this.service.getLoggedUser().subscribe(
      (resp: ClientsAccount) => {
        if(resp != null || resp != undefined){
          this.drive.owner = resp;
          console.log(this.drive)
        }
      }
    )
  }

  loadAllClients(){
    this.service.getAllClients().subscribe((resp: ClientsAccount[]) => {
     
      this.users = resp;
    
    })

    this.getDriveOwner();
  }

  removeClient(client: ClientsAccount){
    this.selectedClients.splice(this.selectedClients.indexOf(client), 1)
  }

}
