import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthRequest } from '../model/AuthRequest';
import { AuthResponse } from '../model/AuthResponse';

const BASIC_URL = "http://localhost:8080"
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl: string = '/auth/login';
  private accessToken: string | null = null;
  private refreshToken: string | null = null;

  constructor(private http: HttpClient) { }

  getTokens(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(BASIC_URL + this.authUrl, authRequest)
      .pipe(
        tap(res => {
          this.accessToken = res.accessToken;
          this.refreshToken = res.refreshToken;
        })
      );
  }

  getAccessToken(): string | null {
    return this.accessToken;
  }
}
