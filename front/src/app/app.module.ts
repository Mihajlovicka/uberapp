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


import { MatFormFieldModule} from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from "@angular/material/card";
import { AuthInterceptor } from "./_auth/auth.interceptor";
import { AuthGuard } from "./_auth/auth.guard";
import { ProfileViewComponent } from './profile-view/profile-view.component';
import { DriverProfileViewComponent } from './driver-profile-view/driver-profile-view.component';
import { AccountAccessReviewComponent } from './account-access-review/account-access-review.component';


import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MapComponent } from './map/map.component';
import { AddressItemComponent } from './address-item/address-item.component';


const appRoutes: Routes = [
  { path: 'register', component: RegisterComponent},
  { path:'registerConfirm/:email', component:RegistrationConfirmComponent},
  { path:'bankConfirm/:email', component:AccountAccessReviewComponent},
  {path: 'driver-info', component: AddDriverComponent},
  {path:'car-info', component:AddCarComponent},
  {path:'login', component:LoginComponent},
  {path:'client', component:ClientHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'driver', component:DriverHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_DRIVER'}},
  {path:'forbidden', component:ForbiddenComponent},
  {path:'clientProfile', component:ProfileViewComponent},
  {path:'driversProfile', component:DriverProfileViewComponent},
  {path:'map', component:MapComponent}
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
    ProfileViewComponent,
    DriverProfileViewComponent,
    AccountAccessReviewComponent,
    MapComponent,
    AddressItemComponent,
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
    MatCardModule,
    LeafletModule,
    MatAutocompleteModule
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


