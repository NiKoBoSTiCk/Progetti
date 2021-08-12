import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { Series } from "../models/series";
import { catchError } from "rxjs/operators";
import { MessageService } from "./message.service";

@Injectable({
  providedIn: 'root'
})
export class SeriesService {
  private seriesUrl = 'http://localhost:8000/series';

  httpWithoutInterceptor: HttpClient;

  constructor(private http: HttpClient,
              private httpBackend: HttpBackend,
              private messageService: MessageService) {
    this.httpWithoutInterceptor = new HttpClient(httpBackend);
  }

  getAllSeries(pageNumber:number, pageSize:number, sortBy:string): Observable<any> {
    return this.httpWithoutInterceptor.get<any>(
      this.seriesUrl + '/search/all' +  '?pageSize=' + pageSize + '&pageNumber=' + pageNumber + '&sortBy=' + sortBy)
      .pipe(
      catchError(this.handleError<any>('getAllSeries', []))
    );
  }

  getSeriesById(id:number): Observable<Series> {
    return this.httpWithoutInterceptor.get<Series>(
      this.seriesUrl + '/search/' + id)
      .pipe(
        catchError(this.handleError<Series>('getSeriesById'))
      );
  }

  getSeriesByTitle(title:string): Observable<Series[]> {
    return this.httpWithoutInterceptor.get<Series[]>(
      this.seriesUrl + '/search/by_title' + '?title=' + title)
      .pipe(
        catchError(this.handleError<Series[]>('getSeriesByTitle'))
      );
  }

  getSeriesByGenre(genre:string): Observable<Series[]> {
    return this.httpWithoutInterceptor.get<Series[]>(
      this.seriesUrl + '/search/by_genre' + '?genre=' + genre)
      .pipe(
        catchError(this.handleError<Series[]>('getSeriesByGenre', []))
      );
  }

  addSeries(title:string, episodes:number, plot:string, genres:string[]): Observable<any>{
    return this.http.post(this.seriesUrl,
      {
        title: title,
        episodes: episodes,
        plot: plot,
        genres: genres
      })
      .pipe(
        catchError(this.handleError('addSeries'))
      );
  }

  deleteSeries(title:string): Observable<any>{
    console.log(" " + title);
    return this.http.delete(this.seriesUrl + '?title=' + title
    ).pipe(
      catchError(this.handleError('deleteSeries'))
    );
  }

  updateSeries(title:string, episodes:number, plot:string, genres:string[]): Observable<any>{
    return this.http.put(this.seriesUrl, {
      title: title,
      episodes: episodes,
      plot: plot,
      genres: genres
    }).pipe(
      catchError(this.handleError<Series>('updateSeries'))
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
    this.messageService.add(`SeriesService: ${message}`);
  }
}

