package com.fahai.cc.service.customer.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.util.Page;

public interface CustomerQueryCountService {

	Page<CustomerQueryCount> findDailyCount(Map<String, Object> paramsMap);

	Page<CustomerQueryCount> findMonthCount(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportMonthReport(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportDailyReport(Map<String, Object> paramsMap);

}
