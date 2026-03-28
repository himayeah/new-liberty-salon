import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpService } from '../http.service';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductServiceService {
  constructor(private http: HttpClient, private httpService: HttpService) { }

  private getHeaders() {
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  serviceCall(form_detalils: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/product`;
    return this.http.post(requestUrl, form_detalils, { headers: this.getHeaders() });
  }

  getData(): Observable<any> {
    const requestUrl = `${environment.baseUrl}/product`;
    return this.http.get(requestUrl, { headers: this.getHeaders() });
  }

  editData(id: number, form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/product/${id}`;
    return this.http.put(requestUrl, form_details, { headers: this.getHeaders() });
  }

  deleteData(id: number): Observable<any> {
    const requestUrl = `${environment.baseUrl}/product/${id}`;
    return this.http.delete(requestUrl, { headers: this.getHeaders() });
  }

}
