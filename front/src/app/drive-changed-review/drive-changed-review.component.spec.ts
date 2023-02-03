import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriveChangedReviewComponent } from './drive-changed-review.component';

describe('DriveChangedReviewComponent', () => {
  let component: DriveChangedReviewComponent;
  let fixture: ComponentFixture<DriveChangedReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriveChangedReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DriveChangedReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
