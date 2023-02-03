import {AfterViewChecked, Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {Role, Status, User} from "../model/user.model";
import {Message} from "../model/message.model";
import {AppService} from "../app.service";
import {ActivatedRoute} from "@angular/router";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

@Component({
  selector: 'app-messages-client',
  templateUrl: './messages-client.component.html',
  styleUrls: ['./messages-client.component.css']
})
export class MessagesClientComponent implements OnInit,AfterViewChecked {

  // @ts-ignore
  @ViewChild('scrollMe') private myScrollContainer: ElementRef;
  @Output() messagesNumber = new EventEmitter<number>();

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
      name:'SRRRRR',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    reciever:{
      username:'',
      name:'SRRRRR',
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
  users:any=["jaa", "tii"];


  constructor(private appService: AppService,
              private route: ActivatedRoute
  ) { }



  loadReciever(){
    this.messageData.reciever=this.messageData.sender;
  }
  getLoggedUser(){
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      this.messageData.sender=this.logged_user;
      //this.messageData.reciever=this.logged_user;
      console.log(resp);
      console.log(this.logged_user.role)
      this.reloadMessages();
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
    this.initializeWebSocketConnection();
    this.getLoggedUser();
    this.loadReciever();


  }
  reloadMessages(){
    console.log("REKOADING")
    this.appService.getMessagesForUser(this.logged_user.email).subscribe((resp: Message[]) => {
      this.messages = resp;
      console.log(resp);
      this.getNumberOfUnopenedMessages();
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
    this.loadReciever();

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
  public getNumberOfUnopenedMessages(){
    let number:number = 0
    for (let message of this.messages) {
      if(!message.read) number++;
    }
    this.messagesNumber.emit(number);
  }
}
