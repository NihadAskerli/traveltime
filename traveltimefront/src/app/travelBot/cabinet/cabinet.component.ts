import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cabinet',
  templateUrl: './cabinet.component.html',
  styleUrls: ['./cabinet.component.css']
})
export class CabinetComponent implements OnInit {

  constructor(private route:Router) { }

  ngOnInit(): void {
  }
  passQeydiyyat(){
    this.route.navigate(['/reg'])
    }
    passDaxil(){
    this.route.navigate(['/daxil'])
    }

}
