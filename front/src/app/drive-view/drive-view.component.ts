import { Component, OnInit } from '@angular/core';
import * as L from "leaflet";
import {AppService} from "../app.service";
import {Drive} from "../model/drive.model";
import {ActivatedRoute} from "@angular/router";
import { Grade } from '../model/grade.model';
import {PasswordChangeComponent} from "../password-change/password-change.component";
import {GradesOverviewComponent} from "../grades-overview/grades-overview.component";
import {MatDialog} from "@angular/material/dialog";
L.Icon.Default.imagePath = 'assets/';

@Component({
  selector: 'app-drive-view',
  templateUrl: './drive-view.component.html',
  styleUrls: ['./drive-view.component.css']
})
export class DriveViewComponent implements OnInit {

  private map!: L.Map;
  avgGradeDriver: number = 0.0;
  avgGradeCar: number = 0.0;
  public grades: Grade[] = [];
  options = {
    layers: [
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        // attribution: '...',
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      }),
    ],
    zoom: 14,
    center: L.latLng(45.253434, 19.831323),
  };
  drive :Drive | undefined
  mainGroup: L.LayerGroup[] = [];
  id:number = -1;


  constructor(private appService: AppService,
              public dialog: MatDialog,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'))
    this.appService.getDrive(this.id).subscribe((res:any)=>{
      this.drive = res;
      let geoLayerRouteGroup: L.LayerGroup = new L.LayerGroup();
      let color = Math.floor(Math.random() * 16777215).toString(16);
      let json:any = JSON.parse(res.routeJSON)
      let routeLayer = L.geoJSON(json['features'][0]['geometry']);
      routeLayer.setStyle({color: `#${color}`});
      routeLayer.addTo(geoLayerRouteGroup);
      this.mainGroup = [geoLayerRouteGroup]
      this.map.fitBounds(routeLayer.getBounds())
      this.drive?.stops.forEach((stop) => {
        let markerLayer = L.marker([stop.location.latitude, stop.location.longitude], {
          icon: L.icon({
            iconUrl: 'assets/startMark.png',
            iconSize: [20, 20],
            iconAnchor: [10, 10],
          }),
        })
        markerLayer.addTo(geoLayerRouteGroup);
      })
    });

    this.appService.getGradesForDrive(this.id).subscribe((res:any)=>{
      this.grades = res;
      for(let g of this.grades){
        this.avgGradeDriver += g.gradeDriver;
        this.avgGradeCar += g.gradeCar;
      }
      if(this.avgGradeDriver != 0) this.avgGradeDriver /= this.grades.length;
      if(this.avgGradeCar != 0) this.avgGradeCar /= this.grades.length;

      console.log(res);
    });

  }

  onMapReady(map: L.Map) {
    this.map = map
  }

  openGrades(){
    const dialogRef = this.dialog.open(GradesOverviewComponent, {
      data: {driveID: this.id},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');

    });
  }

}
