

interface PasswordChangeInterface{
  email: string;
  oldPassword: string;
  newPassword: string;
}

export class PasswordChange implements PasswordChangeInterface{
  public email:string;
  public oldPassword:string;
  public newPassword:string;
  constructor(passwordChangeInt:PasswordChangeInterface) {
    this.email = passwordChangeInt.email;
    this.oldPassword = passwordChangeInt.oldPassword;
    this.newPassword = passwordChangeInt.newPassword;
  }
}
