package com.fahai.cc.service.customer.service.impl;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.interf.mysql.entity.CustomerInformation;
import com.fahai.cc.service.customer.entity.Customer;
import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.customer.entity.CustomerYearPieDto;
import com.fahai.cc.service.customer.mapper.CustomerMapper;
import com.fahai.cc.service.customer.service.CustomerService;
import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.field.entity.Field;
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.settingTemplet.mapper.SettingTempletMapper;
import com.fahai.cc.service.util.CommonUtil;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.JsonUtil;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.vo.CustomerInterfaceVo;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private SettingTempletMapper settingTempletMapper;
	
	@Override
	public Page<Customer> findPageCustomer(Map<String, Object> paramsMap) {
		long totalCount = customerMapper.getTotalCount(paramsMap);
        int pageSize = (int) paramsMap.get(Constant.PAGE_SIZE);

        if(totalCount == 0l){
            return new Page<>(1,0,pageSize,null);
        }

        int currentPage = (int) paramsMap.get(Constant.PAGE);
        int start = (currentPage-1)*pageSize;
        paramsMap.put(Constant.PAGE_START,start);
        List<Customer> customerList = customerMapper.findPageCustomer(paramsMap);

        Page<Customer> page = new Page<>();
        page.setTotalCount((int)totalCount);
        page.setList(customerList);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
		return page;
	}
	@Override
	public Customer updateCustomerStatus(String customerStr, Integer adminUserId) {
		Customer customer = JsonUtil.toBean(customerStr, Customer.class); 
		Customer activeCustomer = new Customer();
		activeCustomer.setCustomerId(customer.getCustomerId());
		activeCustomer.setStatus(Constant.NORMAL_STATUS_CODE);
		activeCustomer.setLastModifyDate(new Date());
		customerMapper.updateCustomer(activeCustomer);
		//记录日志
		customer.setStatus(Constant.NORMAL_STATUS_CODE);
		customer.setLastModifyDate(new Date());
		customer.setActionType(Constant.ACTIONTYPE_LOG_ACTIVATION);
		customer.setActionUserId(adminUserId);
		customerMapper.saveCustomerLog(customer);
		
		return customer;
	}
	@Override
	public Customer deleteCustomerById(String customerStr, Integer adminUserId){
		
		Customer customer = JsonUtil.toBean(customerStr, Customer.class); 
		customer.setStatus(Constant.DELETE_STATUS_CODE);
		customer.setLastModifyDate(new Date());
		customerMapper.updateCustomer(customer);
		//记录日志
		customer.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
		customer.setActionUserId(adminUserId);
		customerMapper.saveCustomerLog(customer);
		
		return customer;
	}

	public Customer saveCustomer(String customer, String addCustomerServiceInterfaces, String addCustomerServiceDomains,
			Integer adminUserId) {
		//保存客户的基本信息
		Customer customerBean = JsonUtil.toBean(customer, Customer.class); 
		customerBean.setCreateAdminUserId(adminUserId);
		customerBean.setStatus(0);
		customerBean.setCreateDate(new Date());
		customerBean.setLastModifyDate(new Date());
		//生成20位的授权码
		String authCode = CommonUtil.randomPassword(20); 
		Customer isExist = customerMapper.getCustomerByAuthCode(authCode);
		while (isExist !=null) {
			authCode = CommonUtil.randomPassword(20);
			isExist = customerMapper.getCustomerByAuthCode(authCode);
		}
		customerBean.setAuthCode(authCode);
		customerMapper.saveCustomer(customerBean);
		customerMapper.saveCustomerConfig(customerBean);
		//记录日志
		customerBean.setActionUserId(adminUserId);
		customerBean.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
		customerMapper.saveCustomerLog(customerBean);
		
		HashMap<String, Object> newMap = Maps.newHashMap();
		newMap.put("customerId", customerBean.getCustomerId());
		newMap.put("createDate",new Date());
		newMap.put("createAdminUserId", adminUserId);
		
		Type type = new TypeToken<List<CustomerInterfaceVo>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
        //保存客户的接口选项
        List<CustomerInterfaceVo> serviceInterfaceList= gson.fromJson(addCustomerServiceInterfaces, type);
		if(serviceInterfaceList != null && serviceInterfaceList.size() > 0){
			for (CustomerInterfaceVo serviceInterface : serviceInterfaceList) {
				newMap.put("interfaceId", serviceInterface.getInterfaceId());
				newMap.put("status", 0);
				newMap.put("modelId", serviceInterface.getModelId());
				newMap.put("customRange", serviceInterface.getCustomRange());
				newMap.put("customQuery", serviceInterface.getCustomQuery());
				customerMapper.saveCustomerInterface(newMap);
				//记录日志
				newMap.put("logId", customerBean.getLogId());
				newMap.put("actionType", Constant.ACTIONTYPE_LOG_SAVE);
				newMap.put("actionUserId", adminUserId);
				customerMapper.saveCustomerInterfaceLog(newMap);
			}
		}
		type = new TypeToken<List<Domain>>() {}.getType();
		List<Domain> domainList= gson.fromJson(addCustomerServiceDomains, type);
		if (domainList != null && domainList.size() > 0) {
			//保存客户的领域 维度 字段 选项
			for (Domain domain : domainList) {
				//保存客户领域
				newMap.clear();
				newMap.put("customerId", customerBean.getCustomerId());
				newMap.put("createDate",new Date());
				newMap.put("createAdminUserId", adminUserId);
				newMap.put("domainId", domain.getDomainId());
				customerMapper.saveCustomerDomain(newMap);
				for (Dimension dimension : domain.getDimensionList()) {
					newMap.put("dimensionId", dimension.getDimensionId());
					customerMapper.saveCustomerDimension(newMap);
					for (Field field : dimension.getFieldList()) {
						newMap.put("fieldId", field.getFieldId());
						newMap.put("detailShowYN", field.getDefaultDetailShowYN());
						newMap.put("listShowYN", field.getDefaultListShowYN());
						customerMapper.saveCustomerField(newMap);
						
						//记录日志
						newMap.put("logId", customerBean.getLogId());
						newMap.put("actionType", Constant.ACTIONTYPE_LOG_SAVE);
						newMap.put("actionUserId", adminUserId);
						customerMapper.saveCustomerFieldLog(newMap);
					}
				}
				
			}
		}
		
		return customerBean;
		
	}


	@Override
	public List<Domain> findCustomerDomain(Integer customerId) {
		
		return customerMapper.findCustomerDomain(customerId);
	}


	@Override
	public List<ServiceInterface> findCustomerInterface(Integer customerId) {
		
		return customerMapper.findCustomerInterface(customerId);
	}


	public Customer updateCustomer(String customer, String addCustomerServiceInterfaces, String addCustomerServiceDomains,Integer adminUserId) {
		//更新客户基本信息
		Customer customerBean = JsonUtil.toBean(customer, Customer.class); 
		customerBean.setLastModifyDate(new Date());
		customerMapper.updateCustomer(customerBean);
		Integer customerId = customerBean.getCustomerId();
		customerMapper.updateCustomerConfig(customerBean);
		
		//记录日志
		customerBean.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
		customerBean.setActionUserId(adminUserId);
		customerMapper.saveCustomerLog(customerBean);
		
		
		//现将该客户的接口和领域等信息删除
		customerMapper.deleteInterfacesByCustomerId(customerId);
		customerMapper.deleteDomainsByCustomerId(customerId);
		
		HashMap<String, Object> newMap = Maps.newHashMap();
		newMap.put("customerId", customerId);
		newMap.put("createDate",new Date());
		newMap.put("createAdminUserId", customerBean.getCreateAdminUserId());
		Type type = new TypeToken<List<CustomerInterfaceVo>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
        //保存客户的接口选项
        List<CustomerInterfaceVo> serviceInterfaceList= gson.fromJson(addCustomerServiceInterfaces, type);
		if (serviceInterfaceList != null && serviceInterfaceList.size() > 0) {
			for (CustomerInterfaceVo serviceInterface : serviceInterfaceList) {
				newMap.put("interfaceId", serviceInterface.getInterfaceId());
				newMap.put("status", 0);
				newMap.put("modelId", serviceInterface.getModelId());
				newMap.put("customRange", serviceInterface.getCustomRange());
				newMap.put("customQuery", serviceInterface.getCustomQuery());
				customerMapper.saveCustomerInterface(newMap);
				//记录日志
				newMap.put("logId", customerBean.getLogId());
				newMap.put("actionType", Constant.ACTIONTYPE_LOG_UPDATE);
				newMap.put("actionUserId", adminUserId);
				customerMapper.saveCustomerInterfaceLog(newMap);
			}
		}
		type = new TypeToken<List<Domain>>() {}.getType();
		List<Domain> domainList= gson.fromJson(addCustomerServiceDomains, type);
		if (domainList != null && domainList.size() > 0) {
			//保存客户的领域 维度 字段 选项
			for (Domain domain : domainList) {
				//保存客户领域
				newMap.clear();
				newMap.put("customerId", customerId);
				newMap.put("createDate",new Date());
				newMap.put("createAdminUserId", customerBean.getCreateAdminUserId());
				newMap.put("domainId", domain.getDomainId());
				customerMapper.saveCustomerDomain(newMap);
				for (Dimension dimension : domain.getDimensionList()) {
					newMap.put("dimensionId", dimension.getDimensionId());
					customerMapper.saveCustomerDimension(newMap);
					for (Field field : dimension.getFieldList()) {
						newMap.put("fieldId", field.getFieldId());
						newMap.put("detailShowYN", field.getDefaultDetailShowYN());
						newMap.put("listShowYN", field.getDefaultListShowYN());
						customerMapper.saveCustomerField(newMap);

						//记录日志
						newMap.put("logId", customerBean.getLogId());
						newMap.put("actionType", Constant.ACTIONTYPE_LOG_UPDATE);
						newMap.put("actionUserId", adminUserId);
						customerMapper.saveCustomerFieldLog(newMap);
					}
				}
				
			}
		}
		return customerBean;
	}

	@Override
	public Long checkCustomer(Customer customer, String checkType) throws Exception {
		HashMap<String, Object> params = Maps.newHashMap();
		if ("companyName".equals(checkType)) {
			params.put("companyName", customer.getCompanyName());
		}else if ("contactTel".equals(checkType)) {
			params.put("contactTel", customer.getContactTel());
		}else if ("contactPhone".equals(checkType)) {
			params.put("contactPhone", customer.getContactPhone());
		}else if ("contactEmail".equals(checkType)) {
			params.put("contactEmail", customer.getContactEmail());
		}else if ("userName".equals(checkType)) {
			params.put("userName", customer.getUserName());
		}else{
			throw new Exception("不存在需要校验的客户信息"); 
		}
		
		params.put("customerId", customer.getCustomerId());
		
		
		return customerMapper.findCountByParam(params);
	}

	@Override
	public List<CustomerInformation> getCustomerList() {
		
		return customerMapper.getCustomerList();
	}

	@Override
	public List<CustomerInformation> getCustomerinfor(int customerId) {
		
		return customerMapper.getCustomerinfor(customerId);
	}

	@Override
	public Long interfaceYN(Integer interfaceId) {
		
		return customerMapper.interfaceYN(interfaceId);
	}

	@Override
	public Long modelYN(Integer modelId) {
		
		return customerMapper.modelYN(modelId);
	}

	@Override
	public Long domainYN(Integer domainId) {
		
		return customerMapper.domainYN(domainId);
	}

	@Override
	public Long dimensionYN(Integer dimensionId) {
		
		return customerMapper.dimensionYN(dimensionId);
	}

	@Override
	public Long fieldYN(Integer fieldId) {
		
		return customerMapper.fieldYN(fieldId);
	}

	@Override
	public List<CustomerInformation> getCustomerInformationListByModelId(Integer modelId) {
		
		return customerMapper.getCustomerInformationListByModelId(modelId);
	}

	@Override
	public CustomerInformation getCustomer(Integer customerId) {
		
		return customerMapper.getCustomer(customerId);
	}
	@Override
	public void getYearPieData(String yearTime, Integer customerId, Map<String, Object> resultMap) {
		//统计接口的查询量
		List<CustomerYearPieDto> interfaceCodeList = customerMapper.getInterfaceCodeYearPieData(yearTime,customerId);
		//统计类型的查询量
		List<CustomerYearPieDto> searchTypeList = customerMapper.getSearchTypeYearPieData(yearTime,customerId);
		Set<String> typeSet = new HashSet<>();
		if (interfaceCodeList!=null&&interfaceCodeList.size()>0) {
			resultMap.put("seriesData1", interfaceCodeList);
			for (CustomerYearPieDto customerYearPieDto : interfaceCodeList) {
				typeSet.add(customerYearPieDto.getName());
			}
		}
		if (searchTypeList!=null&&searchTypeList.size()>0) {
			resultMap.put("seriesData2", searchTypeList);
			for (CustomerYearPieDto customerYearPieDto : searchTypeList) {
				typeSet.add(customerYearPieDto.getName());
			}
		}
		if (typeSet.size()>0) {
			resultMap.put("legendData", typeSet);
		}
		
	}
	@Override
	public Page<CustomerQueryCount> getYearCountList(Map<String, Object> paramsMap) {

		long totalCount = customerMapper.getTotalYearCount(paramsMap);
		
		int pageSize = (int)paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount == 0L) {
			return new Page<CustomerQueryCount>(1, 0, pageSize, null);
		}
		
		int currentPage = (int)paramsMap.get(Constant.PAGE);
		int start = (currentPage-1)*pageSize;
		paramsMap.put(Constant.PAGE_START, start);
		
		List<CustomerQueryCount> lists = customerMapper.getYearCountList(paramsMap);
		
		Page<CustomerQueryCount> pages = new Page<>();
		
		pages.setCurrentPage(currentPage);
		pages.setPageSize(pageSize);
		pages.setTotalCount((int)totalCount);
		pages.setList(lists);
		
		return pages;
	}
	@Override
	public Page<CustomerQueryCount> getMonthCountList(Map<String, Object> paramsMap) {

		long totalCount = customerMapper.getTotalMonthCount(paramsMap);
		
		int pageSize = (int)paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount == 0L) {
			return new Page<CustomerQueryCount>(1, 0, pageSize, null);
		}
		
		int currentPage = (int)paramsMap.get(Constant.PAGE);
		int start = (currentPage-1)*pageSize;
		paramsMap.put(Constant.PAGE_START, start);
		
		List<CustomerQueryCount> lists = customerMapper.getMonthCountList(paramsMap);
		
		Page<CustomerQueryCount> pages = new Page<>();
		
		pages.setCurrentPage(currentPage);
		pages.setPageSize(pageSize);
		pages.setTotalCount((int)totalCount);
		pages.setList(lists);
		
		return pages;
	}
	@Override
	public Page<CustomerQueryCount> getDailyCountList(Map<String, Object> paramsMap) {

		long totalCount = customerMapper.getTotalDailyCount(paramsMap);
		
		int pageSize = (int)paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount == 0L) {
			return new Page<CustomerQueryCount>(1, 0, pageSize, null);
		}
		
		int currentPage = (int)paramsMap.get(Constant.PAGE);
		int start = (currentPage-1)*pageSize;
		paramsMap.put(Constant.PAGE_START, start);
		
		List<CustomerQueryCount> lists = customerMapper.getDailyCountList(paramsMap);
		
		Page<CustomerQueryCount> pages = new Page<>();
		
		pages.setCurrentPage(currentPage);
		pages.setPageSize(pageSize);
		pages.setTotalCount((int)totalCount);
		pages.setList(lists);
		
		return pages;
	}
	@Override
	public List<CustomerQueryCount> exportCustomerDailyReport(Map<String, Object> paramsMap) {
		List<CustomerQueryCount> dataList = customerMapper.exportCustomerDailyReport(paramsMap);
		return dataList;
	}
	@Override
	public List<CustomerQueryCount> exportCustomerMonthReport(Map<String, Object> paramsMap) {
		List<CustomerQueryCount> dataList = customerMapper.exportCustomerMonthReport(paramsMap);
		return dataList;
	}
	@Override
	public List<CustomerQueryCount> exportCustomerYearReport(Map<String, Object> paramsMap) {
		List<CustomerQueryCount> dataList = customerMapper.exportCustomerYearReport(paramsMap);
		return dataList;
	}

}
