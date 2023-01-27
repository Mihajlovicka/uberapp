import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {RegisterForm} from "./model/registerForm.model";
import {Role, User} from "./model/user.model";
import {ClientsAccount} from "./model/clientsAccount.model";
import {CarBodyType, Fuel} from "./model/car.model";
import {DriverCarInfo} from "./model/driverCarInfo.model";
import {DriversAccount} from "./model/driversAccount.model";
import {MatDialog} from "@angular/material/dialog";
import {ErrorDialogComponent} from "./dialog-template/error-dialog/error-dialog.component";
import {Image} from "./model/image.model";

@Injectable({
  providedIn: 'root'
})

  export class AppService{
    private registerUrl = 'http://localhost:8080/api/register';
    private addDriverUrl = 'http://localhost:8080/api/add-driver';
    private getClientUrl = 'http://localhost:8080/api/getClient?email=';
    private getDriverUrl = 'http://localhost:8080/api/getDriver?email=';
    private updateClientUrl = 'http://localhost:8080/api/updateClient';
    private updateDriverUrl = 'http://localhost:8080/api/updateDriver';
    private bankUrlAccept = 'http://localhost:8080/api/acceptBankAccountAccess';
    private bankUrlDecline = 'http://localhost:8080/api/declineBankAccountAccess';
    private allActiveClients = 'http://localhost:8080/api/getAllActiveClients';
    private uploadImageUrl = 'http://localhost:8080/api/uploadIMG';
    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };



  private emptyDriverCar: DriverCarInfo = {
    username:'',
    name: '',
    surname: '',
    email: '',
    phone: '',
    password: '',
    checkPassword: '',
    role: Role.ROLE_DRIVER,
    car: {
      brand: '',
      model: '',
      color: '',
      plateNumber: '',
      bodyType: CarBodyType.HATCHBACK,
      fuelType: Fuel.GASOLINE,
      numOfSeats:5

    }
  }


  private dataSubject = new BehaviorSubject(this.emptyDriverCar);
  currentData = this.dataSubject.asObservable();


  constructor(private http: HttpClient,
              public dialog: MatDialog) {



      }

      declineBankAccountAccess(client: ClientsAccount): Observable<ClientsAccount>{
        return this.http.post<ClientsAccount>(`${this.bankUrlDecline}`, client, this.httpOptions).pipe(
          catchError(this.handleError<ClientsAccount>())
        )
      }



      acceptBankAccountAccess(client: ClientsAccount): Observable<ClientsAccount>{
        return this.http.post<ClientsAccount>(`${this.bankUrlAccept}`, client, this.httpOptions).pipe(
          catchError(this.handleError<ClientsAccount>())
        )
      }


      public getAllClients(): Observable<ClientsAccount[]>{
        return this.http.get<ClientsAccount[]>(`${this.allActiveClients}`).pipe(catchError(this.handleError<ClientsAccount[]>()));
      }



      public getClient(email:string): Observable<ClientsAccount>{

        return this.http.get<ClientsAccount>(`${this.getClientUrl+email}`).pipe(
          catchError(this.handleError<ClientsAccount>()));
      }

      public getDriver(email:string): Observable<DriversAccount>{

        return this.http.get<DriversAccount>(`${this.getDriverUrl+email}`).pipe(
          catchError(this.handleError<DriversAccount>()));
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

     public updateClient(client: ClientsAccount): Observable<ClientsAccount> {
      return this.http.post<ClientsAccount>(`${this.updateClientUrl}`, client, this.httpOptions).pipe(
        catchError(this.handleError<ClientsAccount>())
      )
     }

     public uploadPicture(uploadImageData:FormData): any{
       //Make a call to the Spring Boot Application to save the image
        //Make a call to the Spring Boot Application to save the image
       return this.http.post(`${this.uploadImageUrl}`, uploadImageData, { observe: 'response' })
         .pipe(
           catchError(this.handleError<Image>("Doslo je do greske pri cuvanju slike"))
         );

     }

     public updateDriver(driver: DriversAccount): Observable<DriversAccount> {
      return this.http.post<DriversAccount>(`${this.updateDriverUrl}`, driver, this.httpOptions).pipe(
        catchError(this.handleError<DriversAccount>())
      )
     }


     setData(data: DriverCarInfo) {
      this.dataSubject.next(data);
 }

    private handleAddDriverCarError<T>(result?:T){
      return(error:any): Observable<T> => {
        console.error(error); // log to console instead

        if(error.error.message === "Registration plate number exist."){

          this.openErrorDialog("Registacioni broj vec postoji!");

      }

        else if(error.error.message === "Email in use."){
          this.openErrorDialog("E-mail je u upotrebi!");
        }

      else{
        this.openErrorDialog("Registration failed.");
      }

        return of(result as T);
      };



      }



     private handleRegistrationError<T>(result?: T) {
      return (error: any): Observable<T> => {
        console.error(error); // log to console instead

        if(error === "Email in use."){

          this.openErrorDialog("E-mail je u upotrebi!");

      }
      else if(error === "Account number does not exist.")
      {
        this.openErrorDialog("Ne postoji racun sa ovim brojem.");
      }
      else{
        this.openErrorDialog("Registration failed.");
      }

        return of(result as T);
      };
     }



      public handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
          // TODO: send the error to remote logging infrastructure
          console.error(error); // log to console instead

          // TODO: better job of transforming error for user consumption
          this.openErrorDialog(`${operation} failed.`);

          // Let the app keep running by returning an empty result.
          return of(result as T);
        };
      }





  public openErrorDialog(message: string) {
    this.dialog.open(ErrorDialogComponent, {
      data: {
        errorMessage: message,
      },
    });
  }




  }






