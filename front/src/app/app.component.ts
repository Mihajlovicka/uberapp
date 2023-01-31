import {Component, ElementRef, ViewChild} from '@angular/core';
import { AppService } from './app.service';
import {NotificationsComponent} from "./notifications/notifications.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // @ts-ignore
  @ViewChild('NotificationsDrawer') private drawer: any;
  @ViewChild('header') private header: any;

  title = 'front';
  notifikacije:boolean = false;

  poruka: string = "";

  constructor(private appService: AppService) {

  }

  ngOnInit(): void {


  }


  funkcija($event: boolean) {
    if($event){
      this.notifikacije = true;
     this.drawer.toggle();
    }

  }

  newNotifications($event: number) {
    this.header.setNumberOfNotifications($event);
  }
}
