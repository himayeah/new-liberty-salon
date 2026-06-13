import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpService } from 'src/app/services/http.service';

@Injectable({
  providedIn: 'root'
})
export class StylistWorkspaceService {
  private apiUrl = `${environment.baseUrl}/api/v1/stylist-workspace`;

  constructor(private http: HttpClient, private httpService: HttpService) { }

}
