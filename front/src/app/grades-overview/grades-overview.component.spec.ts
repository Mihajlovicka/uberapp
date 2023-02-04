import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradesOverviewComponent } from './grades-overview.component';

describe('GradesOverviewComponent', () => {
  let component: GradesOverviewComponent;
  let fixture: ComponentFixture<GradesOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GradesOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GradesOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
