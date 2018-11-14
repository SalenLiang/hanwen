package com.fahai.cc.service.adminUser.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.adminUser.entity.AdminUserLoginLog;
import com.fahai.cc.service.adminUser.mapper.AdminUserLoginLogMapper;
import com.fahai.cc.service.adminUser.service.AdminUserLoginLogService;

@Service
public class AdminUserLoginLogServiceImpl implements AdminUserLoginLogService {

    private Logger logger = LoggerFactory.getLogger(AdminUserLoginLogServiceImpl.class);

    @Autowired
    private AdminUserLoginLogMapper adminUserLoginLogMapper;

    @Override
    @Async
    public void saveLog(AdminUserLoginLog loginLog) {
        logger.info("保存系统用户登陆日志");
        adminUserLoginLogMapper.saveLog(loginLog);
    }
}
