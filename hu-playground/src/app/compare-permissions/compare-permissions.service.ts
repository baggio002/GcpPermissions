import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';


import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Role } from './role';
import { Permission } from './permission';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
	  // tslint:disable-next-line: indent
	  'Access-Control-Allow-Origin': '*'
  })
};

@Injectable()
export class RolePermissionService {
  rolesUrl = 'https://backend-dot-zhaohu-prod.uc.r.appspot.com/roles';  // URL to web api
  roleUrl = 'https://backend-dot-zhaohu-prod.uc.r.appspot.com/role'; 
  permissionsUrl = 'https://backend-dot-zhaohu-prod.uc.r.appspot.com/permissions';

  constructor(
    private http: HttpClient) {
  }

  getRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(this.rolesUrl, httpOptions).pipe();
  }

  getRole(id: number): Observable<Role[]> {
    return this.http.get<Role[]>(this.rolesUrl + '/' + id, httpOptions).pipe();
  }

  getPermissionsByRole(id: number): Observable<Permission[]> {
    return this.http.get<Permission[]>(this.roleUrl + '/' + id + '/permissions', httpOptions).pipe();
  }

  getPermissions(): Observable<Permission[]> {
    return this.http.get<Permission[]>(this.permissionsUrl, httpOptions).pipe();
  }

  getPermission(id: number): Observable<Role[]> {
    return this.http.get<Role[]>(this.permissionsUrl + '/' + id, httpOptions).pipe();
  }

}


/*
Copyright Google LLC. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at https://angular.io/license
*/
