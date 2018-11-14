package com.fahai.cc.service.customer.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fahai.cc.interf.mysql.entity.CustomerInformation;
import com.fahai.cc.service.customer.entity.Customer;
import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.customer.entity.CustomerYearPieDto;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;

public interface CustomerMapper {

	long getTotalCount(Map<String, Object> paramsMap);


	List<Customer> findPageCustomer(Map<String, Object> paramsMap);


	int  saveCustomer(Customer customer);


	void updateCustomer(Customer customerBean);


	void saveCustomerInterface(HashMap<String, Object> newMap);


	void saveCustomerDomain(HashMap<String, Object> newMap);


	void saveCustomerDimension(HashMap<String, Object> newMap);


	void saveCustomerField(HashMap<String, Object> newMap);


	List<ServiceInterface> findCustomerInterface(Integer customerId);


	List<Domain> findCustomerDomain(Integer customerId);


	void deleteInterfacesByCustomerId(Integer customerId);


	void deleteDomainsByCustomerId(Integer customerId);


	Long findCountByParam(HashMap<String, Object> params);


	Customer getCustomerByAuthCode(String authCode);


	List<CustomerInformation> getCustomerList();


	List<CustomerInformation> getCustomerinfor(int customerId);

	Long interfaceYN(Integer interfaceId);


	Long modelYN(Integer modelId);


	Long domainYN(Integer domainId);


	Long dimensionYN(Integer dimensionId);


	Long fieldYN(Integer fieldId);


	CustomerInformation getCustomer(Integer customerId);


	List<CustomerInformation> getCustomerInformationListByModelId(Integer modelId);


	void saveCustomerLog(Customer customerBean);


	void saveCustomerFieldLog(HashMap<String, Object> newMap);


	void saveCustomerInterfaceLog(HashMap<String, Object> newMap);


	void saveCustomerConfig(Customer customerBean);
	

	void updateCustomerConfig(Customer customerBean);


	List<CustomerYearPieDto> getInterfaceCodeYearPieData(String yearTime, Integer customerId);


	List<CustomerYearPieDto> getSearchTypeYearPieData(String yearTime, Integer customerId);


	long getTotalYearCount(Map<String, Object> paramMap);


	List<CustomerQueryCount> getYearCountList(Map<String, Object> paramsMap);


	long getTotalMonthCount(Map<String, Object> paramsMap);


	List<CustomerQueryCount> getMonthCountList(Map<String, Object> paramsMap);


	long getTotalDailyCount(Map<String, Object> paramsMap);


	List<CustomerQueryCount> getDailyCountList(Map<String, Object> paramsMap);


	List<CustomerQueryCount> exportCustomerDailyReport(Map<String, Object> paramsMap);


	List<CustomerQueryCount> exportCustomerMonthReport(Map<String, Object> paramsMap);


	List<CustomerQueryCount> exportCustomerYearReport(Map<String, Object> paramsMap);


}
