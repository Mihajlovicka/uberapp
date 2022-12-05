import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppService } from './app.service';
import { RegisterComponent } from './register/register.component';

import { RouterModule, Routes } from '@angular/router';
import { RegistrationConfirmComponent } from './registration-confirm/registration-confirm.component';
import {UserRegistrationService} from "./user-registration.service";



const appRoutes: Routes = [
  { path: 'register', component: RegisterComponent},
  { path:'registerConfirm/:email', component:RegistrationConfirmComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    RegistrationConfirmComponent
  ],



  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(
      appRoutes,{
        enableTracing: true
      }
    )

  ],
  providers: [AppService, UserRegistrationService],
  bootstrap: [AppComponent]



})
export class AppModule { }


