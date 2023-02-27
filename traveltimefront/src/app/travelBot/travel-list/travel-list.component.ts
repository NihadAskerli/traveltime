import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { TravelService } from '../travel.service';

@Component({
  selector: 'app-travel-list',
  templateUrl: './travel-list.component.html',
  styleUrls: ['./travel-list.component.css'],
})
export class TravelListComponent implements OnInit {
  newData!: any;
  teklif: any;
  constructor(private travelService: TravelService) {}

  ngOnInit(): void {
    this.getNewData();
    
  }

  // suggestion(id: any) {
  //   console.log(this.teklif)
  //   const offer = { id: id, teklif: this.teklif };
  //   console.log(offer)
  //   return  { id: id, teklif: this.teklif };
  // }

  suggest(id: any) {
    const offer={
      id:String,
      suggestion:String,
    };
    offer.id=id;
    offer.suggestion=this.teklif;
    // console.log(offer);
    this.travelService.addData(offer).subscribe({
      next:(x)=>{
        console.log(x);
      }}
    )
     
    


  }

  getNewData(){
    console.log(this.travelService.getDataList());
    this.travelService.getDataList().subscribe((res) => {
      this.newData = res;
      console.log(this.newData);
    });
  }
}
