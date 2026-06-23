import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { HttpService } from '../http.service';

@Injectable({
  providedIn: 'root'
})
export class AppointmentSchedulingServiceService {

  constructor(
    private http: HttpClient,
    private httpService: HttpService
  ) { }

  serviceCall(form_details: any): Observable<any> {

    const requestUrl = `${environment.baseUrl}/appointment-schedule-form`;

    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }

    return this.http.post(requestUrl, form_details, { headers });
  }

  getData() {

    const requestUrl = `${environment.baseUrl}/appointment-schedule-form`;

    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get(requestUrl, { headers });
  }

  //Retrieve the Max ID, Increment that value from 10 and return the fina Result - Q2
  getMaxId() {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/max-id`;

    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get(requestUrl, { headers });
  }

  deleteData(id: number) {

    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/${id}`;

    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.delete(requestUrl, { headers });
  }

  editData(id: number, form_details: any) {
    console.log('In Edit Data');

    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/${id}`;

    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.put(requestUrl, form_details, { headers });
  }

  //Dashoard card
  getAppointmentCountLast30Days(): Observable<number> {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/count-last-30-days`;

    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<number>(requestUrl, { headers });
  }

  //Dashboard card (getMostUsedService)
  getMostUsedService(): Observable<string> {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/get-most-used-service`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get(requestUrl, { headers, responseType: 'text' }) as Observable<string>;
  }

  //Dashboard line chart (getAppointmentCountsByMonth)
  getAppointmentCountsByMonth(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/count-by-month`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  //Dashboard pie chart (getTop3Services)
  getTop3Services(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/top-3-services`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }
  //Dashboard table (getTop5Employees)
  getTop5Employees(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/top-5-employees`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  // Dashboard Appointment Timeline Notification (Next 30 minutes)
  getUpcomingNotifications(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/upcoming-notifications`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  // send status update to backend
  sendStatusUpdates(id: number, status: string) {
    console.log('Data sent from frontend successfully');
    const requestUrl = `${environment.baseUrl}/appointment-schedule-form/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, { appointmentStatus: status }, { headers });
  }

}
