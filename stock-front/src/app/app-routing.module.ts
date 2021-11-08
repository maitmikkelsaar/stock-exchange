import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DaysComponent } from './days/days.component';
import {DayDetailComponent} from "./day-detail/day-detail.component";

const routes: Routes = [
  { path: 'days', component: DaysComponent },
  { path: 'detail/:id', component: DayDetailComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
