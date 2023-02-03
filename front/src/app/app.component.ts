import {Component, ElementRef, ViewChild} from '@angular/core';
import { AppService } from './app.service';
import {NotificationsComponent} from "./notifications/notifications.component";
import {UserAuthService} from "./service/user-auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // @ts-ignore
  @ViewChild('NotificationsDrawer') private drawer: any;
  @ViewChild('header') private header: any;

  showSupport: boolean = true;
  title = 'front';
  notifikacije:boolean = false;
  isLogged: boolean = false;

  poruka: string = "";

  constructor(private appService: AppService, public service: UserAuthService) {

  }

  ngOnInit(): void {

  }

  public isLoggedIn(){
    return this.service.isLoggedIn();
  }
  funkcija($event: boolean) {
    if($event){
      this.notifikacije = true;

     this.drawer.toggle();
    }

  }
  openChat($event: boolean){
    if($event){
      this.showSupport = !this.showSupport;
      if(!this.showSupport){
        this.appService.openMessages().subscribe((resp: any[]) => {
          console.log(resp);


        });
      }
    }
  }

  newNotifications($event: number) {
    this.header.setNumberOfNotifications($event);
  }
  newMessage($event: number) {
    this.header.setNumberOfMessages($event);
  }
}
