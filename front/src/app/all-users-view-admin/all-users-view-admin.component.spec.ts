import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllUsersViewAdminComponent } from './all-users-view-admin.component';

describe('AllUsersViewAdminComponent', () => {
  let component: AllUsersViewAdminComponent;
  let fixture: ComponentFixture<AllUsersViewAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllUsersViewAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllUsersViewAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
