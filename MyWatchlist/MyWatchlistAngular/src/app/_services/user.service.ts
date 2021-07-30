import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const WATCHLIST_URL = 'http://localhost:8000/watchlist/';
const SERIES_URL = 'http://localhost:8000/series/search/';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  getAllSeries(): Observable<any> {
    return this.http.get(SERIES_URL);
  }

  getUserBoard(): Observable<any> {
    return this.http.get(WATCHLIST_URL + 'user', { responseType: 'text' });
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(WATCHLIST_URL + 'mod', { responseType: 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(WATCHLIST_URL + 'admin', { responseType: 'text' });
  }
}
