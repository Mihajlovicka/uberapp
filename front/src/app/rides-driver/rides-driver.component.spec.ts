import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RidesDriverComponent } from './rides-driver.component';

describe('RidesDriverComponent', () => {
  let component: RidesDriverComponent;
  let fixture: ComponentFixture<RidesDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RidesDriverComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RidesDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
