import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AddCarComponent } from './add-car.component';
import { AppService } from '../app.service';
import {HttpClientModule} from "@angular/common/http";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MatDialogModule} from "@angular/material/dialog";
import {NgModule} from "@angular/core";
import {RegisterComponent} from "../register/register.component";
import {of} from "rxjs";
import {DriversAccount} from "../model/driversAccount.model";
@NgModule({
  declarations: [AddCarComponent],
  imports: [
    FormsModule,
    HttpClientTestingModule,
    ReactiveFormsModule,
    MatDialogModule,
  ],
  providers: [AppService],
})
class TestAddCarModule {}
describe('AddCarComponent', () => {
  let component: AddCarComponent;
  let fixture: ComponentFixture<AddCarComponent>;
  let appService: AppService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestAddCarModule, RouterTestingModule],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCarComponent);
    component = fixture.componentInstance;
    appService = TestBed.inject(AppService);
    fixture.detectChanges();
    spyOn(window,'alert');
  });


  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should save the car when save() is called with valid input', () => {
    spyOn(appService, 'setData');
    spyOn(appService, 'addDriverCarAccount').and.returnValue(of({} as DriversAccount));
    component.info.name = 'John';
    component.info.surname = 'Doe';
    component.info.email = 'john.doe@example.com';
    component.info.phone = '1234567890';
    component.info.password = 'password';
    component.info.checkPassword = 'password';
    // Set up test data
    component.info.car.brand = 'Toyota';
    component.info.car.model = 'Camry';
    component.info.car.color = 'Red';
    component.info.car.plateNumber = 'AB123CD';
    component.selectedBodyType = 'SEDAN';
    component.selectedFuelType = 'GASOLINE';
    component.selectedNumOfSeats = '5';

    // Trigger save()
    component.save();

    // Check that the car is saved
    expect(appService.setData).toHaveBeenCalledWith(component.info);
    expect(appService.addDriverCarAccount).toHaveBeenCalledWith(component.info);

    // You can also test any other expected behavior, like displaying a success message or navigating to another page
  });

  it('should show an error message if the brand is not entered', () => {
    // Set up test data with missing brand
    component.info.car.brand = '';
    // Trigger save()
    component.save();

    // Check that an error message is shown
    expect(window.alert).toHaveBeenCalledWith('Unesite marku!');
  });

  it('should show an error message if the color is not entered', () => {
    // Set up test data with missing color
    component.info.car.brand = 'Opel';
    component.info.car.model = 'Astra'
    component.info.car.color = '';
    // Trigger save()
    component.save();

    // Check that an error message is shown
    expect(window.alert).toHaveBeenCalledWith('Unesite boju!');
  });


  it('should show an error message if the selected body type is not valid', () => {
    // Set up test data with invalid body type
    component.info.car.brand = 'Opel';
    component.info.car.model = 'Astra'
    component.info.car.color = 'Crvena';
    component.info.car.plateNumber = 'NS123BG';
    component.selectedBodyType = 'INVALID_TYPE';
    // Trigger save()
    component.save();

    // Check that an error message is shown
    expect(window.alert).toHaveBeenCalledWith('Nevažeći tip karoserije!');
  });

  it('should show an error message if the selected fuel type is not valid', () => {
    // Set up test data with invalid fuel type
    component.info.car.brand = 'Opel';
    component.info.car.model = 'Astra'
    component.info.car.color = 'Crvena';
    component.info.car.plateNumber = 'NS123BG';
    component.selectedBodyType = 'HATCHBACK';
    component.selectedFuelType = 'INVALID_TYPE';
    // Trigger save()
    component.save();

    // Check that an error message is shown
    expect(window.alert).toHaveBeenCalledWith('Nevažeći tip goriva!');
  });

  it('should show an error message if the selected number of seats is not valid', () => {
    // Set up test data with invalid number of seats
    component.info.car.brand = 'Opel';
    component.info.car.model = 'Astra'
    component.info.car.color = 'Crvena';
    component.info.car.plateNumber = 'NS123BG';
    component.selectedBodyType = 'HATCHBACK';
    component.selectedFuelType = 'DIESEL';
    component.selectedNumOfSeats = '10';
    // Trigger save()
    component.save();

    // Check that an error message is shown
    expect(window.alert).toHaveBeenCalledWith('Nevažeći broj sedišta!');
  });

  // Add more test cases to cover different scenarios and edge cases

});
