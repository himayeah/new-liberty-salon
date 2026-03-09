import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpService } from '../http.service';
import { environment } from 'src/app/environments/environment';

@Injectable({
    providedIn: 'root',
})
export class InventoryServiceService {
    constructor(private http: HttpClient, private httpService: HttpService) { }

    private getHeaders() {
        let headers = new HttpHeaders();
        const token = this.httpService.getAuthToken();
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return headers;
    }

    serviceCall(formDetails: any) {
        const requestUrl = `${environment.baseUrl}/inventory`;
        return this.http.post(requestUrl, formDetails, { headers: this.getHeaders() });
    }

    getData() {
        const requestUrl = `${environment.baseUrl}/inventory`;
        return this.http.get(requestUrl, { headers: this.getHeaders() });
    }

    editData(id: number, form_details: any) {
        const requestUrl = `${environment.baseUrl}/inventory/${id}`;
        return this.http.put(requestUrl, form_details, { headers: this.getHeaders() });
    }

    deleteData(id: number) {
        const requestUrl = `${environment.baseUrl}/inventory/${id}`;
        return this.http.delete(requestUrl, { headers: this.getHeaders() });
    }
}
