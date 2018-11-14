package com.fahai.cc.service.customer.controller;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.interf.mysql.entity.CustomerInformation;
import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.service.AdminUserService;
import com.fahai.cc.service.adminUserRegion.service.AdminUserRegionService;
import com.fahai.cc.service.customer.entity.Customer;
import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.customer.service.CustomerService;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.region.entity.Region;
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.util.PoiUtil;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/customer")
public class CustomerController {

	private Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private AdminUserRegionService adminUserRegionService;
	
	@Autowired
	private CustomeSession customeSession;
	
	
	@RequestMapping(value = "/find",method = RequestMethod.POST)
    public Page<Customer> findUserByPage(HttpServletRequest request){
		logger.info("查询客户列表");
		Page<Customer> pageCustomer = null;
		try {
			System.out.println(request.getParameter(Constant.PAGE));
	        int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
	        int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
	        String companyName = request.getParameter("companyName");
	        String authCode = request.getParameter("authCode");
	        Map<String, Object> paramsMap = Maps.newHashMap();
	        paramsMap.put(Constant.PAGE_SIZE,pageSize);
	        paramsMap.put(Constant.PAGE,currentPage);
	        paramsMap.put("status", request.getParameter("status"));
//	        AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION); 
	        AdminUser user = customeSession.getUser(request);
	        if(StringUtils.isNotBlank(companyName)){
	            paramsMap.put("companyName",companyName);
	        }
	        if(StringUtils.isNotBlank(authCode)){
	        	paramsMap.put("authCode", authCode);
	        }
	        
	        
	        List<String> roleIdList = adminUserService.findRoleIdByAdminUserId(user.getAdminUserId());
	        if (roleIdList.contains("6")) {
	        	pageCustomer = customerService.findPageCustomer(paramsMap);
				return pageCustomer;
			}
	        paramsMap.put("ownerId", user.getAdminUserId());
	        if (roleIdList.contains("5")) {
	        	List<Region> userRegionList = adminUserRegionService.findRegionByAdminUserId(user.getAdminUserId());
				paramsMap.put("regionList", userRegionList);
			}
	        
	        pageCustomer = customerService.findPageCustomer(paramsMap);
	        
		} catch (Exception e) {
			logger.error("CustomerController.class [findUserByPage()] :error"+e);
		}
		return pageCustomer;
    }
	
	
    @RequestMapping(value="/save")
    public Map<String,Object> saveCustomer( String customer, String addCustomerServiceInterfaces, String addCustomerServiceDomains,HttpServletRequest request) throws UnsupportedEncodingException{
        logger.info("新增客户");
    	Map<String, Object> resultMap = Maps.newHashMap();
        try{
//	        AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
            Customer customerBean = customerService.saveCustomer(customer,addCustomerServiceInterfaces,addCustomerServiceDomains,user.getAdminUserId());
            
          //获取该新增客户的信息放到redis缓存中
    		CustomerInformation customerInformation = customerService.getCustomer(customerBean.getCustomerId());
    		redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMER_KEY+customerBean.getAuthCode(), customerInformation);
    		
    		List<CustomerInformation> customerinforList = customerService.getCustomerinfor(customerBean.getCustomerId());
    		if (customerinforList != null && customerinforList.size()>0) {
    			redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMERINFORMATION_KEY+customerBean.getAuthCode(),customerinforList);
    		}
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"保存客户失败");
            logger.error("保存客户出现异常",e.fillInStackTrace());
        }
        
        
        return resultMap;
    }

    @RequestMapping("/update")
    public Map<String,Object> updateCustomer(String customer,String addCustomerServiceInterfaces,String addCustomerServiceDomains,HttpServletRequest request){
    	logger.info("更新客户");
    	Map<String, Object> resultMap = Maps.newHashMap();
        try{
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
        	Customer customerBean = customerService.updateCustomer(customer,addCustomerServiceInterfaces,addCustomerServiceDomains,user.getAdminUserId());
        	//更新redis缓存
    		CustomerInformation customerInformation = customerService.getCustomer(customerBean.getCustomerId());
    		if (customerInformation != null) {
    			redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMER_KEY+customerBean.getAuthCode(), customerInformation);
    			
    			List<CustomerInformation> customerinforList = customerService.getCustomerinfor(customerBean.getCustomerId());
    			if (customerinforList != null && customerinforList.size()>0) {
    				redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMERINFORMATION_KEY+customerBean.getAuthCode(),customerinforList);
    			}
			}
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"修改客户失败");
            logger.error("修改客户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }

    @RequestMapping("/deleteCustomerById")
    public Map<String,Object> deleteCustomerById(String customerStr,HttpServletRequest request){
    	logger.info("删除客户");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
        	Customer customer = customerService.deleteCustomerById(customerStr,user.getAdminUserId());
        	
        	//删除redis缓存中的客户的信息
    		redisTemplate.opsForValue().getOperations().delete(Constant.REDIS_CUSTOMER_KEY+customer.getAuthCode());
    		redisTemplate.opsForValue().getOperations().delete(Constant.REDIS_CUSTOMERINFORMATION_KEY+customer.getAuthCode());
        	
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"删除客户失败");
            logger.error("删除客户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }
    @RequestMapping("/activationCustomer")
    public Map<String,Object> activationCustomer(String customerStr,HttpServletRequest request){
    	logger.info("激活客户");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
//    		AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
    		AdminUser user = customeSession.getUser(request);
    		Customer customer = customerService.updateCustomerStatus(customerStr,user.getAdminUserId());
    		//将客户的信息放到redis缓存中
    		CustomerInformation customerInformation = customerService.getCustomer(customer.getCustomerId());
    		redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMER_KEY+customer.getAuthCode(), customerInformation);
    		
    		List<CustomerInformation> customerinforList = customerService.getCustomerinfor(customer.getCustomerId());
    		if (customerinforList != null && customerinforList.size()>0) {
    			redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMERINFORMATION_KEY+customer.getAuthCode(),customerinforList);
    		}
    		resultMap.put(Constant.ERROR_CODE,"0");
    	}catch (Exception e){
    		resultMap.put(Constant.ERROR_CODE,"1");
    		resultMap.put(Constant.ERROR_MSG,"删除客户失败");
    		logger.error("删除客户出现异常",e.fillInStackTrace());
    	}
    	return resultMap;
    }
    @RequestMapping("/findCustomerDetail")
    public Map<String, Object> findCustomerDetail(Integer customerId){
    	logger.info("查询客户详情");
    	Map<String, Object> resultMap = Maps.newHashMap();
        try{
        	List<Domain> editDomains = customerService.findCustomerDomain(customerId);
        	List<ServiceInterface> editInterfaces = customerService.findCustomerInterface(customerId);
        	resultMap.put("editDomains",editDomains );
        	resultMap.put("editInterfaces",editInterfaces );
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"查询客户详情失败");
            logger.error("查询客户详情出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }
    
    @RequestMapping("/checkCustomer")
    public Map<String, Object> checkCustomer(Customer customer,String checkType){
    	logger.info("校验客户信息的唯一性");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try {
    		Long count = customerService.checkCustomer(customer,checkType);
    		if (count > 0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
    		resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			logger.error("校验客户信息的唯一性出现错误",e.fillInStackTrace());
			resultMap.put(Constant.ERROR_CODE, "1");
		}
    	return resultMap;
    }
    
    //校验某个接口是否被客户使用
    @RequestMapping("/interfaceYN")
    public Map<String, Object> interfaceYN(Integer interfaceId){
    	logger.info("检测接口"+interfaceId+"是否被客户使用");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		Long count = customerService.interfaceYN(interfaceId);
    		if (count > 0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			} else {
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
    		resultMap.put(Constant.ERROR_CODE, "0");
    	}catch (Exception e) {
    		logger.error("校验接口是否被客户使用出现异常",e.fillInStackTrace());
    		resultMap.put(Constant.ERROR_CODE, "1");
		}
    	return resultMap;
    }
    //校验某个接口是否被客户使用
    @RequestMapping("/modelYN")
    public Map<String, Object> modelYN(Integer modelId){
    	logger.info("检测模型"+modelId+"是否被客户使用");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		Long count = customerService.modelYN(modelId);
    		if (count > 0) {
    			resultMap.put(Constant.CHECK_RESULT, "1");
    		} else {
    			resultMap.put(Constant.CHECK_RESULT, "0");
    		}
    		resultMap.put(Constant.ERROR_CODE, "0");
    	}catch (Exception e) {
    		logger.error("校验模型是否被客户使用出现异常",e.fillInStackTrace());
    		resultMap.put(Constant.ERROR_CODE, "1");
    	}
    	return resultMap;
    }
    //校验某个领域是否被客户使用
    @RequestMapping("/domainYN")
    public Map<String, Object> domainYN(Integer domainId){
    	logger.info("检测领域"+domainId+"是否被客户使用");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		Long count = customerService.domainYN(domainId);
    		if (count > 0) {
    			resultMap.put(Constant.CHECK_RESULT, "1");
    		} else {
    			resultMap.put(Constant.CHECK_RESULT, "0");
    		}
    		resultMap.put(Constant.ERROR_CODE, "0");
    	}catch (Exception e) {
    		logger.error("校验模型是否被客户使用出现异常",e.fillInStackTrace());
    		resultMap.put(Constant.ERROR_CODE, "1");
    	}
    	return resultMap;
    }
    //校验某个维度是否被客户使用
    @RequestMapping("/dimensionYN")
    public Map<String, Object> dimensionYN(Integer dimensionId){
    	logger.info("检测维度"+dimensionId+"是否被客户使用");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		Long count = customerService.dimensionYN(dimensionId);
    		if (count > 0) {
    			resultMap.put(Constant.CHECK_RESULT, "1");
    		} else {
    			resultMap.put(Constant.CHECK_RESULT, "0");
    		}
    		resultMap.put(Constant.ERROR_CODE, "0");
    	}catch (Exception e) {
    		logger.error("校验模型是否被客户使用出现异常",e.fillInStackTrace());
    		resultMap.put(Constant.ERROR_CODE, "1");
    	}
    	return resultMap;
    }
    //校验某个字段是否被客户使用
    @RequestMapping("/fieldYN")
    public Map<String, Object> fieldYN(Integer fieldId){
    	logger.info("检测字段"+fieldId+"是否被客户使用");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		Long count = customerService.fieldYN(fieldId);
    		if (count > 0) {
    			resultMap.put(Constant.CHECK_RESULT, "1");
    		} else {
    			resultMap.put(Constant.CHECK_RESULT, "0");
    		}
    		resultMap.put(Constant.ERROR_CODE, "0");
    	}catch (Exception e) {
    		logger.error("校验模型是否被客户使用出现异常",e.fillInStackTrace());
    		resultMap.put(Constant.ERROR_CODE, "1");
    	}
    	return resultMap;
    }
    //客户年度报表饼图数据
    @RequestMapping("/getYearPieData")
    public Map<String, Object> getYearPieData(String yearTime,Integer customerId){
    	logger.info("查询"+customerId+"客户年度报表饼图的数据");
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		customerService.getYearPieData(yearTime,customerId,resultMap);
    		resultMap.put(Constant.ERROR_CODE, "0");
    	}catch (Exception e) {
    		logger.error("查询客户年度报表饼图的数据出现异常",e.fillInStackTrace());
    		resultMap.put(Constant.ERROR_CODE, "1");
    	}
    	return resultMap;
    }
    //客户年度统计列表
    @RequestMapping("/yearCountList")
    public Page<CustomerQueryCount> yearCountList(HttpServletRequest request){
    	logger.info("客户年度统计列表");
    	Page<CustomerQueryCount> pageCustomer = null;
    	try{
			int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
			int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
			String customerId = request.getParameter("customerId");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			Map<String, Object> paramsMap = Maps.newHashMap();
			paramsMap.put(Constant.PAGE_SIZE,pageSize);
			paramsMap.put(Constant.PAGE,currentPage);
    		if (StringUtils.isNoneBlank(beginDate)) {
    			paramsMap.put("beginDate", beginDate);
			}
    		if (StringUtils.isNoneBlank(endDate)) {
    			paramsMap.put("endDate", endDate);
    		}
    		paramsMap.put("customerId", customerId);
    		pageCustomer = customerService.getYearCountList(paramsMap);
    	}catch (Exception e) {
    		logger.error("查询客户年度统计列表出现异常",e.fillInStackTrace());
    	}
    	return pageCustomer;
    }
    //客户月度统计列表
    @RequestMapping("/monthCountList")
    public Page<CustomerQueryCount> monthCountList(HttpServletRequest request){
    	logger.info("客户年度统计列表");
    	Page<CustomerQueryCount> pageCustomer = null;
    	try{
    		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
    		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
    		String customerId = request.getParameter("customerId");
    		String beginDate = request.getParameter("beginDate");
    		String endDate = request.getParameter("endDate");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put(Constant.PAGE_SIZE,pageSize);
    		paramsMap.put(Constant.PAGE,currentPage);
    		if (StringUtils.isNoneBlank(beginDate)) {
    			paramsMap.put("beginDate", beginDate);
    		}
    		if (StringUtils.isNoneBlank(endDate)) {
    			paramsMap.put("endDate", endDate);
    		}
    		paramsMap.put("customerId", customerId);
    		pageCustomer = customerService.getMonthCountList(paramsMap);
    	}catch (Exception e) {
    		logger.error("查询客户年度统计列表出现异常",e.fillInStackTrace());
    	}
    	return pageCustomer;
    }
    //客户日别统计列表
    @RequestMapping("/dailyCountList")
    public Page<CustomerQueryCount> dailyCountList(HttpServletRequest request){
    	logger.info("客户年度统计列表");
    	Page<CustomerQueryCount> pageCustomer = null;
    	try{
    		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
    		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
    		String customerId = request.getParameter("customerId");
    		String beginDate = request.getParameter("beginDate");
    		String endDate = request.getParameter("endDate");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put(Constant.PAGE_SIZE,pageSize);
    		paramsMap.put(Constant.PAGE,currentPage);
    		if (StringUtils.isNoneBlank(beginDate)) {
    			paramsMap.put("beginDate", beginDate);
    		}
    		if (StringUtils.isNoneBlank(endDate)) {
    			paramsMap.put("endDate", endDate);
    		}
    		paramsMap.put("customerId", customerId);
    		pageCustomer = customerService.getDailyCountList(paramsMap);
    	}catch (Exception e) {
    		logger.error("查询客户年度统计列表出现异常",e.fillInStackTrace());
    	}
    	return pageCustomer;
    }
    //导出客户日别统计报表
    @RequestMapping("/exportCustomerDailyReport")
    public ResponseEntity<byte[]> exportCustomerDailyReport(HttpServletRequest request,HttpServletResponse response){
		logger.info("导出日别统计报表");
		try {
			String companyName = request.getParameter("companyName");
			String customerId = request.getParameter("customerId");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			Map<String, Object> paramsMap = Maps.newHashMap();
			paramsMap.put("customerId",customerId );
			paramsMap.put("beginDate",beginDate );
			paramsMap.put("endDate",endDate );
			String title = "接口客户“"+companyName+"”";
			if (beginDate.equals(endDate)) {
				title += beginDate+"日别报表";
			}else{
				title += beginDate+"至"+endDate+"日别报表";
			}
			AdminUser user = customeSession.getUser(request);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			String str = user.getAdminUserId().toString()+(new Date()).getTime();
			headers.setContentDispositionFormData("attachment", str+".xlsx");
			List<CustomerQueryCount> dataList = customerService.exportCustomerDailyReport(paramsMap);
			PoiUtil poiUtil = new PoiUtil();
			Workbook workbook = poiUtil.dailyCustomerReportExport(title, dataList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
			return responseEntity;
		} catch (Exception e) {
			logger.error("CustomerController.class [exportCustomerDailyReport()] :error"+e);
		}
		return null;
	}
    //导出客户月别统计报表
    @RequestMapping("/exportCustomerMonthReport")
    public ResponseEntity<byte[]> exportCustomerMonthReport(HttpServletRequest request,HttpServletResponse response){
    	logger.info("导出月别统计报表");
    	try {
    		String companyName = request.getParameter("companyName");
    		String customerId = request.getParameter("customerId");
    		String beginDate = request.getParameter("beginDate");
    		String endDate = request.getParameter("endDate");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put("customerId",customerId );
    		paramsMap.put("beginDate",beginDate );
    		paramsMap.put("endDate",endDate );
    		String title = "接口客户“"+companyName+"”";
    		if (beginDate.equals(endDate)) {
    			title += beginDate+"月别报表";
    		}else{
    			title += beginDate+"至"+endDate+"月别报表";
    		}
    		AdminUser user = customeSession.getUser(request);
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    		String str = user.getAdminUserId().toString()+(new Date()).getTime();
    		headers.setContentDispositionFormData("attachment", str+".xlsx");
    		List<CustomerQueryCount> dataList = customerService.exportCustomerMonthReport(paramsMap);
    		PoiUtil poiUtil = new PoiUtil();
    		Workbook workbook = poiUtil.monthCustomerReportExport(title, dataList);
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		workbook.write(bos);
    		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
    		return responseEntity;
    	} catch (Exception e) {
    		logger.error("CustomerController.class [exportCustomerMonthReport()] :error"+e);
    	}
    	return null;
    }
    //导出客户年度统计报表
    @RequestMapping("/exportCustomerYearReport")
    public ResponseEntity<byte[]> exportCustomerYearReport(HttpServletRequest request,HttpServletResponse response){
    	logger.info("导出年度统计报表");
    	try {
    		String companyName = request.getParameter("companyName");
    		String customerId = request.getParameter("customerId");
    		String beginDate = request.getParameter("beginDate");
    		String endDate = request.getParameter("endDate");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put("customerId",customerId );
    		paramsMap.put("beginDate",beginDate );
    		paramsMap.put("endDate",endDate );
    		String title = "接口客户“"+companyName+"”";
    		if (beginDate.equals(endDate)) {
    			title += beginDate+"年度报表";
    		}else{
    			title += beginDate+"至"+endDate+"年度报表";
    		}
    		AdminUser user = customeSession.getUser(request);
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    		String str = user.getAdminUserId().toString()+(new Date()).getTime();
    		headers.setContentDispositionFormData("attachment", str+".xlsx");
    		List<CustomerQueryCount> dataList = customerService.exportCustomerYearReport(paramsMap);
    		PoiUtil poiUtil = new PoiUtil();
    		Workbook workbook = poiUtil.yearCustomerReportExport(title, dataList);
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		workbook.write(bos);
    		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
    		return responseEntity;
    	} catch (Exception e) {
    		logger.error("CustomerController.class [exportCustomerYearReport()] :error"+e);
    	}
    	return null;
    }
}
