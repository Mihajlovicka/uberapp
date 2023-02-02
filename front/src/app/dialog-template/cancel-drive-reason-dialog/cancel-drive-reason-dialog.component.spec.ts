import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelDriveReasonDialogComponent } from './cancel-drive-reason-dialog.component';

describe('CancelDriveReasonDialogComponent', () => {
  let component: CancelDriveReasonDialogComponent;
  let fixture: ComponentFixture<CancelDriveReasonDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CancelDriveReasonDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CancelDriveReasonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
