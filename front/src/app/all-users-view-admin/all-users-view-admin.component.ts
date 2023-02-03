import {Component, OnInit, ViewChild} from '@angular/core';
import {Drive} from "../model/drive.model";
import {formatDate} from "@angular/common";
import {Role, Status, User} from "../model/user.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {ActivatedRoute, Router} from "@angular/router";
import {AppService} from "../app.service";
import {UsersChatDisplay} from "../model/usersChatDisplay.model";

@Component({
  selector: 'app-all-users-view-admin',
  templateUrl: './all-users-view-admin.component.html',
  styleUrls: ['./all-users-view-admin.component.css']
})
export class AllUsersViewAdminComponent implements OnInit {
  displayedColumns: string[] = ['userName', 'userSurname', 'userEmail', 'userRole'];
  columnsToDisplayWithExpand: string[] = [...this.displayedColumns, 'expand'];
  // @ts-ignore

  columns = [

    {
      columnDef: 'userName',
      header: 'Ime korisnika',
      cell: (element: UsersChatDisplay) => `${element.user.name}`,
    },
    {
      columnDef: 'userSurname',
      header: 'Prezime korisnika',
      cell: (element: UsersChatDisplay) => `${element.user.surname}`,
    },
    {
      columnDef: 'userEmail',
      header: 'Email korisnika',
      cell: (element: UsersChatDisplay) => `${element.user.email}`,
    },
    {
      columnDef: 'userRole',
      header: 'Uloga korisnika',
      cell: (element: UsersChatDisplay) => `${element.user.role=="ROLE_DRIVER"?"Vozac":"Klijent"}`,
    },


  ];

  users :UsersChatDisplay[] = [];
  email = "";
  logged_user: User = {
    username:'',
    name:'',
    surname:'',
    email:'',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  }

  dataSource: MatTableDataSource<UsersChatDisplay> = new MatTableDataSource<UsersChatDisplay>();
// @ts-ignore
  @ViewChild(MatSort) sort: MatSort;
  private sortedData: UsersChatDisplay[] = [];
  constructor(private _liveAnnouncer: LiveAnnouncer,
              private route: ActivatedRoute,
              private appService: AppService,
              private router: Router) {}


  ngOnInit(): void {
    this.getLoggedUser();

  }
  ngAfterViewInit() {
    this.dataSource = new MatTableDataSource(this.users);
    this.dataSource.sort = this.sort;

    const sortState: Sort = {
      active: 'releaseDate',
      direction: 'desc'
    };
    this.sort.active = sortState.active;
    this.sort.direction = sortState.direction;
    this.sort.sortChange.emit(sortState);

  }
  ngAfterViewChecked() {
    this.dataSource = new MatTableDataSource(this.users);

  }
  getLoggedUser(){
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      this.getUsers();
      console.log(resp);
      console.log(this.logged_user.role)
    });
  }
  getUsers(){
    this.appService.getUsersChatDisplay().subscribe((resp: UsersChatDisplay[] ) => {
      this.users = resp;
       this.dataSource = new MatTableDataSource(this.users);
       console.log(resp);
      console.log(this.logged_user.role)

    });
  }


  public sortData(sort: Sort) {
    const data = this.users.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      console.log("Sorting\n" + sort.active)
      console.log(data);
      console.log(sort.direction)
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'userName': return this.compare(a.user.name, b.user.name, isAsc);
        case 'userSurname': return this.compare(a.user.surname, b.user.surname, isAsc);
          case 'userEmail': return this.compare(a.user.email, b.user.email, isAsc);
        case 'userRole': return this.compare(a.user.role, b.user.role, isAsc);
        default: return 0;
      }
    });
    this.users = this.sortedData;
  }

  compare(a: number | string | Date, b: number | string | Date, isAsc: boolean) {
    console.log(a,b,a<b)
    console.log((a < b ? -1 : 1) * (isAsc ? 1 : -1))
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  ispisi(row:any){
    console.log(row);
  }
  /** Announce the change in sort state for assistive technology. */
  openDrive(drive:Drive){
    this.ispisi(drive);
  }

  openProfile(element:UsersChatDisplay) {
    if (element.user.role === "ROLE_CLIENT") {
      this.router.navigate(['/clientProfile'],{queryParams:{email:element.user.email}});
    }
    if (element.user.role === "ROLE_DRIVER"){
      this.router.navigate(['/driversProfile'],{queryParams:{email:element.user.email}});

    }
  }
  openReport(element:UsersChatDisplay) {
    this.router.navigate(['/reports/'+element.user.email]);
  }

  openDriveHistory(element: UsersChatDisplay) {
    if (element.user.role === "ROLE_CLIENT") {
      this.router.navigate(['/drive-history-client'],{queryParams:{email:element.user.email}});
    }
    if (element.user.role === "ROLE_DRIVER"){
      this.router.navigate(['/drive-history-driver'],{queryParams:{email:element.user.email}});

    }
  }
}
