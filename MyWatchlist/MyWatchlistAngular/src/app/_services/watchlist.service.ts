import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { MessageService } from "./message.service";
import { Observable, of } from "rxjs";
import { catchError } from "rxjs/operators";
import { Watchlist } from "../models/watchlist";

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  private watchlistUrl = 'http://localhost:8000/watchlist/';

  constructor(private http: HttpClient, private messageService: MessageService) { }

  addWatchlist(title:string, username:string): Observable<any>{
    return this.http.post(this.watchlistUrl,
      {
        title: title,
        username: username
      })
      .pipe(
        catchError(this.handleError('addWatchlist'))
      );
  }

  deleteWatchlist(title:string, username:string): Observable<any>{
    return this.http.delete(this.watchlistUrl, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {
        title: title,
        username: username
      },
    }).pipe(
      catchError(this.handleError('deleteWatchlist'))
    );
  }

  updateWatchlist(title:string, username:string, status:string, progress:number,  score:number, comment:string): Observable<any>{
    return this.http.put(this.watchlistUrl, {
      title: title,
      username: username,
      progress: progress,
      status: status,
      score: score,
      comment: comment
    }).pipe(
      catchError(this.handleError('updateWatchlist'))
    );
  }

  getWatchlist(username:string): Observable<Watchlist[]> {
    return this.http.get<Watchlist[]>(
      this.watchlistUrl + username)
      .pipe(
        catchError(this.handleError<Watchlist[]>('getWatchlist', []))
      );
  }

  getWatchlistByStatus(username:string, status:string): Observable<Watchlist[]> {
    return this.http.get<Watchlist[]>(
      this.watchlistUrl + username + '/by_status?status=' + status)
      .pipe(
        catchError(this.handleError<Watchlist[]>('getWatchlist', []))
      );
  }

  getWatchlistById(username:string, id:number): Observable<Watchlist> {
    return this.http.get<Watchlist>(
      this.watchlistUrl + username + '/' + id)
      .pipe(
        catchError(this.handleError<Watchlist>('getWatchlist'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add(`WatchlistService: ${message}`);
  }
}
