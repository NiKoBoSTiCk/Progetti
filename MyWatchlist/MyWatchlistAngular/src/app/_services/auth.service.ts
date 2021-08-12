import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { MessageService } from "./message.service";
import { catchError } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl = 'http://localhost:8000/auth';
  private userUrl = 'http://localhost:8000/users';

  constructor(private http: HttpClient, private messageService: MessageService) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post(this.authUrl + '/login', {
      username,
      password
    }).pipe(
      catchError(this.handleError<any>('login'))
    );
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(this.authUrl + '/signup', {
      username,
      email,
      password
    }).pipe(
      catchError(this.handleError<any>('register'))
    );
  }

  getAllUsers(): Observable<any> {
    return this.http.get<any>(this.userUrl).pipe(
      catchError(this.handleError<any>('getAllUsers'))
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
