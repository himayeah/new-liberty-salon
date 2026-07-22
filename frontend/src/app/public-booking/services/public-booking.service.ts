import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpService } from 'src/app/services/http.service';

@Injectable({
  providedIn: 'root'
})
export class PublicBookingService {
  private clientApiUrl = `${environment.baseUrl}/api/v1/client-reg`;
  private appointmentApiUrl = `${environment.baseUrl}/appointment-schedule-form`;
  private employeeApiUrl = `${environment.baseUrl}/employee_get`;
  private serviceApiUrl = `${environment.baseUrl}/service`;

  constructor(private http: HttpClient, private httpService: HttpService) { }

  searchClient(firstName: string, email: string) {
    const requestUrl = `${this.clientApiUrl}/search`;

    let headers = new HttpHeaders();

    const token = this.httpService.getAuthToken();

    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    const params = new HttpParams()
      .set('firstName', firstName)
      .set('email', email);

    // sends GET request with headers + params
    return this.http.get(requestUrl, { headers, params });
  }

  // Checks client email state (new user, existing user without password, or user with password set)
  checkEmailStatus(email: string) {
    const requestUrl = `${this.clientApiUrl}/check-email`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    const params = new HttpParams().set('email', email);
    return this.http.get(requestUrl, { headers, params, responseType: 'text' });
  }

  // Registers a new client with their details and password
  registerClient(clientData: any) {
    const requestUrl = `${this.clientApiUrl}/register`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, clientData, { headers });
  }

  // Sets password for an existing client that has no password stored yet
  setClientPassword(clientData: any) {
    const requestUrl = `${this.clientApiUrl}/set-password`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, clientData, { headers });
  }

  // Authenticates client login via email + password credentials
  loginClient(credentials: any) {
    const requestUrl = `${this.clientApiUrl}/login`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, credentials, { headers });
  }

  // Dispatches a password reset link to the client's email address
  forgotPassword(email: string) {
    const requestUrl = `${this.clientApiUrl}/forgot-password`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    const params = new HttpParams().set('email', email);
    return this.http.post(requestUrl, null, { headers, params });
  }

  // Resets client password using a valid reset token
  resetPassword(resetData: any) {
    const requestUrl = `${this.clientApiUrl}/reset-password`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, resetData, { headers });
  }

  bookAppointment(appointmentData: any) {
    const requestUrl = `${this.appointmentApiUrl}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();

    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }

    // send POST request with body + headers
    return this.http.post(requestUrl, appointmentData, { headers });
  }

  getStylists() {
    const requestUrl = `${this.employeeApiUrl}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();

    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }

    // send GET request with headers
    return this.http.get(requestUrl, { headers });
  }

  getServices() {
    const requestUrl = `${this.serviceApiUrl}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();

    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }

    // send GET request with headers
    return this.http.get(requestUrl, { headers });
  }

}
