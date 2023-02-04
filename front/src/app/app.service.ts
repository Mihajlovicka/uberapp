import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, catchError, Observable, of, Subject} from "rxjs";
import {RegisterForm} from "./model/registerForm.model";
import {Role, User} from "./model/user.model";
import {ClientsAccount} from "./model/clientsAccount.model";
import {BabySeat, CarBodyType, Fuel} from "./model/car.model";
import {DriverCarInfo} from "./model/driverCarInfo.model";
import {DriversAccount, DriverStatus} from "./model/driversAccount.model";
import {MatDialog} from "@angular/material/dialog";
import {ErrorDialogComponent} from "./dialog-template/error-dialog/error-dialog.component";
import {Image} from "./model/image.model";
import {FavoriteRide} from "./model/favoriteRide.model";
import {PasswordChange} from "./model/passwordChange.model";
import {Message} from "./model/message.model";
import {UsersChatDisplay} from "./model/usersChatDisplay.model";
import {Notification} from "./model/notification.model";
import { DriveReservationForm } from "./model/driveReservationForm.model";
import { Drive } from "./model/drive.model";
import { BankTransaction } from "./model/bankTransaction.model";
import { BankAccount } from "./model/bankAccount.model";
import {Grade} from "./model/grade.model";
import {MapAddress} from "./model/mapAddress.model";
import {Vehicle} from "./model/Vehicle";
import { Stop } from "./model/stop.model";



@Injectable({
  providedIn: 'root'
})


  export class AppService{
    private registerUrl = 'http://localhost:8080/api/register';
    private addDriverUrl = 'http://localhost:8080/api/add-driver';
    private getClientUrl = 'http://localhost:8080/api/getClient?email=';
    private getDriveUrl = 'http://localhost:8080/api/getDrive/';
    private getDriverUrl = 'http://localhost:8080/api/getDriver?email=';
    private updateClientUrl = 'http://localhost:8080/api/updateClient';
    private updateDriverUrl = 'http://localhost:8080/api/updateDriver';
    private bankUrlAccept = 'http://localhost:8080/api/acceptBankAccountAccess';
    private bankUrlDecline = 'http://localhost:8080/api/declineBankAccountAccess';
    private allActiveClients = 'http://localhost:8080/api/getAllActiveClients';
    private uploadImageUrl = 'http://localhost:8080/api/uploadIMG';
    private getLoggedUrl = 'http://localhost:8080/api/getLogged';
    private host = 'http://localhost:8080';
    private getLoggedUserUrl = 'http://localhost:8080/api/getLoggedUser';
    private blockUserUrl = 'http://localhost:8080/api/blockUser';
    private unblockUserUrl = 'http://localhost:8080/api/unblockUser';
    private changePasswordUrl = 'http://localhost:8080/api/changePassword';
    private crateDriveReservationUrl="http://localhost:8080/api/createDriveReservation";
    private getBankTransactionUrl="http://localhost:8080/passenger/confirmPayment/";
    private acceptPaymentUrl = "http://localhost:8080/bank/acceptTransaction/";
    private declinePaymentUrl = "http://localhost:8080/bank/declineTransaction/";
    private getClientsBankAccountUrl = "http://localhost:8080/api/getClientsBankAccount/";
    private acceptDriveParticipationUrl = "http://localhost:8080/api/acceptDrive/";
    private declineDriveParticipationUrl = "http://localhost:8080/api/declineDrive/";
    private cancelDriveUrl = "http://localhost:8080/api/ownerCancelDrive/";
    private continueDriveUrl = "http://localhost:8080/api/continueWithDrive/";

    httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };


  private emptyDriverCar: DriverCarInfo = {
    username: '',
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
      numOfSeats:5,
    }
  }


  private dataSubject = new BehaviorSubject(this.emptyDriverCar);
  currentData = this.dataSubject.asObservable();


  constructor(private http: HttpClient, public dialog: MatDialog) {}

  acceptDriveParticipation(id: number){
    return this.http.post<Drive>(`${this.acceptDriveParticipationUrl + id}`, this.httpOptions).pipe(
      catchError(this.handleError<Drive>())
    )
  }

  cancelDrive(id: number): Observable<Drive>{
    return this.http.post<Drive>(`${this.cancelDriveUrl + id}`, this.httpOptions).pipe(
      catchError(this.handleError<Drive>())
    )
  }

  continueDrive(id: number): Observable<Drive>{
    return this.http.post<Drive>(`${this.continueDriveUrl + id}`, this.httpOptions).pipe(
      catchError(this.handleError<Drive>())
    )
  }

  declineDriveParticipation(id: number){
    return this.http.post<Drive>(`${this.declineDriveParticipationUrl + id}`, this.httpOptions).pipe(
      catchError(this.handleError<Drive>())
    )
  }

  getClientsBankAccount(email: string){
      return this.http.get<BankAccount>(`${this.getClientsBankAccountUrl + email}`).pipe(
        catchError(this.handleError<BankAccount>())
      )
  }

  acceptPayment(id: number): Observable<BankTransaction>{
    return this.http.post<BankTransaction>(`${this.acceptPaymentUrl + id}`, this.httpOptions).pipe(
      catchError(this.handleError<BankTransaction>())
    )
  }

  declinePayment(id: number, bankAccount: BankAccount): Observable<BankTransaction>{
      return this.http.post<BankTransaction>(`${this.declinePaymentUrl + id}`, bankAccount, this.httpOptions).pipe(
        catchError(this.handleError<BankTransaction>())
      )
  }

  declineBankAccountAccess(client: ClientsAccount): Observable<ClientsAccount> {
    return this.http.post<ClientsAccount>(`${this.bankUrlDecline}`, client, this.httpOptions).pipe(
      catchError(this.handleError<ClientsAccount>())
    )
  }

  //pravljenje reservacije za voznju tip-DriveReservationForm i vracanje voznje --tip Drive

  createDriveReservation(driveReservationForm: DriveReservationForm):Observable<Drive>{
    return this.http.post<Drive>(`${this.crateDriveReservationUrl}`, driveReservationForm, this.httpOptions).pipe(
      catchError(this.handleError<Drive>())
    )
  }


  acceptBankAccountAccess(client: ClientsAccount): Observable<ClientsAccount> {
    return this.http.post<ClientsAccount>(`${this.bankUrlAccept}`, client, this.httpOptions).pipe(
      catchError(this.handleError<ClientsAccount>())
    )
  }


  public getAllClients(): Observable<ClientsAccount[]> {
    return this.http.get<ClientsAccount[]>(`${this.allActiveClients}`).pipe(catchError(this.handleError<ClientsAccount[]>()));
  }

      //administrator je tipa user?
      public getLogged(): Observable<any>{
        return this.http.get<any>(`${this.getLoggedUrl}`).pipe(catchError(this.handleError<any>()));
      }


  public getClient(email: string): Observable<ClientsAccount> {

    return this.http.get<ClientsAccount>(`${this.getClientUrl + email}`).pipe(
      catchError(this.handleError<ClientsAccount>()));
  }


  public getDriver(email: string): Observable<DriversAccount> {

    return this.http.get<DriversAccount>(`${this.getDriverUrl + email}`).pipe(
      catchError(this.handleError<DriversAccount>()));
  }

  public getDrive(id: number): Observable<Drive>{
    return this.http.get<Drive>(`${this.getDriveUrl + id}`).pipe(
      catchError(this.handleError<Drive>())
    );


  }

  public getTransictionInfo(id: number): Observable<BankTransaction>{
    return this.http.get<BankTransaction>(`${this.getBankTransactionUrl +id}`).pipe(
      catchError(this.handleError<BankTransaction>())
    );
  }



  public register(registerForm: RegisterForm): Observable<ClientsAccount> {

    return this.http.post<ClientsAccount>(`${this.registerUrl}`, registerForm, this.httpOptions).pipe(
      catchError(this.handleRegistrationError<ClientsAccount>()))

  }

  public sendMessage(message:Message):Observable<any>{
    return this.http.post("http://localhost:8080/api/newMessage",message,this.httpOptions).pipe(catchError(this.handleError<any>()));
  }
  public openNotification(id:bigint):Observable<any>{
    return this.http.post("http://localhost:8080/api/openNotification",id,this.httpOptions).pipe(catchError(this.handleError<any>()));
  }
  public getMessagesForUser(email:string): Observable<Message[]>{
    return this.http.get<Message[]>(`http://localhost:8080/api/getMessages?email=`+email).pipe(catchError(this.handleError<Message[]>()));
  }
  public getNotificationsForUser(email:string): Observable<Notification[]>{
    return this.http.get<Notification[]>(`http://localhost:8080/api/getNotifications?email=`+email).pipe(catchError(this.handleError<Notification[]>()));
  }
  public getUsersChatDisplay(): Observable<UsersChatDisplay[]>{
    return this.http.get<UsersChatDisplay[]>(`http://localhost:8080/api/getUsersChats`).pipe(catchError(this.handleError<UsersChatDisplay[]>()));
  }
  public getUser(email:string): Observable<User>{
    return this.http.get<User>(`http://localhost:8080/api/getUser?email=`+email).pipe(catchError(this.handleError<User>()));
  }
  public getDriveForGrade(driveID:number): Observable<Drive>{
    return this.http.get<Drive>(`http://localhost:8080/api/getDrive?driveID=`+driveID).pipe(catchError(this.handleError<Drive>()));
  }
  public addGrade(grade:Grade):Observable<any>{
    return this.http.post("http://localhost:8080/api/newGrade",grade,this.httpOptions).pipe(catchError(this.handleError<any>("Dodavanje ocene ")));
  }
  public getAllDrivesClient(email:string): Observable<Drive[]>{
    return this.http.get<Drive[]>(`http://localhost:8080/api/getDrivesClient?email=`+email).pipe(catchError(this.handleError<Drive[]>()));
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

  public changePassword(passwordData: PasswordChange): Observable<any> {
    return this.http.post<any>(`${this.changePasswordUrl}`, passwordData, this.httpOptions).pipe(
      catchError(this.handleError<any>())
    )
  }

  public uploadPicture(uploadImageData: FormData): any {
    //Make a call to the Spring Boot Application to save the image
    //Make a call to the Spring Boot Application to save the image
    return this.http.post(`${this.uploadImageUrl}`, uploadImageData, {observe: 'response'})
      .pipe(
        catchError(this.handleError<Image>("Doslo je do greske pri cuvanju slike"))
      );

  }

  public updateDriver(driver: DriversAccount): Observable<DriversAccount> {
    return this.http.post<DriversAccount>(`${this.updateDriverUrl}`, driver, this.httpOptions).pipe(
      catchError(this.handleError<DriversAccount>())
    )
  }
  public blockUser(email:string):any{
    console.log("User to block:" + email);
    return this.http.post(`${this.blockUserUrl}`, email, this.httpOptions).pipe(
      catchError(this.handleError("Neuspesno blokiranje")));

  }
  public unblockUser(email:string):any{
    console.log("User to block:" + email);
    return this.http.post(`${this.unblockUserUrl}`, email, this.httpOptions).pipe(
      catchError(this.handleError("Neuspesno odblokiranje")));

  }

     public getLoggedUser():Observable<User>{
       return this.http.get<User>(`${this.getLoggedUserUrl}`).pipe(
         catchError(this.handleError<User>()));
     }

  setData(data: DriverCarInfo) {
    this.dataSubject.next(data);
  }

  private handleAddDriverCarError<T>(result?: T) {
    return (error: any): Observable<T> => {
      console.error(error); // log to console instead

      if (error.error.message === "Registration plate number exist.") {

        this.openErrorDialog("Registacioni broj vec postoji!");

      } else if (error.error.message === "Email in use.") {
        this.openErrorDialog("E-mail je u upotrebi!");
      } else {
        this.openErrorDialog("Registration failed.");
      }

      return of(result as T);
    };


  }


  private handleRegistrationError<T>(result?: T) {
    return (error: any): Observable<T> => {
      console.error(error); // log to console instead

      if (error === "Email in use.") {

        this.openErrorDialog("E-mail je u upotrebi!");

      } else if (error === "Account number does not exist.") {
        this.openErrorDialog("Ne postoji racun sa ovim brojem.");
      } else {
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

  public addFavoriteRide(ride:FavoriteRide): Observable<FavoriteRide>{
    return this.http.post<FavoriteRide>(this.host + "/ride/new_favorite", ride).pipe(
      catchError(this.handleError<FavoriteRide>("Doslo je do greske prilikom dodavanja rute."))
    )
  }


  getFavoriteRoutes(): Observable<FavoriteRide[]>{
    return this.http.get<FavoriteRide[]>(this.host + "/ride/get_favorites", this.httpOptions).pipe(
      catchError(this.handleError<FavoriteRide[]>("Doslo je do greske."))
    )
  }

  deleteFavorite(favoriteRideID: number) {
    return this.http.delete<number>(this.host + "/ride/delete_favorite/" + favoriteRideID, this.httpOptions).pipe(
      catchError(this.handleError<any>("Doslo je do greske."))
    )
  }


  getFrequentAddresses(): Observable<MapAddress[]>{
    return this.http.get<MapAddress[]>(this.host + "/frequentAddresses" , this.httpOptions).pipe(
      catchError(this.handleError<any>("Doslo je do greske."))
    )
  }

  getDriverCar():Observable<Vehicle> {
    return this.http.get<Vehicle>(this.host + "/api/car/getDriversCar" , this.httpOptions)
  }

  getDriverStatus():Observable<DriversAccount>{
    return this.http.get<DriversAccount>(this.host + "/getDriver" , this.httpOptions).pipe(
      catchError(this.handleError<any>("Doslo je do greske."))
    )
  }

  getCurrentRide() :Observable<Stop[]>{
    return this.http.get<Stop[]>(this.host + "/ride/getCurrent" , this.httpOptions)
  }

  getFirstFutureRide() :Observable<any>{
    return this.http.get<any>(this.host + "/ride/getFirstFuture" , this.httpOptions)
  }

  endRide() {
    return this.http.post<any>(this.host + "/ride/endRide" , this.httpOptions)
  }

  startNextRide() :Observable<any>{
    return this.http.post<any>(this.host + "/ride/goToNextRide" , this.httpOptions)
  }

  startRide() {
    return this.http.post<any>(this.host + "/ride/startRide" , this.httpOptions)
  }

  getClientCurrentCar() : Observable<Vehicle> {
    return this.http.get<Vehicle>(this.host + "/api/car/getClientCurrentCar" , this.httpOptions)
  }

  getClientCurrentDrive():Observable<Stop[]> {
    return this.http.get<Stop[]>(this.host + "/getClientCurrentDriveStops" , this.httpOptions)
  }

  notifyPassengers() {
    return this.http.post<any>(this.host + "/notifyPassengers" , this.httpOptions)
  }

  cancelRide(result: any) {
    return this.http.post<any>(this.host + "/cancelRide", result , this.httpOptions)
  }

  changeAvailability():Observable<any> {
    return this.http.post<any>(this.host + "/changeAvailability" , this.httpOptions).pipe()
  }
}