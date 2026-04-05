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

  private getHeaders() {
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  getAllGrn(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  addGrn(data: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, data, { headers: this.getHeaders() });
  }

  updateGrn(id: any, data: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() });
  }

  getByPurchaseOrderId(id: any): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/by-purchase-order/${id}`, { headers: this.getHeaders() });
  }

  deleteGrn(id: any): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }
}





