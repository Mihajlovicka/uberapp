import { Component, OnInit } from '@angular/core';
import * as L from "leaflet";
import {AppService} from "../app.service";
import {Drive} from "../model/drive.model";
import {ActivatedRoute} from "@angular/router";
L.Icon.Default.imagePath = 'assets/';

@Component({
  selector: 'app-drive-view',
  templateUrl: './drive-view.component.html',
  styleUrls: ['./drive-view.component.css']
})
export class DriveViewComponent implements OnInit {

  private map!: L.Map;

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
      })
  }

  onMapReady(map: L.Map) {
    this.map = map
  }

}
