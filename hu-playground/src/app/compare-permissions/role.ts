import { Permission } from './permission';

export interface Role {
  id: number;
  roleName: string;
  permissionsSize: number;
}
