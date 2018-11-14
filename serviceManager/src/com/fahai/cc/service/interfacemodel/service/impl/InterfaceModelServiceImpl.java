package com.fahai.cc.service.interfacemodel.service.impl;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.interfacemodel.entity.InterfaceModel;
import com.fahai.cc.service.interfacemodel.mapper.InterfaceModelMapper;
import com.fahai.cc.service.interfacemodel.service.InterfaceModelService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.JsonUtil;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
@Service
public class InterfaceModelServiceImpl implements InterfaceModelService {
	
	@Autowired
	private InterfaceModelMapper interfaceModelMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public Page<InterfaceModel> fingByPageInterfaceModel(Map<String, Object> paramsMap) {
		
		long totalCount = interfaceModelMapper.getTotalCount(paramsMap);
		
		int pageSize = (int) paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount == 0) {
			return new Page(1, 0, pageSize, null);
		}
		
		int currentPage = (int)paramsMap.get(Constant.PAGE);
		int start = (currentPage - 1)*pageSize;
		paramsMap.put(Constant.PAGE_START, start);
		
		List<InterfaceModel> interfaceModels = interfaceModelMapper.findPageIngerfaceModel(paramsMap);
		
		Page<InterfaceModel> page = new Page<>();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalCount((int)totalCount);
		page.setList(interfaceModels);
		
		
		return page;
	}
	
	public void saveInterfaceModel(String interfaceModel, String addDomainDimension, Integer adminUserId) {
		
		//保存模型的基本信息
		InterfaceModel model = JsonUtil.jsonToBean(interfaceModel, InterfaceModel.class);
		model.setLastModifyDate(new Date());
		interfaceModelMapper.saveInterfaceModel(model);
		//记录日志
		model.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
		model.setActionUserId(adminUserId);
		interfaceModelMapper.saveInterfaceModelLog(model);
			
		Type type =  new TypeToken<List<Dimension>>() {}.getType();	
		Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
	            @Override
	            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
	                if (src == src.longValue())
	                    return new JsonPrimitive(src.longValue());
	                return new JsonPrimitive(src);
	            }
	        }).create();
	    List<Dimension> dimensionList= gson.fromJson(addDomainDimension, type);
		if (dimensionList != null && dimensionList.size() > 0) {
			Map<Object, Object> newMap = Maps.newHashMap();
			//保存客户的领域 维度 字段 选项
			for (Dimension dimension : dimensionList) {
				//保存客户领域
				newMap.clear();
				newMap.put("modelId", model.getModelId());
				newMap.put("lastModifyDate",new Date());
				newMap.put("domainId", dimension.getDomainId());
				//查询interfaceModelDomain表查看是否存在该条数据，不存在插入
				if (interfaceModelMapper.getCount(newMap)<1) {
					interfaceModelMapper.saveInterfaceModelDomain(newMap);
				}
				newMap.put("dimensionId", dimension.getDimensionId());
				newMap.put("dimensionCode", dimension.getDimensionCode());
				newMap.put("targetCluster", dimension.getTargetCluster());
				newMap.put("targetDimensionCode", dimension.getTargetDimensionCode());
				
				interfaceModelMapper.saveInterfaceModelDimension(newMap);
				//记录日志
				newMap.put("actionUserId", adminUserId);
				newMap.put("logId", model.getLogId());
				newMap.put("actionType", Constant.ACTIONTYPE_LOG_SAVE);
				interfaceModelMapper.saveInterfaceModelDimensionLog(newMap);
				
			}
		}
		
	}
	@Override
	public void deleteInterfaceModel(InterfaceModel interfaceModel) {
		
		//改变模型的状态
		interfaceModel.setStatus(1);
		interfaceModel.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
		interfaceModelMapper.updateInterfaceModel(interfaceModel);
		interfaceModel.setLastModifyDate(new Date());
		interfaceModelMapper.saveInterfaceModelLog(interfaceModel);
	}
	@Override
	public List<Domain> getModelDomainDimension(Integer modelId) {
		List<Domain> domainList = interfaceModelMapper.getModelDomainDimension(modelId);
		return domainList;
	}
	public InterfaceModel  saveEditInterfaceModel(String interfaceModel, String editDomainDimension,Integer adminUserId) {
		//保存模型的基本信息
		InterfaceModel model = JsonUtil.jsonToBean(interfaceModel, InterfaceModel.class);
		model.setLastModifyDate(new Date());
		interfaceModelMapper.updateInterfaceModel(model);
		Integer modelId = model.getModelId();
		//记录日志
		model.setActionUserId(adminUserId);
		model.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
		interfaceModelMapper.saveInterfaceModelLog(model);
		
		
		//将模型的领域和维度信息删除
		
		interfaceModelMapper.deleteDomain(modelId);
		interfaceModelMapper.deleteDimension(modelId);
			
		
		Type type =  new TypeToken<List<Dimension>>() {}.getType();	
		Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
	            @Override
	            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
	                if (src == src.longValue())
	                    return new JsonPrimitive(src.longValue());
	                return new JsonPrimitive(src);
	            }
	        }).create();
	    List<Dimension> dimensionList= gson.fromJson(editDomainDimension, type);
		if (dimensionList != null && dimensionList.size() > 0) {
			Map<Object, Object> newMap = Maps.newHashMap();
			//保存客户的领域 维度 字段 选项
			for (Dimension dimension : dimensionList) {
				//保存客户领域
				newMap.clear();
				newMap.put("modelId", model.getModelId());
				newMap.put("lastModifyDate",new Date());
				newMap.put("domainId", dimension.getDomainId());
				//查询interfaceModelDomain表查看是否存在该条数据，不存在插入
				if (interfaceModelMapper.getCount(newMap)<1) {
					interfaceModelMapper.saveInterfaceModelDomain(newMap);
				}
				newMap.put("dimensionId", dimension.getDimensionId());
				newMap.put("dimensionCode", dimension.getDimensionCode());
				newMap.put("targetCluster", dimension.getTargetCluster());
				newMap.put("targetDimensionCode", dimension.getTargetDimensionCode());
				
				interfaceModelMapper.saveInterfaceModelDimension(newMap);
				//记录日志
				newMap.put("actionType", Constant.ACTIONTYPE_LOG_UPDATE);
				newMap.put("logId", model.getLogId());
				newMap.put("actionUserId", adminUserId);
				interfaceModelMapper.saveInterfaceModelDimensionLog(newMap);
			}
		}
		
		return model;
	}
	@Override
	public List<InterfaceModel> findAll() {
		return interfaceModelMapper.findAll();
	}

	@Override
	public long checkModel(InterfaceModel interfaceModel, String checkType) throws Exception {
		Map<String, Object> newMap = Maps.newHashMap();
		if ("modelName".equals(checkType)) {
			newMap.put("modelName", interfaceModel.getModelName());
		}else{
			throw new Exception("不存在需要校验的模型信息");
		}
		
		newMap.put("modelId", interfaceModel.getModelId());
		return interfaceModelMapper.findModelByParam(newMap);
	}

	@Override
	public long domainYN(Integer domainId) {
		
		return interfaceModelMapper.domainYN(domainId);
	}

	@Override
	public long dimensionYN(Integer dimensionId) {
		
		return interfaceModelMapper.dimensionYN(dimensionId);
	}


}
