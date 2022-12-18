import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, of, Subject } from "rxjs";
import { RegisterForm } from "./model/registerForm.model";
import { Role, User } from "./model/user.model";
import { ClientsAccount } from "./model/clientsAccount.model";
import { CarBodyType, Fuel } from "./model/car.model";
import { DriverCarInfo } from "./model/driverCarInfo.model";
import { DriversAccount } from "./model/driversAccount.model";

@Injectable({
    providedIn: 'root'
  })

  export class AppService{
    private registerUrl = 'http://localhost:8080/api/register';
    private addDriverUrl = 'http://localhost:8080/api/add-driver';
    private getClientUrl = 'http://localhost:8080/api/getClient?email=';
    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };


      private emptyDriverCar: DriverCarInfo = {
        name: '',
        surname: '',
        email: '',
        phone: '',
        password: '',
        checkPassword: '',
        role: Role.ROLE_DRIVER,
        car: {
          brand:'',
          model:'',
          color:'',
          plateNumber:'',
          bodyType: CarBodyType.HATCHBACK,
          fuelType: Fuel.GASOLINE

        }
      }


      private dataSubject = new BehaviorSubject(this.emptyDriverCar);
      currentData = this.dataSubject.asObservable();


      constructor(private http: HttpClient) {



      }




      public getClient(email:string): Observable<ClientsAccount>{
        
        return this.http.get<ClientsAccount>(`${this.getClientUrl+email}`).pipe(
          catchError(this.handleError<ClientsAccount>()));
      }


     public register(registerForm: RegisterForm): Observable<ClientsAccount>{

      return this.http.post<ClientsAccount>(`${this.registerUrl}`, registerForm, this.httpOptions).pipe(
        catchError(this.handleRegistrationError<ClientsAccount>()))


     }


     public addDriverCarAccount(addForm: DriverCarInfo): Observable<DriversAccount> {
        return this.http.post<DriversAccount>(`${this.addDriverUrl}`, addForm, this.httpOptions).pipe(
          catchError(this.handleAddDriverCarError<DriversAccount>())
        )
     }


     setData(data: DriverCarInfo) {
      this.dataSubject.next(data);
 }

    private handleAddDriverCarError<T>(result?:T){
      return(error:any): Observable<T> => {
        console.error(error); // log to console instead

        if(error.error.message === "Registration plate number exist."){

        alert("Registacioni broj vec postoji!");

      }

        else if(error.error.message === "Email in use."){
          alert("E-mail je u upotrebi!");
        }

      else{
        alert("Registration failed.");
      }

        return of(result as T);
      };



      }



     private handleRegistrationError<T>(result?: T) {
      return (error: any): Observable<T> => {
        console.error(error); // log to console instead

        if(error.error.message === "Email in use."){

        alert("E-mail je u upotrebi!");

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

