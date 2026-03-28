import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpService } from '../http.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeAttendanceServiceService {

  constructor(
    private http: HttpClient,
    private httpService: HttpService
  ) { }

  serviceCall(formDetails: any) {
    const requestUrl = `${environment.baseUrl}/employee-attendance`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post(requestUrl, formDetails, { headers });
  }

  getData() {
    const requestUrl = `${environment.baseUrl}/employee-attendance`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get(requestUrl, { headers });
  }

  editData(id: number, form_details: any) {
    const requestUrl = `${environment.baseUrl}/employee-attendance/${id}`;
    let headers: any = {};
    if (this.httpService.getAuthToken() != null) {
      headers = {
        Authorization: 'Bearer ' + this.httpService.getAuthToken()
      };
    }
    return this.http.put(requestUrl, form_details, { headers });
  }

  deleteData(id: number) {
    const requestUrl = `${environment.baseUrl}/employee-attendance/${id}`;
    let headers: any = {};
    if (this.httpService.getAuthToken() != null) {
      headers = {
        Authorization: 'Bearer ' + this.httpService.getAuthToken()
      };
    }
    return this.http.delete(requestUrl, { headers });
  }
}

