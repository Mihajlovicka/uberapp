import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
@Component({
  selector: 'app-cancel-drive-reason-dialog',
  templateUrl: './cancel-drive-reason-dialog.component.html',
  styleUrls: ['./cancel-drive-reason-dialog.component.css']
})
export class CancelDriveReasonDialogComponent{

  constructor(
    public dialogRef: MatDialogRef<CancelDriveReasonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
