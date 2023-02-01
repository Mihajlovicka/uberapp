import { User } from "./user.model";

interface NotificationInterface{
  id: bigint;
  userToNotify: User;
  title: string;
  message: string;
  opened: boolean;
  href: string
}

export class Notification implements NotificationInterface{
  public id: bigint;
  public userToNotify: User;
  public title:string;
  public message: string;
  public opened: boolean;
  public href: string


  constructor(notificationInterface: NotificationInterface){
    this.id = notificationInterface.id;
    this.userToNotify = notificationInterface.userToNotify;
    this.title = notificationInterface.title;
    this.href = notificationInterface.href;
    this.message = notificationInterface.message;
    this.opened = notificationInterface.opened;
  }
}
