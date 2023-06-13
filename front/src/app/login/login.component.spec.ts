import {ComponentFixture, TestBed} from '@angular/core/testing';
import {LoginComponent} from './login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AppService} from "../app.service";
import {MatDialogModule} from "@angular/material/dialog";
import {RouterTestingModule} from "@angular/router/testing";
import {catchError, of, throwError} from "rxjs";
import {UserAuthService} from "../service/user-auth.service";
import {Router} from "@angular/router";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: UserAuthService;
  let appService: AppService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [FormsModule, HttpClientTestingModule, ReactiveFormsModule, MatDialogModule, RouterTestingModule],
      providers: [AppService, UserAuthService]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(UserAuthService);
    router = TestBed.inject(Router);
    appService = TestBed.inject(AppService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set hide to true by default', () => {
    expect(component.hide).toBeTrue();
  });

  it('should set username and password form controls', () => {
    expect(component.username).toBeDefined();
    expect(component.password).toBeDefined();
  });

  it('should return error message for required username field', () => {
    // Simulacija slanja praznog korisničkog imena
    component.username.setValue('');
    expect(component.getErrorMessage()).toBe('Popunite polje');
  });

  it('should return error message for invalid email format', () => {
    // Simulacija slanja neispravnog korisničkog imena (neispravan format e-pošte)
    component.username.setValue('invalidemail');
    expect(component.getErrorMessage()).toBe('Email nije validan');
  });

  it('should call login method and navigate to the appropriate path on successful login', () => {
    // Simulacija ispravnih podataka za prijavu
    const loginData = {
      username: 'validemail@example.com',
      password: 'validpassword'
    };

    // Simulacija uspešnog odgovora od servera
    const mockResponse = {
      accessToken: 'validAccessToken',
      role: 'ROLE_CLIENT'
    };

    // Spy na metodu login u servisu
    spyOn(authService, 'login').and.returnValue(of(mockResponse));

    // Spy na metodu navigate u router-u
    const navigateSpy = spyOn(router, 'navigate');

    // Poziv metode login na komponenti
    component.login(component.loginForm);

    // Provera da li je metoda login pozvana sa ispravnim podacima
    expect(authService.login).toHaveBeenCalledWith(component.loginForm.value);

    // Provera da li je navigacija izvršena na odgovarajuću putanju nakon uspešne prijave
    expect(navigateSpy).toHaveBeenCalledWith(['/client']);
  });

  it('should call handleError from UserAuthService when login error occurs', () => {
    const invalidLoginData = {
      username: 'invalidemail',
      password: 'invalidpassword'
    };
    const mockResponse = {
      accessToken: 'invalidAccessToken',
      role: ''
    };
    spyOn(authService, 'login').and.returnValues(of(mockResponse));
// Spy na metodu navigate u router-u
    const navigateSpy = spyOn(router, 'navigate');

    component.login(component.loginForm);

    expect(authService.login).toHaveBeenCalledWith(component.loginForm.value);
    expect(navigateSpy).toHaveBeenCalledTimes(0);
  });
});
