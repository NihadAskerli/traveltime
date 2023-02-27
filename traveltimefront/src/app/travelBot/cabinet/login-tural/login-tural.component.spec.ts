import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginTuralComponent } from './login-tural.component';

describe('LoginTuralComponent', () => {
  let component: LoginTuralComponent;
  let fixture: ComponentFixture<LoginTuralComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginTuralComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginTuralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
