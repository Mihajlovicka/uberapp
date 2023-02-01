import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RespondDriveRequestComponent } from './respond-drive-request.component';

describe('RespondDriveRequestComponent', () => {
  let component: RespondDriveRequestComponent;
  let fixture: ComponentFixture<RespondDriveRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RespondDriveRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RespondDriveRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
