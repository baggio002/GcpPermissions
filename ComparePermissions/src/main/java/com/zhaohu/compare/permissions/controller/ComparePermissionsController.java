package com.zhaohu.compare.permissions.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zhaohu.compare.permissions.entity.Permission;
import com.zhaohu.compare.permissions.entity.Role;
import com.zhaohu.compare.permissions.repostory.PermissionRepository;
import com.zhaohu.compare.permissions.repostory.RoleRepository;
import com.zhaohu.compare.permissions.view.PermissionView;
import com.zhaohu.compare.permissions.view.RoleView;

// @CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ComparePermissionsController {
	private static final Logger log = LoggerFactory.getLogger(ComparePermissionsController.class);

	private RoleRepository roleRepository;
	private PermissionRepository permissionRepository;

	ComparePermissionsController(RoleRepository roleRepository, PermissionRepository permissionReopsitory) {
		this.permissionRepository = permissionReopsitory;
		this.roleRepository = roleRepository;
	}

	@GetMapping("/roles")
	public List<RoleView> roles() {
		System.out.println("=======begin=========");
		log.info("=======begin=========");
		List<RoleView> views = new ArrayList<RoleView>();
		roleRepository.findAll().forEach(role -> {
			views.add(convertToRoleView(role));
		});
                log.info("roles size = " + views.size());
		System.out.println("roles size = " + views.size());		
		return views;
	}
	
	private RoleView convertToRoleView(Role role) {
		RoleView view = new RoleView();
		
		view.setId(role.getId());
		view.setRoleName(role.getRoleName());
		view.setPermissionSize(role.getPermissions().size());
		
		return view;
		
	}

	@GetMapping("/permissions")
	public List<PermissionView> permissions() {
		List<PermissionView> views = new ArrayList<PermissionView>();
		permissionRepository.findAll().forEach(permission -> {		
			views.add(convertToPermissionView(permission));
		});
		return views;
	}
	
	private PermissionView convertToPermissionView(Permission permission) {
		PermissionView view = new PermissionView();
		view.setId(permission.getId());
		view.setPermissionName(permission.getPermissionName());
		view.setRoleSize(permission.getRoles().size());
		return view;
	}

	@GetMapping("/roles/{id}")
	public Role role(@RequestBody Role role, @PathVariable Integer id) {
		try {
			return roleRepository.findById(id).orElseThrow(() -> new ComparePermissionException());
		} catch (ComparePermissionException e) {
			// TODO Auto-generated catch block
			log.error("no id " + id);
		}
		return role;
	}
	
	@GetMapping("/role/{id}/permissions")
	public List<PermissionView> getPermissionsByRole(@PathVariable Integer id) {
		List<PermissionView> views = new ArrayList<PermissionView>();
		try {
			Role role = roleRepository.findById(id).orElseThrow(() -> new ComparePermissionException());
			role.getPermissions().forEach(permission -> views.add(convertToPermissionView(permission)));
		} catch (ComparePermissionException e) {
			// TODO Auto-generated catch block
			log.error("no id " + id);
		}
		return views;
	}

	@GetMapping("/permission/{id}")
	public Permission permission(@RequestBody Permission permission, @PathVariable Integer id) {
		try {
			return permissionRepository.findById(id).orElseThrow(() -> new ComparePermissionException());
		} catch (ComparePermissionException e) {
			// TODO Auto-generated catch block
			log.error("no id " + id);
		}
		return permission;
	}

}
