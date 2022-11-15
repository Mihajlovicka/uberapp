import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppService } from './app.service';
import { RegisterComponent } from './register/register.component';

import { RouterModule, Routes } from '@angular/router';

const appRoutes: Routes = [
  { path: 'register', component: RegisterComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent
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
  providers: [AppService],
  bootstrap: [AppComponent]



})
export class AppModule { }


