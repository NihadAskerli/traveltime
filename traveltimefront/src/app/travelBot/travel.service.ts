import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TravelService {

  constructor(private http:HttpClient) { }

  

  getDataList(){
    return this.http.get('http://localhost:8081/travelTime/answer'); //bura ise o cedveller ucun cekilecek datanin url'in yazin.
  }

  addData(postedData:any):Observable<any>{
    console.log(postedData)
  return this.http.post('http://localhost:8081/travelTime/offer',postedData);//burada stringi post etmeyimcun urli yaz bura sonra serveri qos

  }
}
