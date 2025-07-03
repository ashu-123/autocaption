import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { throwError } from 'rxjs';

const BASIC_URL = "http://localhost:8080";

@Injectable({
  providedIn: 'root'
})
export class CaptionsService {

  private captionsUrl: string = '/api/images/captions';
  constructor(private http: HttpClient, private authService: AuthService) { }

  getCaptions(formData: FormData) {

    var accessToken: string | null;
    const provider = localStorage.getItem('provider');
    
    if (provider === 'oauth2')
      accessToken = localStorage.getItem('access_token');
    else accessToken = this.authService.getAccessToken();

    if (!accessToken) {
      return throwError(() => new Error('No access token available'));
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${accessToken}`,
    });

    return this.http.post<string[]>(BASIC_URL + this.captionsUrl, formData, { headers: headers });
  }
}
