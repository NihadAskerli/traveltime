import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-travel',
  templateUrl: './travel.component.html',
  styleUrls: ['./travel.component.css']
})
export class TravelComponent implements OnInit {

  constructor(private route:Router) { }

  ngOnInit(): void {
  }

  agent(){
    this.route.navigate(['/cabinet']);
  }

}
