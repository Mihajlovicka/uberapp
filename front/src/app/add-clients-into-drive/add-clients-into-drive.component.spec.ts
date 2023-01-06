import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddClientsIntoDriveComponent } from './add-clients-into-drive.component';

describe('AddClientsIntoDriveComponent', () => {
  let component: AddClientsIntoDriveComponent;
  let fixture: ComponentFixture<AddClientsIntoDriveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddClientsIntoDriveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddClientsIntoDriveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
