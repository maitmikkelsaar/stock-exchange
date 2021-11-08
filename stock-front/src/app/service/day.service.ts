import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from "rxjs";
import {Day} from "../model/day";
import { catchError } from 'rxjs/operators';
import {Details} from "../model/details";
import {ShareValue} from "../model/shareValue";
import {TimeRangeRequest} from "../model/timeRangeRequest";

@Injectable({
  providedIn: 'root'
})
export class DayService {

  private daysUrl = 'http://localhost:8080/days';

  constructor(private http: HttpClient) { }

  getDays(): Observable<Day[]> {
    return this.http.get<Day[]>(this.daysUrl).pipe(
      catchError(this.handleError<Day[]>('getDays', []))
    );
  }

  getDayDetails(id: number): Observable<Details> {
    const url = `${this.daysUrl}/${id}/detail`;
    return this.http.get<Details>(url).pipe(
      catchError(this.handleError<Details>(`getDayDetails id=${id}`))
    );
  }

  getVolumes(start: Date, end: Date): Observable<ShareValue[]> {
    const url = `${this.daysUrl}/detail/volume`;
    var body: TimeRangeRequest = {start, end};
    return this.http.post<ShareValue[]>(url, body).pipe(
      catchError(this.handleError<ShareValue[]>(`getVolumes start=${start}, end=${end}`))
    )
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
