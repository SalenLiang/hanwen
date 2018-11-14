package com.fahai.cc.service.field.mapper;

import java.util.HashMap;
import java.util.List;

import com.fahai.cc.service.field.entity.Field;

public interface FieldMapper {

    void saveField(Field field);

    void batchSaveField(List<Field> field);

    void updateField(Field field);

    List<Field> findFieldList(HashMap<String, Object> paramsMap);

    List<Field> findByDimensionId(Integer dimensionId);

    void batchUpdateField(List<Field> fieldList);

    void batchSaveOrUpdateField(List<Field> filedList);

	void deleteFieldBydimensionId(Integer dimensionId);

	List<com.fahai.cc.interf.mysql.entity.Field> getDimensionField(int dimensionId);

	void deleteField(Integer fieldId);

	void batchSaveFieldLog(List<Field> fieldList);

}
