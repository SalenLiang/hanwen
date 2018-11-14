package com.fahai.cc.service.role.mapper;

import org.apache.ibatis.annotations.Param;

import com.fahai.cc.service.role.entity.Role;
import com.fahai.cc.service.role.entity.UserRole;

import java.util.List;

public interface RoleMapper {

    List<Role> findAllRoleList();

    void saveUserRole(List<UserRole> userRoleList);

    void deleteUserRole(String adminUserId);

    List<UserRole> findUserRoleList(String adminUserId);

	void saveUserRoleLog(List<UserRole> userRoleList);

}
