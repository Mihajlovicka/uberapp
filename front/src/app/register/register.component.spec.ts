import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { NgModule } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of } from 'rxjs';

import { RegisterComponent } from './register.component';
import { AppService } from '../app.service';
import {Role} from "../model/user.model";
import {ClientsAccount} from "../model/clientsAccount.model";
@NgModule({
  declarations: [RegisterComponent],
  imports: [
    FormsModule,
    HttpClientTestingModule,
    ReactiveFormsModule,
    MatDialogModule,
  ],
  providers: [AppService],
})
class TestRegisterModule {}

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let appService: AppService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestRegisterModule, RouterTestingModule],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    appService = TestBed.inject(AppService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the registerForm with default values', () => {
    expect(component.registerForm).toEqual({
      username: 'hehe',
      name: '',
      surname: '',
      email: '',
      phone: '',
      address: {
        city: '',
        street: '',
        number: '',
      },
      password: '',
      checkPassword: '',
      role: Role.ROLE_CLIENT,
      bankAccountNumber: '',
    });
  });

  it('should validate email format', () => {
    expect(component.validateEmail('test@example.com')).toBe(true);
    expect(component.validateEmail('invalidemail')).toBe(false);
  });

  it('should validate phone number format', () => {
    expect(component.validateNums('1234567')).toBe(false);
    expect(component.validateNums('123456789012345')).toBe(true);
  });

  it('should validate name and surname format', () => {
    expect(component.validateChars('John')).toBe(true);
    expect(component.validateChars('John123')).toBe(false);
  });

  it('should validate bank account number format', () => {
    expect(component.validateBankAccountNumber('123456789012345678')).toBe(true);
    expect(component.validateBankAccountNumber('1234567890')).toBe(false);
  });

  it('should capitalize the first letter of form inputs', () => {
    component.registerForm.name = 'petar';
    component.registerForm.surname = 'petrovic';
    component.registerForm.address = {
      city: 'beograd',
      street: 'cara dusana',
      number: '5'
    };
    fixture.detectChanges();
    component.capitalizeForm();

    fixture.detectChanges();
    expect(component.registerForm.name.charAt(0)).toBe('P');
    expect(component.registerForm.surname.charAt(0)).toBe('P');
    expect(component.registerForm.address.city.charAt(0)).toBe('B');
    expect(component.registerForm.address.street.charAt(0)).toBe('C');
  });

  it('should match passwords correctly', () => {
    expect(component.matchingPasswords('password1', 'password1')).toBe(true);
    expect(component.matchingPasswords('password1', 'password2')).toBe(false);
  });

  it('should handle successful registration', () => {
    component.registerForm = {
      username: 'srki0505@gmail.com',
      name: 'srdjan',
      surname: 'Mihajlovic',
      email: 'srki0505@gmail.com',
      phone: '123456789',
      address: {
        city: 'Novi Sad',
        street: 'Futoska',
        number: '11'
      },
      password: 'Moja nova sifra 123',
      checkPassword: 'Moja nova sifra 123',
      role: Role.ROLE_CLIENT,
      bankAccountNumber: '123456789123456789'
    };

    spyOn(appService, 'register').and.returnValue(of({} as ClientsAccount));
    spyOn(appService, 'openErrorDialog');
    fixture.detectChanges();

    component.register();
    expect(appService.openErrorDialog).not.toHaveBeenCalled();

    expect(appService.register).toHaveBeenCalledWith(component.registerForm);
   });

  it('should handle registration with missing fields', () => {
    spyOn(appService, 'register');
    spyOn(appService, 'openErrorDialog');

    component.register();

    expect(appService.register).not.toHaveBeenCalled();
    expect(appService.openErrorDialog).toHaveBeenCalled();
  });
});
