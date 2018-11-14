package com.fahai.cc.service.region.service;


import java.util.List;

import com.fahai.cc.service.region.entity.Region;

public interface RegionService {

    /**
     * 查询出所有的区域
     * @return  区域列表
     */
    List<Region> findAll();

    /**
     * 保存区域
     * @param region  待保存的区域实体类
     */
    void saveRegion(Region region, Integer adminUserId);

    /**
     * 通过区域ID删除区域
     * @param regionCode    区域代码
     */
    void deleteRegion(Region region, Integer adminUserId);

    /**
     * 修改区域
     * @param region  待修改的区域实体类
     */
    void updateRegion(Region region, Integer adminUserId);

    /**
     * 校验区域代码唯一性
     * @param regionCode    待校验的区域代码
     */
    Long checkRegionCode(String regionCode);

	

	

	

	
}
