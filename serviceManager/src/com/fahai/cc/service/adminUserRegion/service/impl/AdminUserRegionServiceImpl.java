package com.fahai.cc.service.adminUserRegion.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.adminUserRegion.entity.AdminUserRegion;
import com.fahai.cc.service.adminUserRegion.mapper.AdminUserRegionMapper;
import com.fahai.cc.service.adminUserRegion.service.AdminUserRegionService;
import com.fahai.cc.service.region.entity.Region;

@Service
public class AdminUserRegionServiceImpl implements AdminUserRegionService {
    private Logger logger = LoggerFactory.getLogger(AdminUserRegionServiceImpl.class);

    @Autowired
    private AdminUserRegionMapper adminUserRegionMapper;

    @Override
    public void save(AdminUserRegion adminUserRegion) {
        logger.info("新增系统用户与区域中间表");
        adminUserRegionMapper.save(adminUserRegion);

    }

    @Override
    public void updateByAdminUserId(AdminUserRegion adminUserRegion) {
        logger.info("修改系统用户与区域中间表");
        adminUserRegionMapper.updateByAdminUserId(adminUserRegion);
    }


    @Override
    public void updateByRegionCode(AdminUserRegion adminUserRegion) {
        logger.info("修改系统用户与区域中间表");
        adminUserRegionMapper.updateByRegionCode(adminUserRegion);
    }
    
    @Override
	public List<Region> findRegionByAdminUserId(Integer adminUserId) {
		
		return adminUserRegionMapper.findRegionByAdminUserId(adminUserId);
	}
}
