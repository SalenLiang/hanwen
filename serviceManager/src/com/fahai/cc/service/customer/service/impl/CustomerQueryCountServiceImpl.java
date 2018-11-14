package com.fahai.cc.service.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.customer.mapper.CustomerQueryCountMapper;
import com.fahai.cc.service.customer.service.CustomerQueryCountService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.Page;
@Service
public class CustomerQueryCountServiceImpl implements CustomerQueryCountService {
	@Autowired
	private CustomerQueryCountMapper customerQueryCountMapper;
	@Override
	public Page<CustomerQueryCount> findDailyCount(Map<String, Object> paramsMap) {
		
		
		long totalCount = customerQueryCountMapper.getTotalDailyCount(paramsMap);
		
		int pageSize = (int)paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount == 0L) {
			return new Page<CustomerQueryCount>(1, 0, pageSize, null);
		}
		
		int currentPage = (int)paramsMap.get(Constant.PAGE);
		int start = (currentPage-1)*pageSize;
		paramsMap.put(Constant.PAGE_START, start);
		
		List<CustomerQueryCount> lists = customerQueryCountMapper.getDailyCountList(paramsMap);
		
		Page<CustomerQueryCount> pages = new Page<>();
		
		pages.setCurrentPage(currentPage);
		pages.setPageSize(pageSize);
		pages.setTotalCount((int)totalCount);
		pages.setList(lists);
		
		return pages;
	}
	@Override
	public Page<CustomerQueryCount> findMonthCount(Map<String, Object> paramsMap) {

		long totalCount = customerQueryCountMapper.getTotalMonthCount(paramsMap);
		
		int pageSize = (int)paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount == 0L) {
			return new Page<CustomerQueryCount>(1, 0, pageSize, null);
		}
		
		int currentPage = (int)paramsMap.get(Constant.PAGE);
		int start = (currentPage-1)*pageSize;
		paramsMap.put(Constant.PAGE_START, start);
		
		List<CustomerQueryCount> lists = customerQueryCountMapper.getMonthCountList(paramsMap);
		
		Page<CustomerQueryCount> pages = new Page<>();
		
		pages.setCurrentPage(currentPage);
		pages.setPageSize(pageSize);
		pages.setTotalCount((int)totalCount);
		pages.setList(lists);
		
		return pages;
	}
	@Override
	public List<CustomerQueryCount> exportMonthReport(Map<String, Object> paramsMap) {
		List<CustomerQueryCount> dataList = customerQueryCountMapper.exportMonthReport(paramsMap);
		return dataList;
	}
	@Override
	public List<CustomerQueryCount> exportDailyReport(Map<String, Object> paramsMap) {
		List<CustomerQueryCount> dataList = customerQueryCountMapper.exportDailyReport(paramsMap);
		return dataList;
	}
}
