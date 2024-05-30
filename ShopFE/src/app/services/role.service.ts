import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private apiGetRoles = `${environment.baseUrl}/roles`;

  constructor(private http: HttpClient) { }

  getRoles(){
    this.http.get(this.apiGetRoles);
  }


}
