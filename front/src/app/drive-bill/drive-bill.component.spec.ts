import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriveBillComponent } from './drive-bill.component';

describe('DriveBillComponent', () => {
  let component: DriveBillComponent;
  let fixture: ComponentFixture<DriveBillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriveBillComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DriveBillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
