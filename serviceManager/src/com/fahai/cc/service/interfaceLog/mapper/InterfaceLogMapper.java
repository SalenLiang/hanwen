package com.fahai.cc.service.interfaceLog.mapper;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.interfaceLog.entity.InterfaceLog;

public interface InterfaceLogMapper {

	long getTotalCount(Map<String, Object> paramsMap);

	List<InterfaceLog> fingPageInterfaceLog(Map<String, Object> paramsMap);
	
}
