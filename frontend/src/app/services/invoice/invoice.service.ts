import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpService } from '../http.service';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  constructor(
    private http: HttpClient,
    private httpService: HttpService
  ) { }

  getInvoiceByBillingId(billingId: number): Observable<any> {
    const requestUrl = `${environment.baseUrl}/invoice/billing/${billingId}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get(requestUrl, { headers });
  }
}
