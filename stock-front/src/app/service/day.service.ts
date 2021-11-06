import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, of} from "rxjs";
import {Day} from "../day";
import { catchError } from 'rxjs/operators';

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

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
