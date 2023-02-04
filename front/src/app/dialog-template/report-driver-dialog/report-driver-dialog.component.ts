import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-report-driver-dialog',
  templateUrl: './report-driver-dialog.component.html',
  styleUrls: ['./report-driver-dialog.component.css']
})
export class ReportDriverDialogComponent{
  constructor(
    public dialogRef: MatDialogRef<ReportDriverDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
