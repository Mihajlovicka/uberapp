import { LiveAnnouncer } from '@angular/cdk/a11y';
import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatSort, Sort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import { Drive } from '../model/drive.model';
import {Role, Status, User} from "../model/user.model";
import {AppService} from "../app.service";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-drive-history-client',
  templateUrl: './drive-history-client.component.html',
  styleUrls: ['./drive-history-client.component.css']
})
export class DriveHistoryClientComponent implements AfterViewInit, OnInit  {
  displayedColumns: string[] = ['firstStop', 'lastStop', 'price', 'startDate', 'endDate'];

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
      cell: (element: Drive) => `${element.price}`,
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
  ];

  drives :Drive[] = [];

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
              private appService: AppService) {}


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
    this.appService.getAllDrivesClient(this.logged_user.email).subscribe((resp: any) => {

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
        case 'lastStop': return this.compare(a.stops[b.stops.length-1].name, b.stops[b.stops.length-1].name, isAsc);
        case 'price': return this.compare(a.price, b.price, isAsc);
        case 'startDate': return this.compare(a.date, b.date, isAsc);
        case 'endDate': return this.compare(a.date, b.date, isAsc);
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

}
