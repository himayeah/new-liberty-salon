import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpService } from '../http.service';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportProcurementService {

  constructor(private http: HttpClient, private httpService: HttpService) { }

  //write method here
  getPendingPurchaseOrders(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/report/procurement/pending-purchase-orders`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }

}
