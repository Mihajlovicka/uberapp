import {Injectable} from '@angular/core';
import H from "@here/maps-api-for-javascript";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import * as L from "leaflet";
import {Observable, map, lastValueFrom, catchError} from "rxjs";
import {ClientsAccount} from "../model/clientsAccount.model";
import {Position} from "../model/mapAddress.model";
import {DomEvent} from "leaflet";
import stop = DomEvent.stop;
import {ErrorDialogComponent} from "../dialog-template/error-dialog/error-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {DriversAccount} from "../model/driversAccount.model";
import {AppService} from "../app.service";
import {Stop} from "../model/drive.model";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private platform = new H.service.Platform({
    apikey
  });

  private tokenORS: string = '5b3ce3597851110001cf624839ef4af2968442a684d7d503b942b88d'

  constructor(private http: HttpClient,
              public dialog: MatDialog,
              private appService:AppService) {
  }

  getHPlatform(): H.service.Platform {
    return this.platform
  }

  showRoute(stops: [number, number][]): Observable<any> {
    const headers = new HttpHeaders({
      'Accept': 'application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8',
      'Content-Type': 'application/json',
      'Authorization': this.tokenORS
    })
    const body = {
      coordinates: stops,
      preference: 'shortest',
    }

    return this.http.post<any>('https://api.openrouteservice.org/v2/directions/driving-car/geojson',
      body, {headers})
      .pipe(catchError(this.appService.handleError<any>("dodavanje rute neuspesno")))
  }

  public orderStops(stops: [number, number][], param: string): Observable<any> {

    const body = {
      locations: stops,
      metrics: [param],
    }

    const headers = new HttpHeaders({
      'Accept': 'application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8',
      'Content-Type': 'application/json',
      'Authorization': this.tokenORS
    })
    return this.http.post<any>('https://api.openrouteservice.org/v2/matrix/driving-car',
      body, {headers})
      .pipe(
        map(response => {if(param === 'duration') return response.durations; else return response.distances}),
        catchError(this.appService.handleError<any>("dodavanje rute neuspesno"))
      )

  }

  public permute(permutationN: number) {
    var permutation: number[] = []
    for (let i = 1; i <= permutationN; i++) permutation.push(i)
    var length = permutation.length,
      result = [permutation.slice()],
      c = new Array(length).fill(0),
      i = 1, k, p;

    while (i < length) {
      if (c[i] < i) {
        k = i % 2 && c[i];
        p = permutation[i];
        permutation[i] = permutation[k];
        permutation[k] = p;
        ++c[i];
        i = 1;
        result.push(permutation.slice());
      } else {
        c[i] = 0;
        ++i;
      }
    }
    return result;
  }

  public getRouteSums(matrix:Array<Array<number>>, combinations:number[][], n:number): number[]{
    var results: number[] = []
    combinations.forEach((comb:number[]) => {
      let sum = 0
      let start = 0
      let end = n-1
      sum += matrix[start][comb[0]]
      sum += matrix[comb[comb.length-1]][end]
      for (let i = 0; i < comb.length-1; i++) {
        sum += matrix[comb[i]][comb[i + 1]]
      }
      results.push(sum)
    })
    return results
  }

  public getBestRouteCombination(results: number[],combinations:number[][], stops:Stop[]): Stop[]{
    var minIndex = 0
    var minSum = results[0]
    for(let i = 1; i < results.length;i++){
      if(minSum > results[i]){
        minSum = results[i]
        minIndex = i
      }
    }
    var stopsInOrder:Stop[] = []
    var minCombination = combinations[minIndex]
    stopsInOrder.push(stops[0])
    minCombination.forEach((stopIndex) => {
      stopsInOrder.push(stops[stopIndex])
    })
    stopsInOrder.push(stops[stops.length-1])
    return stopsInOrder
  }


  public openErrorDialog(message:string){
    this.dialog.open(ErrorDialogComponent, {
      data: {
        errorMessage: message,
      },
    });
  }

}

export const apikey = '4p_yH-oIdvknqcL_qWc-67Qub-dzK1i9CfagdSkB6s0';
export const app_id = 'wxW8BsS6K2RLXNO0HmsWJg';
export const app_code = 'lVVj72KFDijaAV7MSAXtXFsruhRejqV4S_0402zNzSE-7MQSfYL0WbzkGQQRaVSDi4bTGRWNOyuaWEtESH2omA'

