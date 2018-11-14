package com.fahai.cc.service.region.mapper;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.region.entity.Region;

public interface RegionMapper {

    //查询出所有的区域
    List<Region> findAll();
    //保存区域
    void saveRegion(Region region);
    //删除区域
    void deleteRegion(String regionCode);
    //修改区域
    void updateRegion(Region region);

    Long findCount(Map<String, String> map);
    
	void saveRegionLog(Region region);
}
