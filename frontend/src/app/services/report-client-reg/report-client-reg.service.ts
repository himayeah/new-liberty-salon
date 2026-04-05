import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpService } from '../http.service';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportClientRegService {

  constructor(private http: HttpClient, private httpService: HttpService) { }

  getData(): Observable<any[]> {
    const requestUrl = `${environment.baseUrl}/report-client-controller`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get<any[]>(requestUrl, { headers });
  }
}
