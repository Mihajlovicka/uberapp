import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideClientComponent } from './ride-client.component';

describe('RideClientComponent', () => {
  let component: RideClientComponent;
  let fixture: ComponentFixture<RideClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideClientComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RideClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
