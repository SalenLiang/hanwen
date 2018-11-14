package com.fahai.cc.service.region.service.impl;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.mapper.AdminUserMapper;
import com.fahai.cc.service.adminUserRegion.entity.AdminUserRegion;
import com.fahai.cc.service.adminUserRegion.mapper.AdminUserRegionMapper;
import com.fahai.cc.service.region.entity.Region;
import com.fahai.cc.service.region.mapper.RegionMapper;
import com.fahai.cc.service.region.service.RegionService;
import com.fahai.cc.service.util.Constant;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    private Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private AdminUserRegionMapper adminUserRegionMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;


    @Override
    public List<Region> findAll() {
        logger.info("查询所有的区域");
        return regionMapper.findAll();
    }

    @Override
    public void saveRegion(Region region,Integer adminUserId) {
        logger.info("保存区域："+region);
        if(region.getStatus() == null){
            region.setStatus(Constant.NORMAL_STATUS_CODE);
        }
        if(region.getSortord() == null){
            region.setSortord(Constant.NORMAL_STATUS_CODE);
        }

//        AdminUserRegion adminUserRegion = new AdminUserRegion();
//        adminUserRegion.setRegionCode(region.getRegionCode());
//        adminUserRegion.setAdminUserId(Integer.valueOf(adminUserId));
//
//        AdminUser adminUser = new AdminUser();
//        adminUser.setAdminUserId(Integer.parseInt(adminUserId));
//        adminUser.setRegionYN(1);

        regionMapper.saveRegion(region);
        
        //记录日志
        region.setActionUserId(adminUserId);
        region.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
        region.setCreateDate(new Date());
        regionMapper.saveRegionLog(region);
        
//        adminUserMapper.updateAdminUser(adminUser);
//        adminUserRegionMapper.save(adminUserRegion);
    }

    @Override
    public void deleteRegion(Region region, Integer adminUserId){
        logger.info("删除区域,区域代码为："+region.getRegionCode());
        region.setStatus(Constant.DELETE_STATUS_CODE);
        //假删除，修改status为删除状态
        regionMapper.updateRegion(region);
        
        //记录日志
        region.setActionUserId(adminUserId);
        region.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
        region.setCreateDate(new Date());
        regionMapper.saveRegionLog(region);
    }

    @Override
    public void updateRegion(Region region, Integer adminUserId) {

//        AdminUser adminUser = new AdminUser();
//        adminUser.setAdminUserId(Integer.parseInt(region.getAdminUserId()));
//        adminUser.setRegionYN(0);
//
//        AdminUserRegion adminUserRegion = new AdminUserRegion();
//        adminUserRegion.setRegionCode(region.getRegionCode());
//        adminUserRegion.setAdminUserId(Integer.valueOf(region.getAdminUserId()));

        regionMapper.updateRegion(region);
        //记录日志
        region.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
        region.setActionUserId(adminUserId);
        region.setCreateDate(new Date());
        regionMapper.saveRegionLog(region);
//        adminUserMapper.updateAdminUser(adminUser);
//
//        adminUserRegionMapper.updateByRegionCode(adminUserRegion);
    }

    @Override
    public Long checkRegionCode(String regionCode) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("regionCode",regionCode);
        Long count = regionMapper.findCount(map);
        return count;
    }
}
