import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FavoriteRide} from "../../model/favoriteRide.model";
import {AppService} from "../../app.service";

@Component({
  selector: 'app-favorite-routes-dialog',
  templateUrl: './favorite-routes-dialog.component.html',
  styleUrls: ['./favorite-routes-dialog.component.css']
})
export class FavoriteRoutesDialogComponent implements OnInit {

  favoriteRoutes:FavoriteRide[] = []

  step = -1;

  constructor(public dialogRef: MatDialogRef<FavoriteRoutesDialogComponent>,
              private service:AppService) { }

  ngOnInit(): void {
    this.service.getFavoriteRoutes().subscribe((res: FavoriteRide[]) => {
      this.favoriteRoutes = res;
    })
  }

  setStep(index: number) {
    this.step = index;
  }

  deleteRoute(index: number) {
    let ride:any = this.favoriteRoutes[index]
    this.service.deleteFavorite(ride.id).subscribe(() => {
      this.service.openErrorDialog("Uspesno obrisana ruta.");
      this.favoriteRoutes.splice(index,1)
    }
    )
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
