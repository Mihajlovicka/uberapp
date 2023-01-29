import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {PasswordChange} from "../model/passwordChange.model";
import {Status} from "../model/user.model";
import {AppService} from "../app.service";

export interface DialogData {
  email: string;
}

@Component({
  selector: 'app-password-change',
  templateUrl: './password-change.component.html',
  styleUrls: ['./password-change.component.css']
})
export class PasswordChangeComponent {

  newPasswordRepeat: string = "";
  passwordChangeData: PasswordChange = {
    email: this.data.email,
    oldPassword: "",
    newPassword: ""
  };

  constructor(
    public dialogRef: MatDialogRef<PasswordChangeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private appService: AppService,
  ) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
  }

  changePassword() {
    if (this.passwordChangeData.oldPassword != "" && this.passwordChangeData.newPassword != "") {
      if (this.newPasswordRepeat === this.passwordChangeData.newPassword) {


        this.appService.changePassword(this.passwordChangeData).subscribe((resp: any) => {

          console.log(resp);
          this.appService.openErrorDialog("Lozinka je uspesno promenjena.");
          this.onNoClick();
        });
      } else {
        this.appService.openErrorDialog("Lozinke se ne poklapaju.");
      }

    }
    else{
      this.appService.openErrorDialog("Sva polja moraju biti unesena.");

    }
  }

}
