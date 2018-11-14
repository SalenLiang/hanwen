package com.fahai.cc.service.settingTemplet.service.impl;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.field.entity.Field;
import com.fahai.cc.service.limitword.entity.Limitword;
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.settingTemplet.entity.SettingTemplet;
import com.fahai.cc.service.settingTemplet.mapper.SettingTempletMapper;
import com.fahai.cc.service.settingTemplet.service.SettingTempletService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.JsonUtil;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
@Service
public class SettingTempletServiceImpl implements SettingTempletService {
	
	@Autowired
	private SettingTempletMapper settingTempletMapper;
	
	@Override
	public Long checkTemplet(SettingTemplet settingTemplet, String checkType) throws Exception {
		HashMap<String, Object> params = Maps.newHashMap();
		if ("templetName".equals(checkType)) {
			params.put("templetName", settingTemplet.getTempletName());
		}else{
			throw new Exception("不存在需要校验的配置模板信息");
		}
		
		params.put("templetId", settingTemplet.getTempletId());
		
		
		return settingTempletMapper.findCountByParam(params);
	}

	@Override
	public void saveSettingTemplet(String settingTempletStr, String addDomainDimensionList, Integer adminUserId) {
		
		SettingTemplet settingTemplet = JsonUtil.toBean(settingTempletStr, SettingTemplet.class);
		
		settingTemplet.setActionUserId(adminUserId);
		settingTemplet.setCreateDate(new Date());
		settingTemplet.setLastModifyDate(new Date());
		//保存基本信息
		settingTempletMapper.saveSettingTemplet(settingTemplet);
		
		Integer templetId = settingTemplet.getTempletId();
		//记录日志
		settingTemplet.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
		settingTempletMapper.saveSettingTempletLog(settingTemplet);
		
		//保存配置信息
		Map<String, Object> newMap = Maps.newHashMap();
		newMap.put("templetId", templetId);
		newMap.put("createDate", settingTemplet.getCreateDate());
		newMap.put("actionUserId", adminUserId);
		
		Type type = new TypeToken<List<Domain>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
				
        List<Domain> domainList= gson.fromJson(addDomainDimensionList, type);
		if (domainList != null && domainList.size() > 0) {
			for (Domain domain : domainList) {
				newMap.put("domainId", domain.getDomainId());
				for (Dimension dimension : domain.getDimensionList()) {
					newMap.put("dimensionId", dimension.getDimensionId());
					for (Field field : dimension.getFieldList()) {
						newMap.put("fieldId", field.getFieldId());
						newMap.put("detailShowYN", field.getDefaultDetailShowYN());
						newMap.put("listShowYN", field.getDefaultListShowYN());
						settingTempletMapper.saveTempletField(newMap);
						
						//记录日志
						newMap.put("logId", settingTemplet.getLogId());
						newMap.put("actionType", Constant.ACTIONTYPE_LOG_SAVE);
						newMap.put("actionUserId", adminUserId);
						settingTempletMapper.saveTempletFieldLog(newMap);
					}
				}
				
			}
		}
		
	}

	@Override
	public Page<SettingTemplet> findByPage(Map<String, Object> paramsMap) {
		long totalCount = settingTempletMapper.getTotalCount(paramsMap);
		int pageSize = (int) paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount < 1) {
			return new Page<SettingTemplet>(1, 0,pageSize, null);
		}
		
		int currentPage = (int) paramsMap.get(Constant.PAGE);
		
		int start = (currentPage-1)*pageSize;
		
		paramsMap.put(Constant.PAGE_START, start);
		
		List<SettingTemplet> settingTempletList = settingTempletMapper.findPageSettingTemplet(paramsMap);
		
		Page<SettingTemplet> page = new Page<>();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalCount((int)totalCount);
		page.setList(settingTempletList);
		
		return page;
	}

	@Override
	public List<Domain> getSettingTempletDetail(Integer templetId) {
		
		return settingTempletMapper.getSettingTempletDetail(templetId);
	}

	@Override
	public void editSaveSettingTemplet(String settingTempletStr, String addDomainDimensionList, Integer adminUserId) {

		SettingTemplet settingTemplet = JsonUtil.toBean(settingTempletStr, SettingTemplet.class);
		
		settingTemplet.setActionUserId(adminUserId);
		settingTemplet.setCreateDate(new Date());
		settingTemplet.setLastModifyDate(new Date());
		//更新基本信息
		settingTempletMapper.updateSettingTemplet(settingTemplet);
		
		Integer templetId = settingTemplet.getTempletId();
		//记录日志
		settingTemplet.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
		settingTempletMapper.saveSettingTempletLog(settingTemplet);
		
		//删除原来的配置信息
		settingTempletMapper.deleteTempletField(templetId);
		
		//保存配置信息
		Map<String, Object> newMap = Maps.newHashMap();
		newMap.put("templetId", templetId);
		newMap.put("createDate", settingTemplet.getCreateDate());
		newMap.put("actionUserId", adminUserId);
		
		Type type = new TypeToken<List<Domain>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
				
        List<Domain> domainList= gson.fromJson(addDomainDimensionList, type);
		if (domainList != null && domainList.size() > 0) {
			for (Domain domain : domainList) {
				newMap.put("domainId", domain.getDomainId());
				for (Dimension dimension : domain.getDimensionList()) {
					newMap.put("dimensionId", dimension.getDimensionId());
					for (Field field : dimension.getFieldList()) {
						newMap.put("fieldId", field.getFieldId());
						newMap.put("detailShowYN", field.getDefaultDetailShowYN());
						newMap.put("listShowYN", field.getDefaultListShowYN());
						settingTempletMapper.saveTempletField(newMap);
						
						//记录日志
						newMap.put("logId", settingTemplet.getLogId());
						newMap.put("actionType", Constant.ACTIONTYPE_LOG_UPDATE);
						newMap.put("actionUserId", adminUserId);
						settingTempletMapper.saveTempletFieldLog(newMap);
					}
				}
				
			}
		}
		
	}

	@Override
	public void deleteSettingTempletById(SettingTemplet settingTemplet) {
		
		settingTemplet.setStatus(1);//置为停用，假删除
		
		settingTempletMapper.updateSettingTemplet(settingTemplet);
		
	}

	@Override
	public List<SettingTemplet> findAllSettingTemplet() {
		
		return settingTempletMapper.findAllSettingTemplet();
	}

}
