import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Drive} from "../model/drive.model";
import {formatDate} from "@angular/common";
import {Role, Status, User} from "../model/user.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {ActivatedRoute, Router} from "@angular/router";
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

  read: boolean = true;
  displayedColumns: string[] = ['grader', 'gradeDriver', 'gradeCar'];
  columnsToDisplayWithExpand: string[] = [...this.displayedColumns, 'expand'];
  // @ts-ignore
  expandedElement: Grade | null;

  driveID: number = 0;
  grades: Grade[] = [];
  logged_user: User = {
    username: '',
    name: '',
    surname: '',
    email: '',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  }

  dataSource: MatTableDataSource<Grade> = new MatTableDataSource<Grade>();

  constructor(private _liveAnnouncer: LiveAnnouncer,
              private route: ActivatedRoute,
              public dialogRef: MatDialogRef<GradesOverviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private appService: AppService,
              private router: Router) {
  }


  ngOnInit(): void {
    this.getLoggedUser();

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

  getLoggedUser() {
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      this.loadGrades();
      console.log(resp);
      console.log(this.logged_user.role)
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  isGraded() {
    console.log('ROLA' + this.logged_user.role);
    if (this.logged_user.role === "ROLE_CLIENT") {
      for (let grade of this.grades) {
        if (grade.drive.owner.user.email == this.logged_user.email) {
          return true;
        }
        for (let passenger of grade.drive.passengers) {
          if (passenger.passengerEmail == this.logged_user.email) {
            return true;
          }
        }
      }
    }
    if(this.logged_user.role === "ROLE_ADMINISTRATOR" || this.logged_user.role === "ROLE_DRIVER") return true;
    return false;
  }

  getRoute(){
    console.log(this.data.driveID)
    console.log('/drive-rating?driveID=' + this.data.driveID)
    return '/drive-rating?driveID=' + this.data.driveID;
  }
  addGrade() {
    this.router.navigate(['/drive-rating'], {queryParams: {driveID : this.data.driveID}} );
    this.onNoClick();
  }
}
