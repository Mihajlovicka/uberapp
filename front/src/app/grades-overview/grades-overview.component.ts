import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Drive} from "../model/drive.model";
import {formatDate} from "@angular/common";
import {Role, Status, User} from "../model/user.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../app.service";
import {Grade} from "../model/grade.model";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";


export interface DialogData {
  driveID: number;
}
@Component({
  selector: 'app-grades-overview',
  templateUrl: './grades-overview.component.html',
  styleUrls: ['./grades-overview.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class GradesOverviewComponent implements OnInit {

  read: boolean=true;
  displayedColumns: string[] = ['grader', 'gradeDriver', 'gradeCar'];
  columnsToDisplayWithExpand: string[] = [...this.displayedColumns, 'expand'];
  // @ts-ignore
  expandedElement: Grade | null;

  driveID: number = 0;
  grades: Grade[] = [];


  dataSource: MatTableDataSource<Grade> = new MatTableDataSource<Grade>();

  constructor(private _liveAnnouncer: LiveAnnouncer,
              private route: ActivatedRoute,
              public dialogRef: MatDialogRef<GradesOverviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private appService: AppService) {
  }


  ngOnInit(): void {
    this.loadGrades();

  }

  ngAfterViewInit() {
    this.dataSource = new MatTableDataSource(this.grades);


  }

  ngAfterViewChecked() {
    this.dataSource = new MatTableDataSource(this.grades);

  }

  loadGrades() {
    this.route.queryParams.subscribe(params => {

      this.driveID = params['driveID']
    });

    this.appService.getGradesForDrive(this.data.driveID).subscribe((resp: any) => {

      console.log(resp);
      this.grades = resp;
      this.dataSource = new MatTableDataSource(this.grades);
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
