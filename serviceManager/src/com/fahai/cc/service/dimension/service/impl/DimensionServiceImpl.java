package com.fahai.cc.service.dimension.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.dimension.mapper.DimensionMapper;
import com.fahai.cc.service.dimension.service.DimensionService;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.domain.service.DomainService;
import com.fahai.cc.service.field.entity.Field;
import com.fahai.cc.service.field.mapper.FieldMapper;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.JsonUtil;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.vo.DimensionFieldVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class DimensionServiceImpl implements DimensionService {

    @Autowired
    private DimensionMapper dimensionMapper;
    @Autowired
    private DomainService domainService;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Dimension save(String dimensionStr, String fieldListStr,Integer adminUserId) {
        Date now = new Date();
        Dimension dimension = JsonUtil.jsonToBean(dimensionStr, Dimension.class);
        dimension.setLastModifyDate(now);
        //如果status未选择  设置为0 启用
        if (dimension.getStatus() == null) {
			dimension.setStatus(0);
		}
        dimensionMapper.saveDimension(dimension);
        //记录日志
        dimension.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
        dimension.setActionUserId(adminUserId);
        dimensionMapper.saveDimensionLog(dimension);

        List<Field> fieldList = Lists.newArrayList();
        List<Map<String,String>> fieldMapList = JsonUtil.fromJsonToListMap(fieldListStr);
        if (fieldMapList != null && fieldMapList.size()>0) {
        	for (Map<String, String> map : fieldMapList) {
        		Field field = new Field();
        		field.setDimensionId(dimension.getDimensionId());
        		field.setFieldCode(map.get("fieldCode"));
        		field.setFieldName(map.get("fieldName"));
        		field.setDescription(map.get("description"));
        		field.setStatus(Integer.valueOf(map.get("status")));
        		field.setFieldType(map.get("fieldType"));
        		//列表页是否默认选中
        		if (map.get("defaultListShowYN") == null) {
        			field.setDefaultListShowYN(0);;//不选中
        		}else{
        			field.setDefaultListShowYN(Integer.valueOf(map.get("defaultListShowYN")));
        		}
        		//详细页是否默认选中
        		if (map.get("defaultDetailShowYN") == null) {
        			field.setDefaultDetailShowYN(0);;//不选中
        		}else{
        			field.setDefaultDetailShowYN(Integer.valueOf(map.get("defaultDetailShowYN")));
        		}
        		//是否支持搜索
        		if (map.get("searchYN") == null) {
        			field.setSearchYN(0);;//不选中
        		}else{
        			field.setSearchYN(Integer.valueOf(map.get("searchYN")));
        		}
        		field.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
        		field.setActionUserId(adminUserId);
        		field.setLogId(dimension.getLogId());
        		field.setLastModifyDate(now);
        		fieldList.add(field);
        	}
        	if(fieldList.size()>0){
        		fieldMapper.batchSaveField(fieldList);
        	}
        	//记录日志
        	
        	fieldMapper.batchSaveFieldLog(fieldList);
		}
        
        return dimension;
    }
/*
    @Override
    public Page<Dimension> findByPage(Map<String, Object> paramMap) { 
        long totalCount = dimensionMapper.getTotalCount(paramMap);
        int pageSize = (int) paramMap.get(Constant.PAGE_SIZE);
        if(totalCount == 0l){
            return new Page<>(1,0,pageSize,null);
        }
        int currentPage = (int) paramMap.get(Constant.PAGE);
        int start = (currentPage-1)*pageSize;
        paramMap.put(Constant.PAGE_START,start);
        List<Dimension> dimensionList = dimensionMapper.findPageDimension(paramMap);
        Page<Dimension> page = new Page<>();
        page.setTotalCount((int)totalCount);
        page.setList(dimensionList);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);

//        List<DimensionFieldVo> dimensionFieldVos = dimensionMapper.findPageDimensionField(paramMap);
        return page;
    }*/

    @Override
    public Page<DimensionFieldVo> findByPageDimensionField(Map<String, Object> paramMap) {
        long totalCount = dimensionMapper.getTotalCount(paramMap);
        int pageSize = (int) paramMap.get(Constant.PAGE_SIZE);
        if(totalCount == 0){
            return new Page<>(1,0,pageSize,null);
        }
        int currentPage = (int) paramMap.get(Constant.PAGE);
        int start = (currentPage-1)*pageSize;
        paramMap.put(Constant.PAGE_START,start);
//        List<DimensionFieldVo> dimensionFieldVos = dimensionMapper.findPageDimensionField(paramMap);
        //先查询维度
        List<DimensionFieldVo> dimensionFieldVos = dimensionMapper.findPageDimension(paramMap);
        //查询各个维度的字段
        for (DimensionFieldVo dimensionFieldVo : dimensionFieldVos) {
			Integer dimensionId = dimensionFieldVo.getDimension().getDimensionId();
			List<Field> fieldList = fieldMapper.findByDimensionId(dimensionId);
			dimensionFieldVo.setFields(fieldList);
		}
        Page<DimensionFieldVo> page = new Page<>();
        page.setTotalCount((int)totalCount);
        page.setList(dimensionFieldVos);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public void delete(Dimension dimension) {
        dimension.setStatus(1);
        dimension.setLastModifyDate(new Date());
        dimension.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
        dimensionMapper.deleteDimension(dimension);
        //记录日志
        dimensionMapper.saveDimensionLog(dimension);
    }

    @Override
    public Map<String, Object> findDimensionById(Integer dimensionId) {
        HashMap<String, Object> map = Maps.newHashMap();
//        Dimension dimension = dimensionMapper.findDimensionById(dimensionId);
//        List<Field> fields = fieldMapper.findByDimensionId(dimensionId);
        List<Domain> domains = domainService.getAllValidDomain();

        DimensionFieldVo dimensionFieldVo = dimensionMapper.findDimensionFieldById(dimensionId);
        if (dimensionFieldVo != null) {
        	map.put("dimension",dimensionFieldVo.getDimension());
        	map.put("fieldList",dimensionFieldVo.getFields());
		}else{
			Dimension findDimensionById = dimensionMapper.findDimensionById(dimensionId);
			map.put("dimension",findDimensionById);
		}
        map.put("domainList",domains);
        return map;
    }

    @Override
    public Dimension update(String dimensionStr, String fieldListStr,Integer adminUserId) {
        Date now = new Date();
        Dimension dimension = JsonUtil.toBean(dimensionStr,  Dimension.class);
        dimension.setLastModifyDate(now);
        dimensionMapper.updateDimension(dimension);
        //记录日志
        dimension.setActionUserId(adminUserId);
        dimension.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
        dimensionMapper.saveDimensionLog(dimension);
        
        List<Field> updateFiledList = Lists.newArrayList();
        List<Field> newFiledList = Lists.newArrayList();
        List<Map<String,String>> fieldMapList = JsonUtil.fromJsonToListMap(fieldListStr);
        if(fieldMapList != null && fieldMapList.size()>0){
        	fieldMapList.forEach(map ->{
        			Field field = new Field();
            		field.setDimensionId(dimension.getDimensionId());
            		field.setFieldCode(map.get("fieldCode"));
            		field.setFieldName(map.get("fieldName"));
            		field.setDescription(map.get("description"));
            		field.setFieldType(map.get("fieldType"));
            		field.setStatus(Integer.valueOf(map.get("status")));
            		//列表页是否默认选中
            		if (map.get("defaultListShowYN") == null) {
            			field.setDefaultListShowYN(0);;//不选中
            		}else{
            			field.setDefaultListShowYN(Integer.valueOf(map.get("defaultListShowYN")));
            		}
            		//详细页是否默认选中
            		if (map.get("defaultDetailShowYN") == null) {
            			field.setDefaultDetailShowYN(0);;//不选中
            		}else{
            			field.setDefaultDetailShowYN(Integer.valueOf(map.get("defaultDetailShowYN")));
            		}
            		//是否支持搜索
            		if (map.get("searchYN") == null) {
            			field.setSearchYN(0);;//不选中
            		}else{
            			field.setSearchYN(Integer.valueOf(map.get("searchYN")));
            		}
            		field.setActionUserId(adminUserId);
            		field.setLogId(dimension.getLogId());
            		field.setLastModifyDate(now);
            		if(map.get("fieldId")!=null){//更新
            			field.setFieldId(Integer.parseInt(map.get("fieldId")));
            			field.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
            			updateFiledList.add(field);
            		}else{//新增
            			field.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
            			newFiledList.add(field);
            		}
        	});
        	if(updateFiledList.size()>0){
        		fieldMapper.batchSaveOrUpdateField(updateFiledList);
        		//记录日志
        		fieldMapper.batchSaveFieldLog(updateFiledList);
        	}
        	if (newFiledList.size()>0) {
				fieldMapper.batchSaveField(newFiledList);
				//记录日志
				fieldMapper.batchSaveFieldLog(newFiledList);
			}
        	
        }
        
        return dimension;
    }

	@Override
	public Long checkDimensionById(String dimensionCode) {
		 Long count  = dimensionMapper.checkDimensionById(dimensionCode);
        return count;
	}

	@Override
	public List<com.fahai.cc.interf.mysql.entity.Dimension> getDimensionList() {
		
		return dimensionMapper.getDimensionList();
	}

	@Override
	public com.fahai.cc.interf.mysql.entity.Dimension getDimension(Integer dimensionId) {
		
		return dimensionMapper.getDimension(dimensionId);
	}

    

}
