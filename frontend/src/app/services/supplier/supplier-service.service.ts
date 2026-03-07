import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpService } from '../http.service';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SupplierServiceService {

  constructor(private http: HttpClient, private httpService: HttpService) { }

  private getHeaders() {
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  serviceCall(form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/supplier`;
    return this.http.post(requestUrl, form_details, { headers: this.getHeaders() });
  }

  getData(): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/supplier`;
    return this.http.get(requestUrl, { headers: this.getHeaders() });
  }

  editData(id: number, form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/supplier/${id}`;
    return this.http.put(requestUrl, form_details, { headers: this.getHeaders() });
  }

  deleteData(id: number): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/supplier/${id}`;
    return this.http.delete(requestUrl, { headers: this.getHeaders() });
  }

}
