import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountAccessReviewComponent } from './account-access-review.component';

describe('AccountAccessReviewComponent', () => {
  let component: AccountAccessReviewComponent;
  let fixture: ComponentFixture<AccountAccessReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountAccessReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountAccessReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
