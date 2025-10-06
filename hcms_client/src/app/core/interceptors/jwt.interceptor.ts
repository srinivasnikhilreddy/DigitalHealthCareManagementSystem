import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

export const jwtInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const authService = inject(AuthService);
  let token = authService.getAccessToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }

  return next(authReq).pipe(
    catchError(err => {
      if (err.status === 401) {
        const refreshSubject = new BehaviorSubject<string | null>(null);
        return authService.refreshToken().pipe(
          switchMap(newToken => {
            token = newToken;
            refreshSubject.next(newToken);
            return next(req.clone({ setHeaders: { Authorization: `Bearer ${newToken}` } }));
          }),
          catchError(e => {
            authService.logout();
            return throwError(() => e);
          })
        );
      }
      return throwError(() => err);
    })
  );
};
