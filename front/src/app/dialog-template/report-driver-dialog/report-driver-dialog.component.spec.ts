import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportDriverDialogComponent } from './report-driver-dialog.component';

describe('ReportDriverDialogComponent', () => {
  let component: ReportDriverDialogComponent;
  let fixture: ComponentFixture<ReportDriverDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportDriverDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportDriverDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
