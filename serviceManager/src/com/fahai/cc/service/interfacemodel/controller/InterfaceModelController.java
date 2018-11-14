package com.fahai.cc.service.interfacemodel.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.interf.mysql.entity.CustomerInformation;
import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.customer.service.CustomerService;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.interfacemodel.entity.InterfaceModel;
import com.fahai.cc.service.interfacemodel.service.InterfaceModelService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/interfaceModel")
public class InterfaceModelController {
	private Logger logger = LoggerFactory.getLogger(InterfaceModelController.class);
	
	@Autowired
	private InterfaceModelService interfaceModelService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private CustomeSession customeSession;
	
	@RequestMapping("/find")
	public Page<InterfaceModel> findByPage(HttpServletRequest request){
		logger.info("查询模型列表");
		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));
		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));
		
		String modelName = request.getParameter("modelName");
		
		String status = request.getParameter("status");
		Map<String, Object> paramsMap = Maps.newHashMap();
		paramsMap.put(Constant.PAGE_SIZE, pageSize);
		paramsMap.put(Constant.PAGE, currentPage);
		if (StringUtils.isNotBlank(status)) {
			paramsMap.put("status", status);
		}
		if (StringUtils.isNotBlank(modelName)) {
			paramsMap.put("modelName", modelName);
		}
		
		return interfaceModelService.fingByPageInterfaceModel(paramsMap);
	}
	@RequestMapping("/save")
	public Map<String, Object> saveInterfaceModel(String interfaceModel,String addDomainDimension,HttpServletRequest request){
		logger.info("新增模型");
		Map<String, Object> resultMap = Maps.newHashMap();
        try{
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
            interfaceModelService.saveInterfaceModel(interfaceModel,addDomainDimension,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"保存模型失败");
            logger.error("InterfaceModelController.class [saveInterfaceModel()] :error"+e);
        }
        
        return resultMap;
	}
	@RequestMapping("/update")
	public Map<String, Object> saveEditInterfaceModel(String interfaceModel,String editDomainDimension,HttpServletRequest request){
		logger.info("更新模型");
		Map<String, Object> resultMap = Maps.newHashMap();
		try{
//			AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
			AdminUser user = customeSession.getUser(request);
			InterfaceModel model = interfaceModelService.saveEditInterfaceModel(interfaceModel,editDomainDimension,user.getAdminUserId());
			//更新模型的时候，将客户中使用到该模型的客户的信息进行更新
			List<CustomerInformation> customerList = customerService.getCustomerInformationListByModelId(model.getModelId());
			for (CustomerInformation customerInformation : customerList) {
				List<CustomerInformation> customerInformationList = customerService.getCustomerinfor(Integer.parseInt(customerInformation.getCustomerId()));
				redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMERINFORMATION_KEY+customerInformation.getAuthCode(), customerInformationList);
			}
			resultMap.put(Constant.ERROR_CODE,"0");
		}catch (Exception e){
			resultMap.put(Constant.ERROR_CODE,"1");
			resultMap.put(Constant.ERROR_MSG,"保存模型失败");
			logger.error("InterfaceModelController.class [saveEditInterfaceModel()] :error"+e);
		}
		
		return resultMap;
	}
	@RequestMapping("/delete")
	public Map<String, Object> deleteInterfaceModel(InterfaceModel interfaceModel,HttpServletRequest request){
		Map<String, Object> resultMap = Maps.newHashMap();
		logger.info("删除模型");
        try{
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
        	interfaceModel.setActionUserId(user.getAdminUserId());
            interfaceModelService.deleteInterfaceModel(interfaceModel);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"删除模型失败");
            logger.error("InterfaceModelController.class [deleteInterfaceModel()] :error"+e);
        }
        
        return resultMap;
	}
	
	@RequestMapping("/getModelDomainDimension")
	public Map<String, Object> getModelDomainDimension(Integer modelId){
		Map<String, Object> resultMap = Maps.newHashMap();
		logger.info("查询模型"+modelId+"的信息");
        try{
            List<Domain> domainList = interfaceModelService.getModelDomainDimension(modelId); 
            resultMap.put("domainList", domainList);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"获取模型信息失败");
            logger.error("InterfaceModelController.class [getModelDomainDimension()] :error"+e);
        }
        
        return resultMap;
	}   
	@RequestMapping("/findAll")
	public Map<String, Object> findAll(){
		Map<String, Object> resultMap = Maps.newHashMap();
		logger.info("查询所有的模型");
        try{
            List<InterfaceModel> modelList = interfaceModelService.findAll(); 
            resultMap.put("modelList", modelList);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"获取模型信息失败");
            logger.error("InterfaceModelController.class [findAll()] :error"+e);
        }
        
        return resultMap;
	}
	@RequestMapping("/checkModel")
	public Map<String, Object> checkModel(InterfaceModel interfaceModel,String checkType){
		Map<String, Object> resultMap = Maps.newHashMap();
		logger.info("检测模型的唯一性");
		try{
			long count = interfaceModelService.checkModel(interfaceModel,checkType);
			if (count > 0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
			resultMap.put(Constant.ERROR_CODE,"0");
		}catch (Exception e){
			resultMap.put(Constant.ERROR_CODE,"1");
			resultMap.put(Constant.ERROR_MSG,"获取模型信息失败");
			logger.error("InterfaceModelController.class [checkModel()] :error"+e);
		}
		
		return resultMap;
	}
	@RequestMapping("/domainYN")
	public Map<String, Object> domainYN(Integer domainId){
		Map<String, Object> resultMap = Maps.newHashMap();
		logger.info("检测是否存在领域"+domainId+"对应的模型");
		try{
			long count = interfaceModelService.domainYN(domainId);
			if (count > 0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
			resultMap.put(Constant.ERROR_CODE,"0");
		}catch (Exception e){
			resultMap.put(Constant.ERROR_CODE,"1");
			resultMap.put(Constant.ERROR_MSG,"获取模型信息失败");
			logger.error("InterfaceModelController.class [domainYN()] :error"+e);
		}
		return resultMap;
	}
	@RequestMapping("/dimensionYN")
	public Map<String, Object> dimensionYN(Integer dimensionId){
		logger.info("检测是否存在维度"+dimensionId+"对应的模型");
		Map<String, Object> resultMap = Maps.newHashMap();
		try{
			long count = interfaceModelService.dimensionYN(dimensionId);
			if (count > 0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
			resultMap.put(Constant.ERROR_CODE,"0");
		}catch (Exception e){
			resultMap.put(Constant.ERROR_CODE,"1");
			resultMap.put(Constant.ERROR_MSG,"获取模型信息失败");
			logger.error("InterfaceModelController.class [dimensionYN()] :error"+e);
		}
		return resultMap;
	}
	
	
}
