import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { CabinetComponent } from './travelBot/cabinet/cabinet.component';
import { LoginTuralComponent } from './travelBot/cabinet/login-tural/login-tural.component';
import { RegistirationComponent } from './travelBot/cabinet/registiration/registiration.component';
import { TravelListComponent } from './travelBot/travel-list/travel-list.component';
import { TravelComponent } from './travelBot/travel/travel.component';

const route: Routes = [
  { path: '', component: TravelComponent },
  { path: 'reg', component: RegistirationComponent },
  { path: 'daxil', component: LoginTuralComponent },
  {path:'cabinet', component:CabinetComponent},
  {path:'list', component:TravelListComponent  }
];

@NgModule({
  declarations: [
    AppComponent,
    TravelComponent,
    TravelListComponent,
    CabinetComponent,
    LoginTuralComponent,
    RegistirationComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(route),
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
        
  ],
  exports: [RouterModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
