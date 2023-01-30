import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectedVehicleTypeComponent } from './selected-vehicle-type.component';

describe('SelectedVehicleTypeComponent', () => {
  let component: SelectedVehicleTypeComponent;
  let fixture: ComponentFixture<SelectedVehicleTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectedVehicleTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectedVehicleTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
