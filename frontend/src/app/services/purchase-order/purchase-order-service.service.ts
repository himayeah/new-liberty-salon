import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpService } from '../http.service';

@Injectable({
  providedIn: 'root'
})
export class PurchaseOrderServiceService {
  private baseUrl = `${environment.baseUrl}/api/purchase-orders`;

  constructor(private http: HttpClient, private httpService: HttpService) { }

  private getHeaders() {
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  getData(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  getById(id: any): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  serviceCall(data: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, data, { headers: this.getHeaders() });
  }

  editData(id: any, data: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() });
  }

  deleteData(id: any): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }
}

