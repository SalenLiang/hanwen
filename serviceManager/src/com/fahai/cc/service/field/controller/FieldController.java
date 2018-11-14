package com.fahai.cc.service.field.controller;

import com.fahai.cc.service.field.entity.Field;
import com.fahai.cc.service.field.service.FieldService;
import com.fahai.cc.service.util.Constant;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serviceManager/field")
public class FieldController {
    private Logger logger = LoggerFactory.getLogger(FieldController.class);

    @Autowired
    private FieldService fieldService;

    @RequestMapping("/update")
    public Map<String,Object> update(Field field){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        logger.info("更新字段");
        try {
            fieldService.update(field);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("FieldController.class [update()] :error"+e);
        }

        return resultMap;
    }
    @RequestMapping("/delete/{fieldId}")
    public Map<String,Object> delete(@PathVariable Integer fieldId){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        logger.info("删除字段");
        try {
            fieldService.delete(fieldId);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("FieldController.class [delete()] :error"+e);
        }
        return resultMap;
    }
    //url:Constant.prefixUrl+"/field/findFieldListByDimensionId/"+dimensionId,
    @RequestMapping("/findFieldListByDimensionId/{dimensionId}")
    public Map<String,Object> findFieldListByDimensionId(@PathVariable Integer dimensionId){
    	logger.info("查询维度"+dimensionId+"包含的字段");
        HashMap<String, Object> resultMap = Maps.newHashMap();
        try {
            List<Field> fieldList = fieldService.findFieldListByDimensionId(dimensionId);
            resultMap.put(Constant.ERROR_CODE,"0");
            resultMap.put(Constant.DATA_LIST,fieldList);
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("FieldController.class [findFieldListByDimensionId()] :error"+e);
        }
        return resultMap;
    }


}
