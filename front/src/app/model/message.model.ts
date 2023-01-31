import { User } from "./user.model";



interface MessageInterface{
  sender: User;
  reciever: User;
  message: string;
  read: boolean;
}

export class Message implements MessageInterface{
  public sender: User;
  public reciever: User;
  public message: string;
  public read: boolean;


  constructor(messageInt: MessageInterface){
    this.sender = messageInt.sender;
    this.reciever = messageInt.reciever;
    this.message = messageInt.message;
    this.read = messageInt.read;
  }
}
