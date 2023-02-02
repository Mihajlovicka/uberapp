import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriveHistoryClientComponent } from './drive-history-client.component';

describe('DriveHistoryClientComponent', () => {
  let component: DriveHistoryClientComponent;
  let fixture: ComponentFixture<DriveHistoryClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriveHistoryClientComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DriveHistoryClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
