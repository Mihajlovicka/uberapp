import { User } from "./user.model";
import {Image} from "./image.model";

interface UsersChatDisplayInterface{
  user: User;
  image: Image;
  client: boolean;
  hasMessages:boolean;
}

export class UsersChatDisplay implements UsersChatDisplayInterface{
  public user: User;
  public image: Image;
  public client: boolean;
  public hasMessages:boolean;

  constructor(clientsInt: UsersChatDisplayInterface){
    this.user = clientsInt.user;
    this.image = clientsInt.image;
    this.client = clientsInt.client;
    this.hasMessages = clientsInt.hasMessages;
  }
}
