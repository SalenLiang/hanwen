package com.fahai.cc.service.interfaceLog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.interfaceLog.entity.InterfaceLog;
import com.fahai.cc.service.interfaceLog.mapper.InterfaceLogMapper;
import com.fahai.cc.service.interfaceLog.service.InterfaceLogService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.JodaDateUtil;
import com.fahai.cc.service.util.Page;
@Service
public class InterfaceLogServiceImpl implements InterfaceLogService {

	
	@Autowired
	private InterfaceLogMapper interfaceLogMapper;
	
	@Override
	public Page<InterfaceLog> findByPage(Map<String, Object> paramsMap) {
		
		if ((String)paramsMap.get("beginDate") != null && (String)paramsMap.get("beginDate") != "") {
			Date beginDate = JodaDateUtil.parseStringToDate((String)paramsMap.get("beginDate"), "yyyy-MM-dd"); 
			paramsMap.put("beginDate", beginDate);
		}
		
		if ((String)paramsMap.get("endDate")!=null&&(String)paramsMap.get("endDate")!="") {
			Date endDate = JodaDateUtil.parseStringToDate((String)paramsMap.get("endDate"), "yyyy-MM-dd"); 
			paramsMap.put("endDate", endDate);
		}
		
		int pageSize = (int) paramsMap.get(Constant.PAGE_SIZE);
		
		long totalCount = interfaceLogMapper.getTotalCount(paramsMap);
		
		
		if (totalCount == 0L) {
			return new Page<>(1,0,pageSize,null);
		}
		
		int currentPage = (int) paramsMap.get(Constant.PAGE);
		
		int start = (currentPage-1)*pageSize;
		
		paramsMap.put(Constant.PAGE_START, start);
		
		List<InterfaceLog> interfaceLogList = interfaceLogMapper.fingPageInterfaceLog(paramsMap);
		
		Page<InterfaceLog> page = new Page<>();
		
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalCount((int)totalCount);
		page.setList(interfaceLogList);
		
		
		return page;
	}
	
}
