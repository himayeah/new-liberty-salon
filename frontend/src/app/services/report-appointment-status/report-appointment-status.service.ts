import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpService } from '../http.service';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportAppointmentStatusService {

  constructor(private http: HttpClient, private httpService: HttpService) { }

  getAppointmentCancellationData(): Observable<number> {
    const requestUrl = `${environment.baseUrl}/report-appointment-status/count`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    //backend returns long (A single Number. so you should use 'number' in the frontend.Not any[] since any[] expects an array)
    return this.http.get<number>(requestUrl, { headers });
  }

  getAppointmentCancellationDetails(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/report-appointment-status/cancellation-details`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  getAppointmentsBySource(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/report-appointment-status/bookings-by-source`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  // top 3 services (Pie chart)
  getTop3Services(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/report-appointment-status/top-3-services`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  // Appointment Count By Status(bar chart)
  getAppointmentCountByStatus(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/report-appointment-status/count-by-status`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

}
