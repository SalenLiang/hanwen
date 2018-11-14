package com.fahai.cc.service.adminUserRegion.service;

import java.util.List;

import com.fahai.cc.service.adminUserRegion.entity.AdminUserRegion;
import com.fahai.cc.service.region.entity.Region;

public interface AdminUserRegionService {

    /**
     * 保存方法
     * @param adminUserRegion   实体类
     */
    void save(AdminUserRegion adminUserRegion);

    /**
     * 修改方法
     * @param adminUserRegion   实体类
     */
    void updateByAdminUserId(AdminUserRegion adminUserRegion);

    /**
     * 修改方法
     * @param adminUserRegion   实体类
     */
    void updateByRegionCode(AdminUserRegion adminUserRegion);

    List<Region> findRegionByAdminUserId(Integer adminUserId);
}
