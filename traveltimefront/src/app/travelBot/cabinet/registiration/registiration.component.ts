import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registiration',
  templateUrl: './registiration.component.html',
  styleUrls: ['./registiration.component.css']
})
export class RegistirationComponent implements OnInit {

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
