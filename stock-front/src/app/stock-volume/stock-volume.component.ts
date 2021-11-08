import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl} from '@angular/forms';
import {debounceTime} from "rxjs/operators";
import {DayService} from "../service/day.service";
import {ShareValue} from "../model/shareValue";

@Component({
  selector: 'app-stock-volume',
  templateUrl: './stock-volume.component.html',
  styleUrls: ['./stock-volume.component.css']
})
export class StockVolumeComponent implements OnInit {
  volumes: Array<ShareValue> = []
  displayedColumns = ["name", "value"];
  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });


  constructor(
    private dayService: DayService
  ) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.range.valueChanges.pipe(
      debounceTime(200)
    ).subscribe(event => {
      if (event.start && event.end) {
        this.getVolumes(event.start, event.end);
      }
    });
  }

  getVolumes(start: Date, end: Date): void {
    this.dayService.getVolumes(this.removeTimeZone(start), this.removeTimeZone(end))
    .subscribe(volumes => {
      this.volumes = volumes;
    });
  }

  removeTimeZone(date: Date): Date {
    var timeZoneDifference = (date.getTimezoneOffset() / 60) * -1; //convert to positive value.
    return new Date(date.getTime() + (timeZoneDifference * 60) * 60 * 1000);
  }
}
