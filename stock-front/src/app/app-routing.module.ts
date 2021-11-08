import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DaysComponent } from './days/days.component';
import {DayDetailComponent} from "./day-detail/day-detail.component";
import {StockVolumeComponent} from "./stock-volume/stock-volume.component";

const routes: Routes = [
  { path: 'days', component: DaysComponent },
  { path: 'detail/:id', component: DayDetailComponent },
  { path: 'volume', component: StockVolumeComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
