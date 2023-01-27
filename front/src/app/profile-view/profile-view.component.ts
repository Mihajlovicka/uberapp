import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { AppService } from '../app.service';
import { BankStatus, ClientsAccount } from '../model/clientsAccount.model';
import { Role, Status } from '../model/user.model';

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.css']
})
export class ProfileViewComponent implements OnInit {



  retrievedImage: any;
  message: string | undefined;
  disableUpload: boolean=true;
  pictureChanging:boolean=false;
  imageName: any;
  disabled:boolean=true;
  izmena:string="Izmeni podatke"
  email:string="";

  clientsAccount: ClientsAccount = {
    user:{
      username:'',
      name:'SRRRRR',
      surname:'',
      email:'',
      status: Status.ACTIVE,
      role: Role.ROLE_CLIENT
    },
    address:{
      city:'',
      street:'',
      number:''
    },
    picture: {
      name:'',
      type:'',
      picByte:null
    },
    phone:'',
    clientsBankAccount:{
      balance:0,
      bankAccountNumber:'',
      verificationEmail:'',
      ownerName:'',
      ownerSurname:''
    },
    bankStatus: BankStatus.EMPTY
  }
  private selectedFile: File | undefined;


  constructor(private appService: AppService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.loadClient();
    //document.getElementById("inputFirstName")?.setAttribute('value',this.clientsAccount.user.email);
  }

  loadClient(){
    this.route.queryParams.subscribe(params => {

      this.email = params['email']
      console.log(this.email);
     });

     this.appService.getClient(this.email).subscribe((resp: ClientsAccount) => {
      this.clientsAccount = resp;
       console.log(this.clientsAccount);
      console.log(resp);
       console.log(this.clientsAccount.picture.picByte);

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
    this.loadClient();
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
    this.clientsAccount.user.name.charAt(0).toUpperCase();
    this.clientsAccount.user.surname.charAt(0).toUpperCase();
    this.clientsAccount.address.city.charAt(0).toUpperCase();
    this.clientsAccount.address.street.charAt(0).toUpperCase();
  }

  private checkFileType(file:any) : boolean{
    if(file.type!="image/jpg" && file.type!="image/png" && file.type!="image/jpeg") return false;
    else return true;
  }

  //Gets called when the user selects an image
  public onFileChanged(event:any) {
    //Select File
    if(event.target.files[0].size < 5242880) {
      if (this.checkFileType(event.target.files[0])) {

        this.selectedFile = event.target.files[0];
        this.disableUpload = false;
      } else {
        this.appService.openErrorDialog("Tip fajla mora biti png, jpg ili jpeg. Izaberite drugi fajl!");
        this.disableUpload = true;
      }
    }
    else{
      this.appService.openErrorDialog("Odabrana slika je prevelika. Izaberite neku drugu.");
      this.disableUpload = true;
    }
  }


  //Gets called when the user clicks on submit to upload the image
  onUpload() {
    console.log(this.selectedFile);

    //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    if(this.selectedFile) {
      uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
      uploadImageData.append('userEmail',this.clientsAccount.user.email);
    }
    this.appService.uploadPicture(uploadImageData).subscribe((resp: any) => {
      this.clientsAccount.picture.picByte = resp.body.picByte;
      console.log("Slika:");
      console.log(this.clientsAccount.picture);
      this.appService.openErrorDialog("Uspesno ste promenili sliku.")
      console.log(this.clientsAccount);
      console.log(resp);
     // console.log(resp.body.picByte);
    });
  }


  saveChanges(){
  if(this.clientsAccount.user.name==='')alert("Unesite ime!")
  else if(!this.validateChars(this.clientsAccount.user.name))alert("Ime nije validno")

  else if(this.clientsAccount.user.surname==='')alert("Unesite prezime!")
  else if(!this.validateChars(this.clientsAccount.user.surname))alert("Prezime nije validno")

  else if(this.clientsAccount.phone==='')alert("Unesite telefon!")
  else if(!this.validateNums(this.clientsAccount.phone))alert("Telefon nije validan!")

  else if(this.clientsAccount.address.city==='')alert("Unesite grad!")
  else if(this.clientsAccount.address.street==='')alert("Unesite ulicu!")
  else if(this.clientsAccount.address.number==='')alert("Unesite broj!")
  else if(!this.validateChars(this.clientsAccount.address.city) || !this.validateChars(this.clientsAccount.address.street))alert("Adresa nije validna!")

  else{

  this.capitalizeForm();

  this.appService.updateClient(this.clientsAccount).subscribe((resp: ClientsAccount) => {
      this.clientsAccount = resp;

    this.appService.openErrorDialog("Podaci uspesno izmenjenji.");
    })
  }

  }
}
