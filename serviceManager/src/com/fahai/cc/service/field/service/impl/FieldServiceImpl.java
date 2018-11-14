package com.fahai.cc.service.field.service.impl;

import com.fahai.cc.service.field.entity.Field;
import com.fahai.cc.service.field.mapper.FieldMapper;
import com.fahai.cc.service.field.service.FieldService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class FieldServiceImpl implements FieldService{
    private Logger logger = LoggerFactory.getLogger(FieldServiceImpl.class);

    @Autowired
    private FieldMapper fieldMapper;

    @Override
    public void save(Field field) {
        field.setLastModifyDate(new Date());
        if(field.getDescription() == null){
            field.setDescription("");
        }
        field.setStatus(0);
        fieldMapper.saveField(field);
        logger.info("保存字段");
    }

    @Override
    public void delete(Integer fieldId) {
        Field field = new Field();
        field.setLastModifyDate(new Date());
        field.setStatus(1);
        field.setFieldId(fieldId);
        fieldMapper.updateField(field);
//    	fieldMapper.deleteField(fieldId);
        logger.info("删除字段");
    }

    @Override
    public void update(Field field) {
        field.setLastModifyDate(new Date());
        fieldMapper.updateField(field);
        logger.info("修改字段");
    }

    @Override
    public List<Field> findFieldListByDimensionId(Integer dimensionId) {
        logger.info("根据维度ID查找字段列表");
        HashMap<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put("dimensionId",dimensionId);
        paramsMap.put("status",0);
        List<Field> fields = fieldMapper.findFieldList(paramsMap);
        return fields;
    }

	@Override
	public List<com.fahai.cc.interf.mysql.entity.Field> getDimensionField(int dimensionId) {
		
		return fieldMapper.getDimensionField(dimensionId);
	}
}
