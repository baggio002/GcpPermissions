package com.zhaohu.compare.permissions.view;

public class PermissionView {
	private int id;
	private String permissionName;
	private int roleSize;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public int getRoleSize() {
		return roleSize;
	}
	public void setRoleSize(int roleSize) {
		this.roleSize = roleSize;
	}
}
