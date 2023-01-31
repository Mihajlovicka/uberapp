import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriveParticipantsComponent } from './drive-participants.component';

describe('DriveParticipantsComponent', () => {
  let component: DriveParticipantsComponent;
  let fixture: ComponentFixture<DriveParticipantsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriveParticipantsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DriveParticipantsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
