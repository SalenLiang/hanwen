package com.fahai.cc.service.field.service;

import java.util.List;

import com.fahai.cc.service.field.entity.Field;

/**
 * 字段service
 */
public interface FieldService {

    void save(Field field);
    void delete(Integer fieldId);
    void update(Field field);

    /**
     * 通过维度ID查找字段列表
     * @param dimensionId   维度ID
     */
    List<Field> findFieldListByDimensionId(Integer dimensionId);
    
	List<com.fahai.cc.interf.mysql.entity.Field> getDimensionField(int dimensionId);

}
