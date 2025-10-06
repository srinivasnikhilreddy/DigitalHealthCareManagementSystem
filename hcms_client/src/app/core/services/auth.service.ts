import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthResponse } from '../models/auth-response.model';
import { RefreshTokenRequest } from '../models/refresh-token.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService
{
    private api = `${environment.apiBaseUrl}/auth`;
    private tokenKey = 'jwt-token';
    private refreshTokenKey = 'refresh-token';
    private roleKey = 'user-role';
    private loggedIn$ = new BehaviorSubject<boolean>(this.hasValidAccessToken());
    public isLoggedIn$ = this.loggedIn$.asObservable();

    constructor(private http: HttpClient, private router: Router) {}

    /** Register with optional profile picture */
    register(user: any, file?: File): Observable<boolean> {
      const formData = new FormData();
      formData.append('user', new Blob([JSON.stringify(user)], { type: 'application/json' }));
      if (file) {
        formData.append('file', file);
      }

      return this.http.post<AuthResponse>(`${this.api}/register`, formData).pipe(
        map(res => {
          if (res && res['access-token'] && res['refresh-token']) {
            this.storeAuthResponse(res);
            return true;
          }
          return false;
        }),
        catchError(err => {
          console.error('Registration error', err);
          return of(false);
        })
      );
    }

    /** Login */
    login(credentials: { username: string; password: string }): Observable<boolean> {
        return this.http.post<AuthResponse>(`${this.api}/login`, credentials).pipe(
            tap(res => this.storeAuthResponse(res)),
            map(res => !!res && !!res['access-token']),
            catchError(err => {
                console.error('Login error', err);
                return of(false);
            })
        );
    }

    /** Refresh JWT */
    refreshToken(): Observable<string> {
        const refreshToken = localStorage.getItem(this.refreshTokenKey);
        if (!refreshToken) {
            this.logout();
            return throwError(() => new Error('No refresh token'));
        }
        const req: RefreshTokenRequest = { 'refresh-token': refreshToken };
        return this.http.post<AuthResponse>(`${this.api}/refresh`, req).pipe(
            tap(res => this.storeAuthResponse(res)),
            map(res => res['access-token']),
            catchError(err => {
                this.logout();
                return throwError(() => err);
            })
        );
    }

    logout(): void {
        const refreshToken = localStorage.getItem(this.refreshTokenKey);
        if (refreshToken) {
            this.http.post(`${this.api}/logout`, { 'refresh-token': refreshToken }).subscribe();
        }
        localStorage.removeItem(this.tokenKey);
        localStorage.removeItem(this.refreshTokenKey);
        localStorage.removeItem(this.roleKey);
        localStorage.removeItem('username');
        localStorage.removeItem('patientId');
        this.loggedIn$.next(false);
        this.router.navigate(['/auth/login']);
    }

    private storeAuthResponse(res: AuthResponse): void {
        if (!res) return;
        localStorage.setItem(this.tokenKey, res['access-token']);
        localStorage.setItem(this.refreshTokenKey, res['refresh-token']);
        if (res.role) {
            localStorage.setItem(this.roleKey, res.role);
        } else if (res.roles && res.roles.length > 0) {
            localStorage.setItem(this.roleKey, res.roles[0]);
        }
        this.loggedIn$.next(true);
    }

    hasValidAccessToken(): boolean {
        return !!localStorage.getItem(this.tokenKey);
    }

    getAccessToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    getUserRole(): string | null {
        return localStorage.getItem(this.roleKey);
    }
}
