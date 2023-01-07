import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakeDriveReservationComponent } from './make-drive-reservation.component';

describe('MakeDriveReservationComponent', () => {
  let component: MakeDriveReservationComponent;
  let fixture: ComponentFixture<MakeDriveReservationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakeDriveReservationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MakeDriveReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
