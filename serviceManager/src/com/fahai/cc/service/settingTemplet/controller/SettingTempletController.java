package com.fahai.cc.service.settingTemplet.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.settingTemplet.entity.SettingTemplet;
import com.fahai.cc.service.settingTemplet.service.SettingTempletService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/settingTemplet")
public class SettingTempletController {
	
	private Logger logger = LoggerFactory.getLogger(SettingTempletController.class);
	@Autowired
	private SettingTempletService settingTempletService;
	@Autowired
	private CustomeSession customeSession;
	
	 @RequestMapping("/find")
	    public Page<SettingTemplet> findByPage(HttpServletRequest request){
	    	logger.info("查询配置模板列表");
	    	Page<SettingTemplet> page = null;
	    	try {
	    		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
	    		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
	    		String templetName = request.getParameter("templetName");
	    		String status = request.getParameter("status")==null?"0":request.getParameter("status");
	    		Map<String, Object> paramsMap = Maps.newHashMap();
	    		paramsMap.put(Constant.PAGE_SIZE,pageSize);
	    		paramsMap.put(Constant.PAGE,currentPage);
	    		paramsMap.put("status",status);
	    		if(StringUtils.isNotBlank(templetName)){
	    			paramsMap.put("templetName",templetName);
	    		}
	    		page = settingTempletService.findByPage(paramsMap);
				
			} catch (Exception e) {
				logger.error("SettingTempletController.class [find() :error]"+e.fillInStackTrace());
			}
	        return page;
	    }
	@RequestMapping(value="/save")
    public Map<String,Object> saveSettingTemplet(String settingTempletStr, String addDomainDimensionList,HttpServletRequest request) throws UnsupportedEncodingException{
        logger.info("新增配置模板");
    	Map<String, Object> resultMap = Maps.newHashMap();
        try{
        	AdminUser user = customeSession.getUser(request);
            settingTempletService.saveSettingTemplet(settingTempletStr,addDomainDimensionList,user.getAdminUserId());
            
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"保存模板失败");
            logger.error("保存客户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }
	@RequestMapping(value="/editSave")
	public Map<String,Object> editSaveSettingTemplet(String settingTempletStr, String addDomainDimensionList,HttpServletRequest request) throws UnsupportedEncodingException{
		logger.info("修改配置模板");
		Map<String, Object> resultMap = Maps.newHashMap();
		try{
			AdminUser user = customeSession.getUser(request);
			settingTempletService.editSaveSettingTemplet(settingTempletStr,addDomainDimensionList,user.getAdminUserId());
			
			resultMap.put(Constant.ERROR_CODE,"0");
		}catch (Exception e){
			resultMap.put(Constant.ERROR_CODE,"1");
			resultMap.put(Constant.ERROR_MSG,"保存模板失败");
			logger.error("修改配置模板出现异常",e.fillInStackTrace());
		}
		return resultMap;
	}
	@RequestMapping("/getSettingTempletDetail")
	public Map<String, Object> getSettingTempletDetail(Integer templetId){
		logger.info("获取模板的配置详情");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			List<Domain> templetDomain = settingTempletService.getSettingTempletDetail(templetId);
			resultMap.put("templetDomain", templetDomain);
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			logger.error("校验配置模板信息的唯一性出现异常",e.fillInStackTrace());
			resultMap.put(Constant.ERROR_CODE, "1");
		}
		return resultMap;
	}
	@RequestMapping("/editCheckTemplet")
	public Map<String, Object> editCheckTemplet(SettingTemplet settingTemplet,String checkType){
		logger.info("校验配置模板信息的唯一性");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			Long count = settingTempletService.checkTemplet(settingTemplet,checkType);
			if (count>0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			logger.error("校验配置模板信息的唯一性出现异常",e.fillInStackTrace());
			resultMap.put(Constant.ERROR_CODE, "1");
		}
		return resultMap;
	}
	@RequestMapping("/checkTemplet")
	public Map<String, Object> checkTemplet(SettingTemplet settingTemplet,String checkType){
		logger.info("校验配置模板信息的唯一性");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			Long count = settingTempletService.checkTemplet(settingTemplet,checkType);
			if (count>0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			logger.error("校验配置模板信息的唯一性出现异常",e.fillInStackTrace());
			resultMap.put(Constant.ERROR_CODE, "1");
		}
		return resultMap;
	}
	@RequestMapping("/deleteSettingTempletById")
	public Map<String, Object> deleteSettingTempletById(SettingTemplet settingTemplet){
		logger.info("删除配置模板");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			settingTempletService.deleteSettingTempletById(settingTemplet);
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			logger.error("删除配置模板出现异常",e.fillInStackTrace());
			resultMap.put(Constant.ERROR_CODE, "1");
		}
		return resultMap;
	}
	@RequestMapping("/findAllSettingTemplet")
	public Map<String, Object> findAllSettingTemplet(){
		logger.info("获取可用配置模板集合");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			List<SettingTemplet> availableTempletList = settingTempletService.findAllSettingTemplet();
			resultMap.put("availableTempletList", availableTempletList);
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			logger.error("获取可用配置模板集合出现异常",e.fillInStackTrace());
			resultMap.put(Constant.ERROR_CODE, "1");
		}
		return resultMap;
	}
}
