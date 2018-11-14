package com.fahai.cc.service.adminUserRegion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fahai.cc.service.adminUserRegion.entity.AdminUserRegion;
import com.fahai.cc.service.region.entity.Region;

public interface AdminUserRegionMapper {

    void save(AdminUserRegion adminUserRegion);

    void updateByAdminUserId(AdminUserRegion adminUserRegion);

    void updateByRegionCode(AdminUserRegion adminUserRegion);

    void deleteByAdminUserId(Integer adminUserId);

    void deleteByRegionCode(@Param("regionCode") String regionCode, @Param("adminUserId") Integer adminUserId);

	void saveAdminUserRegionLog(AdminUserRegion adminUserRegion);
	
	List<Region> findRegionByAdminUserId(Integer adminUserId);
}
