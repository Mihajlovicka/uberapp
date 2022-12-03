import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from "rxjs";
import { RegisterForm } from "./model/registerForm.model";
import { User } from "./model/user.model";

@Injectable({
    providedIn: 'root'
  })

  export class AppService{
    private registerUrl = 'http://localhost:8080/api/register';
    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };

      constructor(private http: HttpClient) {
        
       

    }


     public register(registerForm: RegisterForm): Observable<User>{

      return this.http.post<User>(`${this.registerUrl}`, registerForm, this.httpOptions).pipe(
        catchError(this.handeRegistrationError<User>()))

     }


     private handeRegistrationError<T>(result?: T) {
      return (error: any): Observable<T> => {
        console.error(error); // log to console instead

        if(error.error.message === "Email in use."){
  
        alert("Email in use.");

      }
      else{
        alert("Registration failed.");
      }
  
        return of(result as T);
      };
     }

    
     
      private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
          // TODO: send the error to remote logging infrastructure
          console.error(error); // log to console instead
    
          // TODO: better job of transforming error for user consumption
          alert(`${operation} failed.`);
    
          // Let the app keep running by returning an empty result.
          return of(result as T);
        };
      }
    
  }
  
