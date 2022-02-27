import { Component, OnInit } from '@angular/core';
import { Observable, OperatorFunction } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';

import { RolePermissionService } from './compare-permissions.service';
import { Role } from './role';
import { NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { Permission } from './permission';

@Component({
  selector: 'app-compare-permissions',
  templateUrl: 'compare-permissions.component.html',
  providers: [RolePermissionService],
  styleUrls: ['compare-permissions.component.css'],
})

export class ComparePermissionsComponent implements OnInit {
  // rolePermissionService: RolePermissionService;

  constructor(private rolePermissionService: RolePermissionService) {
  }
  roles: Role[] = [];

  roleSelected: Set<Role> = new Set<Role>();
  roleSelected2: Set<Role> = new Set<Role>();

  permissions: Permission[] = [];

  permissionSelected: Set<Permission> = new Set<Permission>();
  permissionSelected2: Set<Permission> = new Set<Permission>();

  excessPermissions: Set<Permission> = new Set<Permission>();
  excessPermissions2: Set<Permission> = new Set<Permission>();

  samePermissions: Set<Permission> = new Set<Permission>();
  samePermissions2: Set<Permission> = new Set<Permission>();


  public role: Role | null | undefined;
  public role2: Role | null | undefined;


  public permission: Permission | null | undefined;
  public permission2: Permission | null | undefined;

  ngOnInit() {
    this.getRoles();
    this.getPermissions();
  }

  getRoles(): void {
    this.rolePermissionService.getRoles()
      .subscribe(roles => {
        (this.roles = roles);
      });
  }

  getPermissions(): void {
    this.rolePermissionService.getPermissions()
      .subscribe(permissions => {
        (this.permissions = permissions);
      });
  }

  setRole(e: NgbTypeaheadSelectItemEvent<Role>) {
    this.roleSelected.add(e.item);
    this.rolePermissionService.getPermissionsByRole(e.item.id).subscribe(permissions => {
      let i = 0;
      permissions.forEach((permission) => {
        if (!this.hasPermission(this.permissionSelected, permission)) {
            this.permissionSelected.add(permission);
        }
        i++;
        if (i == permissions.length) {
          this.compare();
        }
      })
    });
  }

  setRole2(e: NgbTypeaheadSelectItemEvent<Role>) {
    this.roleSelected2.add(e.item);
    this.rolePermissionService.getPermissionsByRole(e.item.id).subscribe(permissions => {
      let i = 0;
      permissions.forEach((permission) => {
        if (!this.hasPermission(this.permissionSelected2, permission)) {
            this.permissionSelected2.add(permission);
        }
        i++;
        if (i == permissions.length) {
          this.compare();
        }
      })
    });
    this.findExcessAndSamePermission2();
  }

  setPermission(e: NgbTypeaheadSelectItemEvent<Permission>) {
    this.permissionSelected.add(e.item);
    this.compare();
  }

  setPermission2(e: NgbTypeaheadSelectItemEvent<Permission>) {
    this.permissionSelected2.add(e.item);
    this.compare();

  }

  onAddRole(): void {
    console.log();
  }

  formatterRole = (role: Role) => role.roleName;
  formatterPermission = (permission: Permission) => permission.permissionName;

  searchRole: OperatorFunction<string, readonly Role[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => term.length < 2 ? []
        : this.roles.filter(role => role.roleName.toLocaleLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  searchPermission: OperatorFunction<string, readonly Permission[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => term.length < 2 ? []
        : this.permissions.filter(permission => permission.permissionName.toLocaleLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
    )

  // tslint:disable-next-line: typedef
  compare() {
    this.excessPermissions.clear();
    this.excessPermissions2.clear();
    this.samePermissions.clear();
    this.samePermissions2.clear();
    this.findExcessAndSamePermission();
    this.findExcessAndSamePermission2();
  }

  findExcessAndSamePermission() {
    this.permissionSelected.forEach(
      (permission) => {
        console.log(" permision in for each = " + permission.permissionName + (this.permissionSelected2.has(permission)));
        if (this.hasPermission(this.permissionSelected2, permission)) {
          this.samePermissions.add(permission);
        } else {
          this.excessPermissions.add(permission);
        }
      }
    );
  }

  hasPermission(permissions: Set<Permission>, check: Permission) {
    let flag = false;
    permissions.forEach(
      (permission) => {
         if (check.permissionName === permission.permissionName) {
            flag = true;
            return;
         }
      }
    );
    return flag;
  }

  findExcessAndSamePermission2() {
    this.permissionSelected2.forEach(
      (permission) => {
        if (this.hasPermission(this.permissionSelected, permission)) {
          this.samePermissions2.add(permission);
        } else {
          this.excessPermissions2.add(permission);
        }
      }
    );
  }

}
