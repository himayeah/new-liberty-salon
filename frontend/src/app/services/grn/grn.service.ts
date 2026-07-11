import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpService } from '../http.service';

@Injectable({
  providedIn: 'root'
})
export class GrnService {

  private baseUrl = `${environment.baseUrl}/api/grn`;

  constructor(private http: HttpClient, private httpService: HttpService) { }

  serviceCall(form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, form_details, { headers });
  }

  addGrn(form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.post(requestUrl, form_details, { headers });
  }

  updateGrn(id: any, form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.put(requestUrl, form_details, { headers });
  }

  editData(id: any, form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.put(requestUrl, form_details, { headers });
  }

  getByPurchaseOrderId(id: any): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/api/grn/by-purchase-order/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

  deleteGrn(id: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.delete(requestUrl, { headers });
  }

  deleteData(id: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.delete(requestUrl, { headers });
  }

  getData(): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/grn`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get(requestUrl, { headers });
  }
}





