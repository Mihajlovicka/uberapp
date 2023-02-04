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
import {DriveHistoryClientComponent} from "./drive-history-client/drive-history-client.component";
import {DriveHistoryDriverComponent} from "./drive-history-driver/drive-history-driver.component";
import {AddGradeComponent} from "./add-grade/add-grade.component";
import {RidesDriverComponent} from "./rides-driver/rides-driver.component";
import {RideClientComponent} from "./ride-client/ride-client.component";
import { RespondDriveRequestComponent } from './respond-drive-request/respond-drive-request.component';
import { RespondToPaymentRequestComponent } from './respond-to-payment-request/respond-to-payment-request.component';
import {DriveViewComponent} from "./drive-view/drive-view.component";
import {ReportsComponent} from "./reports/reports.component";
import { DriveChangedReviewComponent } from './drive-changed-review/drive-changed-review.component';
import {AllUsersViewAdminComponent} from "./all-users-view-admin/all-users-view-admin.component";


const routes: Routes = [
  { path: 'register', component: RegisterComponent},
  { path:'registerConfirm/:email', component:RegistrationConfirmComponent},
  { path:'bankConfirm/:email', component:AccountAccessReviewComponent},
  {path: 'driver-info', component: AddDriverComponent, canActivate:[AuthGuard], data:{role:'ROLE_ADMINISTRATOR'}},
  {path:'car-info', component:AddCarComponent , canActivate:[AuthGuard], data:{role:'ROLE_ADMINISTRATOR'}},
  {path:'login', component:LoginComponent},
  {path:'client', component:ClientHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'favorites', component:FavoriteRoutesComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'driver', component:DriverHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_DRIVER'}},
  {path:'admin', component:AdminHomeComponent, canActivate:[AuthGuard], data:{role:'ROLE_ADMINISTRATOR'}},
  {path:'forbidden', component:ForbiddenComponent},
  {path:'clientProfile', component:ProfileViewComponent},
  {path:'driversProfile', component:DriverProfileViewComponent},
  {path:'map', component:MapComponent},
  {path:'add-clients', component:AddClientsIntoDriveComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path: 'make-drive-reservation', component:MakeDriveReservationComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path:'selectedVehicle', component:SelectedVehicleTypeComponent},
  {path:'payment', component:PaymentComponent},
  {path: 'messages-admin',component:MessagesAdminComponent, canActivate:[AuthGuard], data:{role:'ROLE_ADMINISTRATOR'}},
  {path: 'messages-client',component:MessagesClientComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path: "passenger/accept-drive/:id", component:RespondDriveRequestComponent,  canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path :"passenger/confirmPayment/:id", component: RespondToPaymentRequestComponent,  canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path: 'drive-history-client',component:DriveHistoryClientComponent, canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path: 'drive-history-driver',component:DriveHistoryDriverComponent, canActivate:[AuthGuard], data:{role:'ROLE_DRIVER'}},
  {path: 'drive-rating',component:AddGradeComponent},
  {path: 'rides-dr',component:RidesDriverComponent,  canActivate:[AuthGuard], data:{role:'ROLE_DRIVER'}},
  {path: 'rides-cl',component:RideClientComponent,  canActivate:[AuthGuard], data:{role:'ROLE_CLIENT'}},
  {path: "drive/:id", component:DriveViewComponent},
  {path: "reports", component:ReportsComponent},
  {path: "driveChanged/:id", component: DriveChangedReviewComponent},
  {path: "reports/:email", component:ReportsComponent},
  {path: "all-users", component:AllUsersViewAdminComponent, canActivate:[AuthGuard], data:{role:'ROLE_ADMINISTRATOR'}},
  {path: "", component:MapComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: true
  })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
