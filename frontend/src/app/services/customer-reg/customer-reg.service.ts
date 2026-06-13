import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpService } from '../http.service';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerRegService {

  // you typically put dependancies in the constructor,
  // the things your class needs Angular to provide, ex: when you call methods from another class
  constructor(private http: HttpClient, private httpService: HttpService) { }

  serviceCall(form_details: any): Observable<any> {
    const requestUrl = `${environment.baseUrl}/api/v1/client-reg`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token );
    }
    return this.http.post(requestUrl, form_details, {headers})
  }

  editData(id: number, form_details:any) {
    const requestUrl = `${environment.baseUrl}/api/v1/client-reg/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.put(requestUrl, form_details, {headers: headers});
  }

  deleteData(id: number) {
    const requestUrl = `${environment.baseUrl}/api/v1/client-reg/${id}`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.delete(requestUrl, { headers });
  }

  getData(){
    const requestUrl = `${environment.baseUrl}/api/v1/client-reg`;
    let headers = new HttpHeaders();
    const token = this.httpService.getAuthToken();
    if (token !== null) {
      headers = headers.set('Authorization', 'Bearer ' + token);
    }
    return this.http.get(requestUrl, { headers });
  }

}
