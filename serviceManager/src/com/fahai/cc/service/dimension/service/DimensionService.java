package com.fahai.cc.service.dimension.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.vo.DimensionFieldVo;

public interface DimensionService {

//    Page<Dimension> findByPage(Map<String, Object> paramsMap);

    Page<DimensionFieldVo> findByPageDimensionField(Map<String, Object> paramsMap);

    Map<String,Object> findDimensionById(Integer dimensionId);

    //Map<String,Object> checkDimensionById(String dimensionCode);
    Long checkDimensionById(String dimensionCode);

	List<com.fahai.cc.interf.mysql.entity.Dimension> getDimensionList();

	com.fahai.cc.interf.mysql.entity.Dimension getDimension(Integer dimensionId);

	Dimension save(String dimensionStr, String fieldListStr, Integer adminUserId);

	Dimension update(String dimensionStr, String fieldListStr, Integer adminUserId);

	void delete(Dimension dimension);
}
