package com.fahai.cc.service.dashboard.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.dashboard.entity.DashboardInterfaceCountDto;

public interface DashboardService {

	void getCustomerTop5Data(Map<String, Object> resultMap);

	List<DashboardInterfaceCountDto> getInterfaceCountData();

	List<DashboardInterfaceCountDto> getInterfaceStatus();

}
