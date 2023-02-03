import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RespondToPaymentRequestComponent } from './respond-to-payment-request.component';

describe('RespondToPaymentRequestComponent', () => {
  let component: RespondToPaymentRequestComponent;
  let fixture: ComponentFixture<RespondToPaymentRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RespondToPaymentRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RespondToPaymentRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
