package com.fahai.cc.service.customer.mapper;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.customer.entity.CustomerQueryCount;

public interface CustomerQueryCountMapper {

	Long getTotalDailyCount(Map<String, Object> paramsMap);

	List<CustomerQueryCount> getDailyCountList(Map<String, Object> paramsMap);

	Long getTotalMonthCount(Map<String, Object> paramsMap);

	List<CustomerQueryCount> getMonthCountList(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportMonthReport(Map<String, Object> paramsMap);

	List<CustomerQueryCount> exportDailyReport(Map<String, Object> paramsMap);

}
