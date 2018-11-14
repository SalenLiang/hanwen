package com.fahai.cc.service.adminUser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.adminUser.entity.AdminUserLog;
import com.fahai.cc.service.adminUser.mapper.AdminUserLogMapper;
import com.fahai.cc.service.adminUser.service.AdminUserLogService;

@Service
public class AdminUserLogServiceImpl implements AdminUserLogService {

    @Autowired
    private AdminUserLogMapper adminUserLogMapper;


    @Override
    public void save(AdminUserLog adminUserLog) {

    }
}
