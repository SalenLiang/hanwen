package com.fahai.cc.service.dimension.mapper;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.vo.DimensionFieldVo;

import java.util.List;
import java.util.Map;

public interface DimensionMapper {
    void saveDimension(Dimension dimension);

    long getTotalCount(Map<String, Object> paramMap);

    List<DimensionFieldVo> findPageDimension(Map<String, Object> paramMap);

    void deleteDimension(Dimension dimension);

    Dimension findDimensionById(Integer dimensionId);

    void updateDimension(Dimension dimension);
    
    Long checkDimensionById(String dimensionCode);

    List<DimensionFieldVo> findPageDimensionField(Map<String, Object> paramMap);

    DimensionFieldVo findDimensionFieldById(Integer dimensionId);

	List<com.fahai.cc.interf.mysql.entity.Dimension> getDimensionList();

	com.fahai.cc.interf.mysql.entity.Dimension getDimension(Integer dimensionId);

	void saveDimensionLog(Dimension dimension);
}
