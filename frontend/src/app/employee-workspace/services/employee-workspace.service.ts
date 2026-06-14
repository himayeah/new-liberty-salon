import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StylistWorkspaceService {

  private apiUrl = `${environment.baseUrl}/api/v1/stylist-workspace`;

  constructor(private http: HttpClient) { }

  /**
   * Authenticate a stylist with email and password.
   */
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password });
  }

  /**
   * Set the initial password using the invite token.
   */
  setPassword(token: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/set-password`, { token, email, password });
  }

  /**
   * Request a password reset link to be sent to the employee's email.
   */
  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }

  /**
   * Reset the password using the reset token.
   */
  resetPassword(token: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, { token, email, password });
  }
}
