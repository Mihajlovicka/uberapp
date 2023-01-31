import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import {MatStepperModule} from '@angular/material/stepper';
import { AppComponent } from './app.component';
import { AppService } from './app.service';
import { RegisterComponent } from './register/register.component';
import { RegistrationConfirmComponent } from './registration-confirm/registration-confirm.component';
import {UserAuthService} from "./service/user-auth.service";
import { AddDriverComponent } from './add-driver/add-driver.component';
import { AddCarComponent } from './add-car/add-car.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { ClientHomeComponent } from './client-home/client-home.component';
import { DriverHomeComponent } from './driver-home/driver-home.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from "@angular/material/card";
import {MatDialogModule} from '@angular/material/dialog';
import {MatMenuModule} from '@angular/material/menu';
import {MatExpansionModule} from '@angular/material/expansion';
import { AuthInterceptor } from "./_auth/auth.interceptor";
import { AuthGuard } from "./_auth/auth.guard";
import { ProfileViewComponent } from './profile-view/profile-view.component';
import { DriverProfileViewComponent } from './driver-profile-view/driver-profile-view.component';
import { AccountAccessReviewComponent } from './account-access-review/account-access-review.component';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MapComponent } from './map/map.component';
import { AddressItemComponent } from './address-item/address-item.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { RoutesDialogComponent } from './dialog-template/routes-dialog/routes-dialog.component';
import {MatRadioModule} from '@angular/material/radio';
import {MapService} from "./service/map.service";
import { ErrorDialogComponent } from './dialog-template/error-dialog/error-dialog.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import { AddressViewComponent } from './address-view/address-view.component';
import { AddClientsIntoDriveComponent } from './add-clients-into-drive/add-clients-into-drive.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MakeDriveReservationComponent } from './make-drive-reservation/make-drive-reservation.component';
import { SelectVehicleTypeComponent } from './select-vehicle-type/select-vehicle-type.component';
import { FindTypeOfVehicleComponent } from './find-type-of-vehicle/find-type-of-vehicle.component';
import { PasswordChangeComponent } from './password-change/password-change.component';
import {AppRoutingModule} from "./app-routing.module";
import { MessagesClientComponent } from './messages-client/messages-client.component';
import { MessagesAdminComponent } from './messages-admin/messages-admin.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import { NotificationsComponent } from './notifications/notifications.component';
import {MatBadgeModule} from "@angular/material/badge";
import { FavoriteRoutesComponent } from './favorite-routes/favorite-routes.component';
import { FavoriteRoutesDialogComponent } from './dialog-template/favorite-routes-dialog/favorite-routes-dialog.component';
import { PaymentComponent } from './payment/payment.component';
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatDividerModule } from '@angular/material/divider';
import { SelectedVehicleTypeComponent } from './selected-vehicle-type/selected-vehicle-type.component';



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
    AdminHomeComponent,
    RoutesDialogComponent,
    ErrorDialogComponent,
    AddressViewComponent,
    AddClientsIntoDriveComponent,
    MakeDriveReservationComponent,
    SelectVehicleTypeComponent,
    FindTypeOfVehicleComponent,
    FavoriteRoutesComponent,
    FavoriteRoutesDialogComponent,
    PasswordChangeComponent,
    MessagesClientComponent,
    MessagesAdminComponent,
    NotificationsComponent,
    PaymentComponent,
    SelectedVehicleTypeComponent,
  ],


    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatStepperModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        FormsModule,
        MatCardModule,
        LeafletModule,
        MatAutocompleteModule,
        MatDialogModule,
        MatRadioModule,
        MatTooltipModule,
        MatSlideToggleModule,
        MatSidenavModule,
        MatBadgeModule,
        MatMenuModule,
        MatExpansionModule,
        MatProgressBarModule,
        MatDividerModule
  ],
  providers: [
    AppService,
    AuthGuard,
    {provide:HTTP_INTERCEPTORS,
    useClass:AuthInterceptor,
    multi:true},
    UserAuthService,
    MapService
  ],
  bootstrap: [AppComponent]



})
export class AppModule { }


