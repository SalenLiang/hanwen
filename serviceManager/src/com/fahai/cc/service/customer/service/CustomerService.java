package com.fahai.cc.service.customer.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.interf.mysql.entity.CustomerInformation;
import com.fahai.cc.service.customer.entity.Customer;
import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.util.Page;

public interface CustomerService {

	Page<Customer> findPageCustomer(Map<String, Object> paramsMap);

	Customer saveCustomer(String customer, String addCustomerServiceInterfaces, String addCustomerServiceDomains,
			Integer adminUserId);

	List<Domain> findCustomerDomain(Integer customerId);

	List<ServiceInterface> findCustomerInterface(Integer customerId);

	Customer updateCustomer(String customer, String addCustomerServiceInterfaces, String addCustomerServiceDomains,
			Integer adminUserId);

	Long checkCustomer(Customer customer, String checkType) throws Exception;

	List<CustomerInformation> getCustomerList();

	List<CustomerInformation> getCustomerinfor(int parseInt);

	Long interfaceYN(Integer interfaceId);

	Long modelYN(Integer modelId);

	Long domainYN(Integer domainId);

	Long dimensionYN(Integer dimensionId);

	Long fieldYN(Integer fieldId);

	List<CustomerInformation> getCustomerInformationListByModelId(Integer modelId);

	CustomerInformation getCustomer(Integer customerId);

	Customer deleteCustomerById(String customerStr, Integer adminUserId);

	Customer updateCustomerStatus(String customerStr, Integer adminUserId);

	void getYearPieData(String yearTime, Integer customerId, Map<String, Object> resultMap);

	Page<CustomerQueryCount> getYearCountList(Map<String, Object> paramMap);

	Page<CustomerQueryCount> getMonthCountList(Map<String, Object> paramsMap);

	Page<CustomerQueryCount> getDailyCountList(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportCustomerDailyReport(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportCustomerMonthReport(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportCustomerYearReport(Map<String, Object> paramsMap);

	
}
