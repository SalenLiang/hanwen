package com.fahai.cc.service.settingTemplet.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.settingTemplet.entity.SettingTemplet;
import com.fahai.cc.service.util.Page;

public interface SettingTempletService {

	Long checkTemplet(SettingTemplet settingTemplet, String checkType) throws Exception;
	
	void saveSettingTemplet(String settingTempletStr, String addDomainDimensionList, Integer adminUserId);

	Page<SettingTemplet> findByPage(Map<String, Object> paramsMap);

	List<Domain> getSettingTempletDetail(Integer templetId);

	void editSaveSettingTemplet(String settingTempletStr, String addDomainDimensionList, Integer adminUserId);

	void deleteSettingTempletById(SettingTemplet settingTemplet);

	List<SettingTemplet> findAllSettingTemplet();

}
