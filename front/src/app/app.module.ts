import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppService } from './app.service';
import { RegisterComponent } from './register/register.component';

import { RouterModule, Routes } from '@angular/router';
import { RegistrationConfirmComponent } from './registration-confirm/registration-confirm.component';
import {UserRegistrationService} from "./user-registration.service";

import { AddDriverComponent } from './add-driver/add-driver.component';
import { AddCarComponent } from './add-car/add-car.component';

import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { ClientHomeComponent } from './client-home/client-home.component';
import { DriverHomeComponent } from './driver-home/driver-home.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';


import {MatFormFieldModule} from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatCardModule} from "@angular/material/card";
import {AuthInterceptor} from "./_auth/auth.interceptor";
import {AuthGuard} from "./_auth/auth.guard";


const appRoutes: Routes = [
  { path: 'register', component: RegisterComponent},
  { path:'registerConfirm/:email', component:RegistrationConfirmComponent},
  {path: 'driver-info', component: AddDriverComponent},
  {path:'car-info', component:AddCarComponent},
  {path:'login', component:LoginComponent},
  {path:'client', component:ClientHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'driver', component:DriverHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_DRIVER'}},
  {path:'forbidden', component:ForbiddenComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    RegistrationConfirmComponent,
    AddDriverComponent,
    AddCarComponent,
    LoginComponent,
    HeaderComponent,
    ClientHomeComponent,
    DriverHomeComponent,
    ForbiddenComponent,
  ],


  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(
      appRoutes, {
        enableTracing: true
      }
    ),
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    FormsModule,
    MatCardModule
  ],
  providers: [
    AppService,
    AuthGuard,
    {provide:HTTP_INTERCEPTORS,
    useClass:AuthInterceptor,
    multi:true},
    UserRegistrationService
  ],
  bootstrap: [AppComponent]



})
export class AppModule { }


