import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";


@Component({
  selector: 'app-routes-dialog',
  templateUrl: './routes-dialog.component.html',
  styleUrls: ['./routes-dialog.component.css']
})
export class RoutesDialogComponent {

  routeOption: any = {
    option: '',
    description: ''
  };
  options: any[] = [
    {desc: 'Prati izabran redosled stanica', option: 'same'},
    {desc: 'Optimizuj put vremenski', option: 'duration'},
    {desc: 'Optimizuj put po razdaljini', option: 'distance'}];

  constructor(
    public dialogRef: MatDialogRef<RoutesDialogComponent>,
  ) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
