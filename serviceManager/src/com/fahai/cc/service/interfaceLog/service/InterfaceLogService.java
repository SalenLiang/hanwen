package com.fahai.cc.service.interfaceLog.service;

import java.util.Map;

import com.fahai.cc.service.interfaceLog.entity.InterfaceLog;
import com.fahai.cc.service.util.Page;

public interface InterfaceLogService {

	Page<InterfaceLog> findByPage(Map<String, Object> paramsMap);

}
