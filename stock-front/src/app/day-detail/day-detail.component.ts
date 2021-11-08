import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DayService} from '../service/day.service';
import {Details} from "../model/details";
import {ShareValue} from "../model/shareValue";
import { registerLocaleData } from '@angular/common';
import localeEt from '@angular/common/locales/et';

@Component({
  selector: 'app-day-detail',
  templateUrl: './day-detail.component.html',
  styleUrls: ['./day-detail.component.css']
})
export class DayDetailComponent implements OnInit {
  details: Details | undefined;
  displayedColumns = ["name", "value"];
  gainersDecliners = new Array<ShareValue>();

  constructor(
    private route: ActivatedRoute,
    private dayService: DayService
  ) { }

  ngOnInit(): void {
    registerLocaleData(localeEt, 'et');
    this.getDay();
  }

  getDay(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.dayService.getDayDetails(id)
    .subscribe(details => {
      this.details = details;
      this.gainersDecliners = details.gainersDecliners.gainers.concat(details.gainersDecliners.decliners);
    });
  }


}
