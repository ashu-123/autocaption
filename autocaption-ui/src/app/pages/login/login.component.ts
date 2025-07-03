import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ RouterModule, FormsModule ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  backendBaseUrl = 'http://localhost:8080'; // change to your backend domain

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    // Handle Google redirect: look for ?code=
    this.route.queryParams.subscribe((params) => {
      const code = params['code'];
      if (code) {
        this.exchangeGoogleCode(code);
      }
    });
  }

  loginWithCredentials() {
    localStorage.setItem('provider', 'credentials');
    this.http
      .post(`${this.backendBaseUrl}/auth/login`, {
        username: this.username,
        password: this.password,
      })
      .subscribe({
        next: (res: any) => {
          this.storeTokens(res);
        },
        error: (err) => {
          alert('Invalid credentials');
        },
      });
  }

  loginWithGoogle() {
    localStorage.setItem('provider', 'oauth2');
    const redirectUri = encodeURIComponent(
      window.location.origin + '/oauth2/callback/google' // must match what's in Google console
    );
    const clientId = '676495656186-l59epksbb4ag3c17fss4sbtgv79tkob1.apps.googleusercontent.com';

    const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=openid%20email%20profile&access_type=offline&prompt=consent`;

    window.location.href = googleAuthUrl;
  }

  exchangeGoogleCode(code: string) {
    const params = {
      code: code
    };
    this.http
      .get(`${this.backendBaseUrl}/oauth2/callback/google`, {
        params: params
      })
      .subscribe({
        next: (res: any) => {
          this.storeTokens(res);
        },
        error: (err) => {
          alert('Google login failed');
        },
      });
  }

  private storeTokens(res: { accessToken: string; refreshToken: string }) {
    localStorage.setItem('access_token', res.accessToken);
    localStorage.setItem('refresh_token', res.refreshToken);
    console.log(res.accessToken);
    this.router.navigate(['/captions/generate']); // or home page
  }
}
