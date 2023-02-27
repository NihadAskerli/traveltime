import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TravelService } from '../../travel.service';

@Component({
  selector: 'app-login-tural',
  templateUrl: './login-tural.component.html',
  styleUrls: ['./login-tural.component.css']
})
export class LoginTuralComponent implements OnInit {

  constructor(
    private route: Router,
    private travelService: TravelService,
    private fb: FormBuilder
  ) {}

  
  loginForm: FormGroup;

  ngOnInit(): void {
    this.initialForm();
  }

  initialForm() {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
  passQeydiyyat() {
    this.route.navigate(['/reg']);
  }
  passDaxil() {
    this.route.navigate(['/daxil']);
  }

  check() {
    if (
      this.loginForm.get('email').value == 'traveltime@gmail.com' &&
      this.loginForm.get('password').value == '12345'
    ) {
      this.route.navigate(['/list']);
    } else {
      return;
    }
  }


}
