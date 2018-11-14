package com.fahai.cc.service.settingTemplet.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.settingTemplet.entity.SettingTemplet;

public interface SettingTempletMapper {

	Long findCountByParam(HashMap<String, Object> params);

	void saveSettingTemplet(SettingTemplet settingTemplet);

	void saveSettingTempletLog(SettingTemplet settingTemplet);

	void saveTempletField(Map<String, Object> newMap);

	void saveTempletFieldLog(Map<String, Object> newMap);

	long getTotalCount(Map<String, Object> paramsMap);

	List<SettingTemplet> findPageSettingTemplet(Map<String, Object> paramsMap);

	List<Domain> getSettingTempletDetail(Integer templetId);

	void updateSettingTemplet(SettingTemplet settingTemplet);

	void deleteTempletField(Integer templetId);

	List<SettingTemplet> findAllSettingTemplet();

	List<Domain> getSettingTempletDetailById(Integer customerTempletId);

}
