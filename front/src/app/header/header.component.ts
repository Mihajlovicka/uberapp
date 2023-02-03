import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {UserAuthService} from "../service/user-auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  numberOfNotifications: any = "";
  numberOfMessages: any = "";
  role: string|null = "";
  @Output() openNotifications = new EventEmitter<boolean>();
  @Output() openSupportChat = new EventEmitter<boolean>();
  constructor(
    public service: UserAuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
  this.role = this.service.getRole();
  }

  public isLoggedIn(){
    return this.service.isLoggedIn();
  }

  public isAdmin(){
    return this.service.getRole() == "ROLE_ADMINISTRATOR";
  }
  public isClient(){
    return this.service.getRole() == "ROLE_CLIENT";
  }
  public isDriver(){
    return this.service.getRole() == "ROLE_DRIVER";
  }

  public logout(){
    this.service.clear();
    this.router.navigate([''])
  }

  public openAllNotifications(){
    this.openNotifications.emit(true);
  }
  public openSupport(){

    this.openSupportChat.emit(true);
  }

  setNumberOfNotifications(num:number){
    this.numberOfNotifications = num===0?"":num;

  }
  setNumberOfMessages(num:number){
    this.numberOfMessages = num===0?"":num;

  }

}
