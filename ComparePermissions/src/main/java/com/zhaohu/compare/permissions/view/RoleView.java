package com.zhaohu.compare.permissions.view;

public class RoleView {
	private int id;
	private String roleName;
	private int permissionSize;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getPermissionSize() {
		return permissionSize;
	}
	public void setPermissionSize(int permissionSize) {
		this.permissionSize = permissionSize;
	}
}
