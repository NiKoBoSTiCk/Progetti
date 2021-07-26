import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { MOCK_SERIES } from '../mock/mock-series';
import { Series } from '../model/series';

export const DEMO_SERIES_STORE = 'demo_series_store';

@Injectable({
  providedIn: 'root'
})
export class SeriesService {

  seriesList: Series[] = [];

  constructor() {
    const stored: string | null = localStorage.getItem(DEMO_SERIES_STORE);
    this.seriesList = stored ? JSON.parse(stored) : this.save(MOCK_SERIES);
  }

  getAll(): Observable<Series[]> {
    return of(this.seriesList);
  }

  get(id: number): Observable<Series> {
    const series = this.seriesList.find(m => m.id === id);
    return series ? of(series) : throwError('Series with id $(id) not found!');
  }

  add(series: Series): Observable<Series> {
    this.seriesList.push(series);
    return of(series)
    .pipe(finalize(() => this.save(this.seriesList)));
  }

  remove(id: number): Observable<void> {
    const seriesIndex = this.seriesList.findIndex(m => m.id === id);
    if(seriesIndex !== -1) {
      this.seriesList.splice(seriesIndex, 1);
      return of(undefined)
      .pipe(finalize(() => this.save(this.seriesList)));
    }
    return throwError('Error: series with id $(id) not found!');
  }

  private save(seriesList: Series[]): Series[] {
    localStorage.setItem(DEMO_SERIES_STORE, JSON.stringify(seriesList));
    return seriesList;
  }
}
