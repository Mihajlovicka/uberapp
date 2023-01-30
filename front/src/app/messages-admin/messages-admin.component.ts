import {AfterViewChecked, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Role, Status, User} from "../model/user.model";
import {Message} from "../model/message.model";
import {AppService} from "../app.service";
import {ActivatedRoute} from "@angular/router";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {UsersChatDisplay} from "../model/usersChatDisplay.model";

@Component({
  selector: 'app-messages-admin',
  templateUrl: './messages-admin.component.html',
  styleUrls: ['./messages-admin.component.css']
})
export class MessagesAdminComponent implements OnInit,AfterViewChecked {

  // @ts-ignore
  @ViewChild('scrollMe') private myScrollContainer: ElementRef;

  showClients:boolean = true;
  showDrivers:boolean = true;
  notRead:boolean = false;
  users: Array<UsersChatDisplay> = [];
  private stompClient: any;
  logged_user: User = {
    username:'',
    name:'',
    surname:'',
    email:'',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  }

  messageData:Message={
    sender:{
      username:'',
      name:'',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    reciever:{
      username:'',
      name:'Odaberite chat',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    message:'',
    read: false
  }
  username:string="";
  messages:Array<Message>=[];
  message:string='';
  logged:number=0;


  constructor(private appService: AppService,
              private route: ActivatedRoute
  ) { }



  loadReciever(){
    this.route.queryParams.subscribe(params => {

      this.messageData.reciever.email = params['email']
    });
    this.appService.getUser(this.messageData.reciever.email).subscribe((resp: User) => {
      this.messageData.reciever = resp;
      console.log(resp);
      console.log(this.logged_user.role)

    });

  }

  isUserShown(u:UsersChatDisplay){


    if(this.getRole(u.user.role) == "Klijent"){

      return this.showClients;
    }
    else{
      return this.showDrivers;
    }
  }

  getUsers(){
    this.appService.getUsersChatDisplay().subscribe((resp: UsersChatDisplay[] ) => {
      this.users = resp;
      console.log(resp);
      console.log(this.logged_user.role)

    });
  }

  getLoggedUser(){
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      this.messageData.sender=this.logged_user;
      //this.messageData.reciever=this.logged_user;
      console.log(resp);
      console.log(this.logged_user.role)

    });
  }


  ngAfterViewChecked() {
    this.scrollToElement();
  }
  scrollToElement(): void {
    this.myScrollContainer.nativeElement.scroll({
      top: this.myScrollContainer.nativeElement.scrollHeight+82,
      left: 0,
      behavior: 'smooth'
    });
  }
  ngOnInit(): void {
    this.getUsers();
    this.initializeWebSocketConnection();
    this.getLoggedUser();
   // this.loadReciever();
   // this.reloadMessages();

  }
  reloadMessages() {
    console.log("REKOADING");
    if (this.messageData.reciever.email != "") {

      this.appService.getMessagesForUser(this.messageData.reciever.email).subscribe((resp: Message[]) => {
        this.messages = resp;
        console.log(resp);

      });
    }
  }

  getRole(role:Role):string{
     let r : any = role;
    if(r.name === "ROLE_CLIENT") return "Klijent";
    if(r.name === "ROLE_DRIVER") return "Vozac";
    if(r.name === "ROLE_ADMINISTRATOR") return "Admin";
    else return "";
  }
  changeChat(email:string){
    this.appService.getUser(email).subscribe((resp: User) => {
      this.messageData.reciever = resp;
      console.log(resp);
      console.log(this.logged_user.role)
      this.reloadMessages();
    });

  }
  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;

    this.stompClient.connect({}, () => {
      that.stompClient.subscribe('/message/support', (message: { body: string }) => {
        console.log(message.body);
        this.reloadMessages();
      });
    });
  }

  submit():void{
    this.messageData.message = this.message;


    console.log("SENDING:")
    console.log(this.messageData);
    this.appService.sendMessage(this.messageData).subscribe((resp: any) => {

      console.log(resp);

    })
    console.log(this.messageData.message);
    if(this.logged==1){
      this.logged=0;
    }
    else{
      this.logged=1;
    }
    this.messages.push(this.messageData);
    if(this.messages.length>5) {
      this.scrollToElement();
    }
    this.message='';
  }
}
