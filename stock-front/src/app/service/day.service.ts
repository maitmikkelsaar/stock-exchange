import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from "rxjs";
import {Day} from "../model/day";
import { catchError } from 'rxjs/operators';
import {Details} from "../model/details";

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

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
