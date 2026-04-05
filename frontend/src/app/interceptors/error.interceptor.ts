import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { HttpService } from '../services/http.service';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private httpService: HttpService, private router: Router) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error) => {
        console.error('HTTP Error:', error);
        if (error?.status === 401) {
          this.httpService.logOut();
          this.router.navigate(['/login']);
        }
        // Return the error message if available, otherwise return a default message
        const errorMessage = error?.error?.message || error?.message || 'An error occurred';
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
