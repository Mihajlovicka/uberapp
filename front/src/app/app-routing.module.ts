import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegisterComponent} from "./register/register.component";
import {RegistrationConfirmComponent} from "./registration-confirm/registration-confirm.component";
import {AccountAccessReviewComponent} from "./account-access-review/account-access-review.component";
import {AddDriverComponent} from "./add-driver/add-driver.component";
import {AddCarComponent} from "./add-car/add-car.component";
import {LoginComponent} from "./login/login.component";
import {ClientHomeComponent} from "./client-home/client-home.component";
import {AuthGuard} from "./_auth/auth.guard";
import {DriverHomeComponent} from "./driver-home/driver-home.component";
import {AdminHomeComponent} from "./admin-home/admin-home.component";
import {ForbiddenComponent} from "./forbidden/forbidden.component";
import {ProfileViewComponent} from "./profile-view/profile-view.component";
import {DriverProfileViewComponent} from "./driver-profile-view/driver-profile-view.component";
import {MapComponent} from "./map/map.component";
import {AddClientsIntoDriveComponent} from "./add-clients-into-drive/add-clients-into-drive.component";
import {MakeDriveReservationComponent} from "./make-drive-reservation/make-drive-reservation.component";

import {MessagesAdminComponent} from "./messages-admin/messages-admin.component";
import {MessagesClientComponent} from "./messages-client/messages-client.component";

import {FavoriteRoutesComponent} from "./favorite-routes/favorite-routes.component";
import { SelectedVehicleTypeComponent } from './selected-vehicle-type/selected-vehicle-type.component';
import { PaymentComponent } from './payment/payment.component';
import { RespondDriveRequestComponent } from './respond-drive-request/respond-drive-request.component';

const routes: Routes = [
  { path: 'register', component: RegisterComponent},
  { path:'registerConfirm/:email', component:RegistrationConfirmComponent},
  { path:'bankConfirm/:email', component:AccountAccessReviewComponent},
  {path: 'driver-info', component: AddDriverComponent},
  {path:'car-info', component:AddCarComponent},
  {path:'login', component:LoginComponent},
  {path:'client', component:ClientHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'favorites', component:FavoriteRoutesComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'driver', component:DriverHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_DRIVER'}},
  {path:'admin', component:AdminHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_ADMINISTRATOR'}},
  {path:'forbidden', component:ForbiddenComponent},
  {path:'clientProfile', component:ProfileViewComponent},
  {path:'driversProfile', component:DriverProfileViewComponent},
  {path:'map', component:MapComponent},
  {path:'add-clients', component:AddClientsIntoDriveComponent},
  {path: 'make-drive-reservation', component:MakeDriveReservationComponent},
  {path:'selectedVehicle', component:SelectedVehicleTypeComponent},
  {path:'payment', component:PaymentComponent},
  {path: 'messages-admin',component:MessagesAdminComponent},
  {path: 'messages-client',component:MessagesClientComponent},
  {path: "passenger/accept-drive/:id", component:RespondDriveRequestComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: true
  })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
