import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppService } from '../app.service';
import { BabySeat, CarBodyType, Fuel } from '../model/car.model';
import { DriversAccount, DriverStatus } from '../model/driversAccount.model';
import {Role, Status, User} from '../model/user.model';
import {PasswordChangeComponent} from "../password-change/password-change.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-driver-profile-view',
  templateUrl: './driver-profile-view.component.html',
  styleUrls: ['./driver-profile-view.component.css']
})
export class DriverProfileViewComponent implements OnInit {

  selectedBodyType:string="";

  isAdmin:boolean=false;
  isBlocked:boolean=false;
  retrievedImage: any;
  message: string | undefined;
  disableUpload: boolean=true;
  pictureChanging:boolean=false;
  imageName: any;
  disabled:boolean=true;
  izmena:string="Izmeni podatke"
  email:string="";
  logged_user: User = {
    username:'',
    name:'',
    surname:'',
    email:'',
    status: Status.ACTIVE,
    role: Role.ROLE_CLIENT
  }
  driversAccount : DriversAccount = {
    user:{
      username:'',
      name:'SRRRRR',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    picture:{
      name:'',
      type:'',
      picByte:null
    },
    phone:'',
    car:{
      brand:'',
      model:'',
      color:'',
      plateNumber:'',
      bodyType: CarBodyType.COUPE,
      fuelType: Fuel.AUTOGAS,
      numOfSeats:5,
    },
    driverStatus: DriverStatus.AVAILABLE
  }
  private selectedFile: File | undefined;

  constructor(private appService: AppService,
              private route: ActivatedRoute,
              public dialog: MatDialog) {  }

  ngOnInit(): void {
    this.loadDriver();
    this.getLoggedUser();
  }
  blockUser(){
    this.appService.blockUser(this.driversAccount.user.email).subscribe((resp: any) => {

      console.log(resp);
      this.appService.openErrorDialog("Korisnik je uspesno blokiran.");
      this.driversAccount.user.status=Status.BANNED;
      this.isBlocked=true;
    });
  }
  unblockUser(){
    this.appService.unblockUser(this.driversAccount.user.email).subscribe((resp: any) => {

      console.log(resp);
      this.appService.openErrorDialog("Korisnik je uspesno odblokiran.");
      this.driversAccount.user.status=Status.ACTIVE;
      this.isBlocked=false;
    });
  }
  changePassword(){
    const dialogRef = this.dialog.open(PasswordChangeComponent, {
      data: {email: this.driversAccount.user.email},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');

    });

  }
  getLoggedUser(){
    this.appService.getLoggedUser().subscribe((resp: User) => {
      this.logged_user = resp;

      console.log(resp);
      console.log(this.logged_user.role)
      let role:any = this.logged_user.role;

      if(role.name=="ROLE_ADMINISTRATOR"){
        this.isAdmin=true;
      }

    });
  }

  loadDriver(){
    this.route.queryParams.subscribe(params => {

      this.email = params['email']
     });

     this.appService.getDriver(this.email).subscribe((resp: DriversAccount) => {
      this.driversAccount = resp;
      this.selectedBodyType = this.driversAccount.car.bodyType;
       this.retrievedImage = 'data:image/jpeg;base64,' + this.driversAccount.picture.picByte;
      console.log(this.driversAccount);
      console.log(resp);
    });
  }
  change(){
    if(this.izmena==="Izmeni podatke"){
      this.izmena="Sacuvaj podatke";
      this.disabled=false;
    }
    else{
      this.saveChanges();
      this.izmena="Izmeni podatke";
      this.disabled=true;
    }

  }

  cancelChange(){
    this.izmena="Izmeni podatke";
    this.disabled=true;
    this.loadDriver();
  }
  validateNums(str: string): boolean{
    var regex =  /^.+?\d{7,15}$/;
    return regex.test(str);
  }

  validateChars(str: string): boolean{
    var regex = /^[a-zA-Z]+\s?[a-zA-Z]+$/;
    return regex.test(str);
  }

  capitalizeForm(){
    this.driversAccount.user.name.charAt(0).toUpperCase();
    this.driversAccount.user.surname.charAt(0).toUpperCase();
    this.driversAccount.car.brand.charAt(0).toUpperCase();
    this.driversAccount.car.model.charAt(0).toUpperCase();
  }

  private checkFileType(file:any) : boolean{
    if(file.type!="image/jpg" && file.type!="image/png" && file.type!="image/jpeg") return false;
    else return true;
  }
  //Gets called when the user selects an image
  public onFileChanged(event:any) {
    //Select File
    if(event.target.files[0].size < 5242880) {
      if(this.checkFileType(event.target.files[0])) {
        this.selectedFile = event.target.files[0];
        this.disableUpload = false;
      }
      else{
        this.appService.openErrorDialog("Tip fajla mora biti png, jpg ili jpeg. Izaberite drugi fajl!");
        this.disableUpload = true;
      }
    }
    else{
      this.appService.openErrorDialog("Odabrana slika je prevelika. Izaberite neku drugu.");
      this.disableUpload = true;
    }
    console.log(this.selectedFile);
  }


  //Gets called when the user clicks on submit to upload the image
  onUpload() {
    console.log(this.selectedFile);

    //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    if(this.selectedFile) {
      uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
      uploadImageData.append('userEmail',this.driversAccount.user.email);
    }
    this.appService.uploadPicture(uploadImageData).subscribe((resp: any) => {
      this.driversAccount.picture.picByte = resp.body.picByte;
      this.retrievedImage = 'data:image/jpeg;base64,' + this.driversAccount.picture.picByte;
      console.log("Slika:");
      console.log(this.driversAccount.picture);
      this.appService.openErrorDialog("Uspesno ste promenili sliku.")
      console.log(this.driversAccount);
      console.log(resp);

    });
  }


  saveChanges(){
  if(this.driversAccount.user.name==='')alert("Unesite ime!")
  else if(!this.validateChars(this.driversAccount.user.name))alert("Ime nije validno")

  else if(this.driversAccount.user.surname==='')alert("Unesite prezime!")
  else if(!this.validateChars(this.driversAccount.user.surname))alert("Prezime nije validno")

  else if(this.driversAccount.phone==='')alert("Unesite telefon!")
  else if(!this.validateNums(this.driversAccount.phone))alert("Telefon nije validan!")

  else if(this.driversAccount.car.brand==='')alert("Unesite breand auta!")
  else if(this.driversAccount.car.model==='')alert("Unesite model auta!")
  else if(this.driversAccount.car.plateNumber==='')alert("Unesite broj tablica!")
  else if(!this.validateChars(this.driversAccount.car.brand)) alert("Brend auta nije validan!")
  else if(!this.validateChars(this.driversAccount.car.color)) alert("Boja auta nije validna!")

  else{

  this.capitalizeForm();

  this.appService.updateDriver(this.driversAccount).subscribe((resp: DriversAccount) => {
    console.log("RESP:");
    console.log(resp);
    this.driversAccount = resp;
    this.retrievedImage = 'data:image/jpeg;base64,' + this.driversAccount.picture.picByte;
    this.appService.openErrorDialog("Zahtev za izmenu podataka je poslat administratoru sistema.");
    })
  }
}
}
