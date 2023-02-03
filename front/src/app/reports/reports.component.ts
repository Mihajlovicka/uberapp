import {Component, OnInit, ViewChild} from '@angular/core';
import {FormGroup, FormControl} from '@angular/forms';
import {AppService} from "../app.service";
import { Drive } from '../model/drive.model';
import {DatePipe} from "@angular/common";
import {max} from "rxjs";
import {UserAuthService} from "../service/user-auth.service";

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit{
  range = new FormGroup({
    start: new FormControl(new Date(new Date().setDate(new Date().getDate() - 10))),
    end: new FormControl(new Date(new Date().setDate(new Date().getDate() + 10))),
  });
  drivesTotal:number=0;
  priceTotal:number=0;
  kmTotal:number=0;
  email:string ='';
  drives:Drive[] = []
  data:any
  label:string='broj voznji'
  chartData:any
  chartType: string ='';
  chartTypes: string[] = ['broj voznji', 'kilometraza', 'cena'];

  dates_labels:string[] = []
  dates:Date[] = []
  drives_number:number[] = []
  price_number: number[] = []
  km_number:number[] = []

  type = 'bar';
  options = {
    responsive: true,
    maintainAspectRatio: true,
    scales: {
      yAxes: [{
        ticks: {
          min: 0
        }
      }]
    }
  };

  constructor(private appService:AppService,
              private userService: UserAuthService) {
  }

  showData(){
    if(this.range.value.start === null || this.range.value.end === null){
      this.appService.openErrorDialog("Niste izabrali vremenski period.")
    }
    else{
      this.fillData()
    }
  }

  fillData(){
    this.dates_labels = []
    this.dates = []
    var pipe = new DatePipe('sr-RS');
    let date :Date = new Date(this.range.value.start)
    while(date <= this.range.value.end) {
      this.dates_labels.push(<string>pipe.transform(date, 'dd-MM-yyyy'))
      this.dates.push(new Date(date));
      date = new Date(date.setDate(date.getDate() + 1))
    }
    this.drives_number = Array(this.dates.length).fill(0)
    this.price_number = Array(this.dates.length).fill(0)
    this.km_number = Array(this.dates.length).fill(0)
    for(let drive of this.drives){
      let i = -1;
      for(let j=0; j<this.dates.length;j++){
        if(this.dates[j].getDate()===new Date(drive.startDate).getDate() &&
          this.dates[j].getMonth()===new Date(drive.startDate).getMonth()){
          i=j;
        }
      }
      if(i!==-1){
        this.drives_number[i] += 1
        this.price_number[i] += drive.price
        this.km_number[i] += drive.distance
      }
    }
    this.countTotal()
    this.chartTypeChanged()
    this.setData()
  }

  setData(){
    this.data = {
      labels: this.chartData[0],
      datasets: [{
        label: this.label,
        data: this.chartData[1],
        backgroundColor: "#f38b4a",
      }]
    };
  }

  chartTypeChanged(){
    let chosen:number[] = []
    if(this.chartType === this.chartTypes[0])
      chosen = this.drives_number
    if(this.chartType === this.chartTypes[1])
      chosen = this.km_number
    if(this.chartType === this.chartTypes[2])
      chosen = this.price_number
    this.chartData = [this.dates_labels, chosen]
    this.label = this.chartType
  }

  countTotal(){
    this.kmTotal = this.km_number.reduce((sum, current) => sum+current,0)
    this.priceTotal = this.price_number.reduce((sum, current) => sum+current,0)
    this.drivesTotal = this.drives_number.reduce((sum, current) => sum+current,0)
  }

  ngOnInit() {
    this.chartType = this.chartTypes[0]
    this.appService.getLoggedUser().subscribe((res)=>{
      this.email = res.email
      if(this.userService.getRole() !== "ROLE_ADMINISTRATOR")
        this.appService.getAllDrivesClient(this.email).subscribe((res)=>{
          this.drives = res
          this.fillData()
        })
      else{
        this.appService.getAllDrives().subscribe((res)=>{
          this.drives = res
          this.fillData()
        })
      }
    })
  }

}
