import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppService} from "./app.service";

@Injectable({
  providedIn: 'root'
})
export class UserRegistrationService {

  private registerConfUrl = 'http://localhost:8080/api/registerConfirm';



  constructor(private http: HttpClient) { }

  public registerConfirm(email: string | null): Observable<any> {
    const httpOptions : Object = {
      headers: new HttpHeaders({
        'Accept': 'text/plain',
        'Content-Type': 'text/plain; charset=utf-8'
      }),
      responseType: 'text'
    };

    return this.http.post<string>(`${this.registerConfUrl}`,email, httpOptions)
    }


}
