import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {AppService} from "../app.service";
import {ClientsAccount} from "../model/clientsAccount.model";

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  PATH_OF_API = 'http://localhost:8080/';

  requestHeader = new HttpHeaders({'No-Auth':'True'});


  constructor(private http: HttpClient, private service: AppService) { }

  public registerConfirm(email: string | null): Observable<any> {
    const httpOptions : Object = {
      headers: new HttpHeaders({
        'Accept': 'text/plain',
        'Content-Type': 'text/plain; charset=utf-8'
      }),
      responseType: 'text'
    };

    return this.http.post<string>(this.PATH_OF_API + 'api/registerConfirm',email, httpOptions)
    }

    public login(loginData: any){
      return this.http.post(this.PATH_OF_API + 'auth/login', loginData, {headers: this.requestHeader}).pipe(
        catchError(this.handleError<any>("Korisnicko ime ili lozinka nisu ispravni."))
      );
    }


    public setRole(role:string){
      localStorage.setItem('role',role);
    }
    public getRole():string|null{
        return localStorage.getItem('role');
    }
  public setToken(token:string){
    localStorage.setItem('token',token);
  }
  public getToken():string|null{
    return localStorage.getItem('token');
  }
  public clear(){
    localStorage.clear();
    return this.http.post(this.PATH_OF_API + 'auth/logout', {headers: this.requestHeader})
  }
  public isLoggedIn(){
    return this.getRole() && this.getToken();
  }

  public roleMatch(role:string){
    return this.getRole() != null && this.getRole() && this.getRole() === role;
  }
  public handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.service.openErrorDialog(`${operation} failed.`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
