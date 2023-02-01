import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagesClientComponent } from './messages-client.component';

describe('MessagesClientComponent', () => {
  let component: MessagesClientComponent;
  let fixture: ComponentFixture<MessagesClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MessagesClientComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MessagesClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
