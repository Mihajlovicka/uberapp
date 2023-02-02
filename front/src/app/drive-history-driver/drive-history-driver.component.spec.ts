import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriveHistoryDriverComponent } from './drive-history-driver.component';

describe('DriveHistoryDriverComponent', () => {
  let component: DriveHistoryDriverComponent;
  let fixture: ComponentFixture<DriveHistoryDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriveHistoryDriverComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DriveHistoryDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
