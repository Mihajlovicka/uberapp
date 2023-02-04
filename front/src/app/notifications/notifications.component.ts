import {Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {AppService} from "../app.service";
import {Notification} from "../model/notification.model"
import {Role, Status, User} from "../model/user.model";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  // @ts-ignore
  @ViewChild('notificationDrawer') private drawer: any;
  @Output() openNotifications = new EventEmitter<boolean>();
  @Output() notificationsNumber = new EventEmitter<number>();
  @Output() openSupportChat = new EventEmitter<boolean>();

  notifications: Array<Notification> = [];
  onlyNotOpened: boolean = false;
  logged_user: User = {
    username: '',
    name: '',
    surname: '',
    email: '',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  }


  private stompClient: any;


  constructor(private appService: AppService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getLoggedUser();
  }


  initializeWebSocketConnection(email: string) {
    let ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, () => {
      that.stompClient.subscribe('/notify/' + email, (message: { body: string }) => {
        console.log(message.body);
        this.loadNotifications();
      });
    });
  }

  getLoggedUser() {
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      console.log(resp);


      this.initializeWebSocketConnection(this.logged_user.email);

      this.loadNotifications();
    });
  }

  loadNotifications() {
    console.log("RELOADING");

    if (this.logged_user.email != "") {

      this.appService.getNotificationsForUser(this.logged_user.email).subscribe((resp: Notification[]) => {
        this.notifications = resp;
        console.log(resp);
        this.getNumberOfUnopenedNotifications();
      });
    }

  }

  openNotification(notification: Notification) {
    this.appService.openNotification(notification.id).subscribe((resp: any[]) => {
      console.log(resp);

      if (notification.href != "") {
        if (notification.href == "/messages-client") {
          this.openSupport();
        } else {
          this.router.navigate([notification.href]);
        }
      }
      this.openAllNotifications();
    });
  }

  public openSupport(){


    this.openSupportChat.emit(true);
  }

  public openAllNotifications() {
    this.openNotifications.emit(true);
  }

  public getNumberOfUnopenedNotifications() {
    let number: number = 0
    if(this.notifications === undefined ) return
    for (let notification of this.notifications) {
      if (!notification.opened) number++;
    }
    this.notificationsNumber.emit(number);
  }
}
