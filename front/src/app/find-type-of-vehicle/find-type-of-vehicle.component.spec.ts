import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindTypeOfVehicleComponent } from './find-type-of-vehicle.component';

describe('FindTypeOfVehicleComponent', () => {
  let component: FindTypeOfVehicleComponent;
  let fixture: ComponentFixture<FindTypeOfVehicleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FindTypeOfVehicleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FindTypeOfVehicleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
