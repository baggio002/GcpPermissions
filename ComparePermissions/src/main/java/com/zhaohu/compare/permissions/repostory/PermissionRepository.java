package com.zhaohu.compare.permissions.repostory;

import org.springframework.data.repository.CrudRepository;

import com.zhaohu.compare.permissions.entity.Permission;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {

}
