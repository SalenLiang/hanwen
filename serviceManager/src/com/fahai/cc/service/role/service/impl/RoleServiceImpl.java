package com.fahai.cc.service.role.service.impl;

import com.fahai.cc.service.role.entity.Role;
import com.fahai.cc.service.role.mapper.RoleMapper;
import com.fahai.cc.service.role.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAllRoleList() {
        return roleMapper.findAllRoleList();
    }
}
