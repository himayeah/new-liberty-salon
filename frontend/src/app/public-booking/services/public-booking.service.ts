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

  registerClient(clientData: any) {
    const requestUrl = `${this.clientApiUrl}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();

    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }

    // send POST request with body + headers
    return this.http.post(requestUrl, clientData, { headers });
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
