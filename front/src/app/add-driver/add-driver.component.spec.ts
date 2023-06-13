import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddDriverComponent } from './add-driver.component';
import {Router} from "@angular/router";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NgModule} from "@angular/core";
import {RegisterComponent} from "../register/register.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatDialogModule} from "@angular/material/dialog";
import {AppService} from "../app.service";
import {DriverCarInfo} from "../model/driverCarInfo.model";
import {Role} from "../model/user.model";
import {CarBodyType, Fuel} from "../model/car.model";

@NgModule({
  declarations: [AddDriverComponent],
  imports: [
    FormsModule,
    HttpClientTestingModule,
    ReactiveFormsModule,
    MatDialogModule,
  ],
  providers: [AppService],
})
class TestAddDriverModule {}

describe('AddDriverComponent', () => {
  let component: AddDriverComponent;
  let fixture: ComponentFixture<AddDriverComponent>;
  let router: Router;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddDriverComponent],
      imports: [RouterTestingModule, TestAddDriverModule],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddDriverComponent);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display an error when name is not entered', () => {
    spyOn(window, 'alert');
    component.info.name = '';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Unesite ime!');
  });

  it('should display an error when name is invalid', () => {
    spyOn(window, 'alert');
    component.info.name = '123';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Ime nije validno');
  });

  it('should display an error when surname is not entered', () => {

    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = '';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Unesite prezime!');
  });

  it('should display an error when surname is invalid', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = '456';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Prezime nije validno');
  });

  it('should display an error when email is not entered', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = '';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Unesite email!');
  });

  it('should display an error when email is invalid', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = 'invalid-email';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Email nije validan!.');
  });

  it('should display an error when phone is not entered', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = 'email@gmail.com';
    component.info.phone = '';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Unesite telefon!');
  });

  it('should display an error when phone is invalid', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = 'email@gmail.com';
    component.info.phone = '123abc';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Telefon nije validan!');
  });

  it('should display an error when password is not entered', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = 'email@gmail.com';
    component.info.phone = '0651234567';
    component.info.password = '';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Unesite lozinku!');
  });

  it('should display an error when check password is not entered', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = 'email@gmail.com';
    component.info.phone = '0651234567';
    component.info.password = 'password';
    component.info.checkPassword = '';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Potvrdite lozinku');
  });

  it('should display an error when passwords do not match', () => {
    spyOn(window, 'alert');
    component.info.name = 'Petar';
    component.info.surname = 'Petrovic';
    component.info.email = 'email@gmail.com';
    component.info.phone = '0651234567';
    component.info.password = 'password1';
    component.info.checkPassword = 'password2';
    component.next();
    expect(window.alert).toHaveBeenCalledWith('Lozinke se ne poklapaju!');
  });

  it('should navigate to car-info page when all data is valid', () => {
    const navigateSpy = spyOn(router, 'navigate');

    component.info.name = 'John';
    component.info.surname = 'Doe';
    component.info.email = 'john.doe@example.com';
    component.info.phone = '1234567890';
    component.info.password = 'password';
    component.info.checkPassword = 'password';
    component.next();
    expect(navigateSpy).toHaveBeenCalledWith(['/car-info']);
  });

});
