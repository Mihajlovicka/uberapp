import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Drive} from "../model/drive.model";
import {formatDate} from "@angular/common";
import {Role, Status, User} from "../model/user.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {AppService} from "../app.service";
import {animate, state, style, transition, trigger } from '@angular/animations';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-drive-history-driver',
  templateUrl: './drive-history-driver.component.html',
  styleUrls: ['./drive-history-driver.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class DriveHistoryDriverComponent implements AfterViewInit, OnInit  {
  displayedColumns: string[] = ['firstStop', 'lastStop', 'price', 'startDate', 'endDate','status'];
  columnsToDisplayWithExpand: string[] = [...this.displayedColumns, 'expand'];
  // @ts-ignore
  expandedElement: Drive | null;
  columns = [
    {
      columnDef: 'firstStop',
      header: 'Pocetna stanica',
      cell: (element: Drive) => `${element.stops[0].name}`,
    },
    {
      columnDef: 'lastStop',
      header: 'Poslednja stanica',
      cell: (element: Drive) => `${element.stops[element.stops.length-1].name}`,
    },
    {
      columnDef: 'price',
      header: 'Cena',
      cell: (element: Drive) => `${element.price.toFixed(2)}`,
    },
    {
      columnDef: 'startDate',
      header: 'Datum pocetka voznje',
      cell: (element: Drive) => `${formatDate((new Date(element.date)), 'dd/MM/yyyy hh:mm', 'en')}`,
    },
    {
      columnDef: 'endDate',
      header: 'Datum kraja voznje',
      cell: (element: Drive) => `${formatDate(new Date(element.date), 'dd/MM/yyyy hh:mm', 'en')}`,
    },
    {
      columnDef: 'status',
      header: 'Status voznje',
      cell: (element: Drive) => `${element.driveStatus=="DRIVE_ENDED"?"Uspesna voznja":(element.driveStatus==="DRIVE_REJECTED"?"Odbijena voznja":"Neuspesna voznja")}`,
    },
  ];

  drives :Drive[] = [];
  email = "";
  logged_user: User = {
    username:'',
    name:'',
    surname:'',
    email:'',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  }

  dataSource: MatTableDataSource<Drive> = new MatTableDataSource<Drive>();
// @ts-ignore
  @ViewChild(MatSort) sort: MatSort;
  private sortedData: Drive[] = [];
  constructor(private _liveAnnouncer: LiveAnnouncer,
              private route: ActivatedRoute,
              private appService: AppService,
              private router: Router) {}


  ngOnInit(): void {
    this.getLoggedUser();

  }
  ngAfterViewInit() {
    this.dataSource = new MatTableDataSource(this.drives);
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
    this.dataSource = new MatTableDataSource(this.drives);

  }
  getLoggedUser(){
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;
      this.loadDrives();
      console.log(resp);
      console.log(this.logged_user.role)
    });
  }
  loadDrives(){
    this.route.queryParams.subscribe(params => {

      this.email = params['email']
    });
    if(this.logged_user.role!=="ROLE_ADMINISTRATOR"){
      this.email=this.logged_user.email;
    }
    this.appService.getAllPastDrivesClient(this.email).subscribe((resp: any) => {

      console.log(resp);
      this.drives = resp;
      this.dataSource = new MatTableDataSource(this.drives);
    });
  }

  public sortData(sort: Sort) {
    const data = this.drives.slice();
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
        case 'firstStop': return this.compare(a.stops[0].name, b.stops[0].name, isAsc);
        case 'lastStop': return this.compare(a.stops[a.stops.length-1].name, b.stops[b.stops.length-1].name, isAsc);
        case 'price': return this.compare(a.price, b.price, isAsc);
        case 'startDate': return this.compare(a.date, b.date, isAsc);
        case 'endDate': return this.compare(a.date, b.date, isAsc);
        case 'status': return this.compare(a.driveStatus, b.driveStatus, isAsc);
        default: return 0;
      }
    });
    this.drives = this.sortedData;
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
    this.router.navigate(['/drive/'+drive.id]);
  }
}
