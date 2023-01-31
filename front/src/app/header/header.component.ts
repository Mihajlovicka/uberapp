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
  @Output() openNotifications = new EventEmitter<boolean>();
  constructor(
    public service: UserAuthService,
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

  public openAllNotifications(){
    this.openNotifications.emit(true);
  }
  setNumberOfNotifications(num:number){
    this.numberOfNotifications = num===0?"":num;

  }

}
