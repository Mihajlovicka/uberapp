import {AfterViewInit, Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import {AppService} from "../app.service";
import {FavoriteRide} from "../model/favoriteRide.model";

@Component({
  selector: 'app-favorite-routes',
  templateUrl: './favorite-routes.component.html',
  styleUrls: ['./favorite-routes.component.css']
})
export class FavoriteRoutesComponent implements OnInit {

  favoriteRoutes:FavoriteRide[] = []

  @Output() choosen = new EventEmitter<FavoriteRide>();
  choosenRoute: any;


  constructor(private service:AppService) { }
  ngOnInit(): void {
    this.service.getFavoriteRoutes().subscribe((res:FavoriteRide[])=>{
      this.favoriteRoutes = res;
    })
  }

  step = -1;

  setStep(index: number) {
    this.step = index;
  }

  chooseRoute(index: number) {
    this.choosenRoute = this.favoriteRoutes[index]
    this.choosen.emit(this.choosenRoute);
  }

  deleteRoute(index: number) {
    this.step--;
  }

}
