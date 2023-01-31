import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteRoutesDialogComponent } from './favorite-routes-dialog.component';

describe('FavoriteRoutesDialogComponent', () => {
  let component: FavoriteRoutesDialogComponent;
  let fixture: ComponentFixture<FavoriteRoutesDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoriteRoutesDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FavoriteRoutesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
