package com.fahai.cc.service.dashboard.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import com.fahai.cc.service.dashboard.entity.DashboardInterfaceCountDto;
import com.fahai.cc.service.dashboard.service.DashboardService;
import com.fahai.cc.service.socket.SystemWebSocketHandler;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.HttpClientUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

@RestController(value = "dashboardController")
@RequestMapping("/serviceManager/dashboard")
public class DashboardController {

	private Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping("/getTop5Data")
	public Map<String, Object> getCustomerTop5Data() {
		logger.info("top5统计");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			dashboardService.getCustomerTop5Data(resultMap);
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			logger.error("DashboardController.class [getCustomerTop5Data()] :error" + e);
		}
		return resultMap;
	}

	@RequestMapping("/getInterfaceCountData")
	public Map<String, Object> getInterfaceCountData() {
		logger.info("接口预警");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			List<DashboardInterfaceCountDto> list = dashboardService.getInterfaceCountData();
			resultMap.put(Constant.DATA_LIST, list);
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			logger.error("DashboardController.class [getInterfaceCountData()] :error" + e);
		}
		return resultMap;
	}
	
	
	

	public void checkInterfaceHeartBeat() {
		logger.info("检测接口的状态");
		Map<Object, Object> resultMap = Maps.newHashMap();
		int returnCode = 0;
		try {
			List<DashboardInterfaceCountDto> interfaceStatusList = dashboardService.getInterfaceStatus();
			resultMap.put(Constant.ERROR_CODE, "0");
			resultMap.put(Constant.CHECK_RESULT,interfaceStatusList);
		} catch (Exception e) {
			logger.error("DashboardController.class [checkInterfaceHeartBeat()] :error"+e);
			resultMap.put(Constant.ERROR_CODE, "1");
		} 
		Gson gson = new Gson();
		SystemWebSocketHandler.sendMessageToAllUser(new TextMessage(gson.toJson(resultMap)));
	}

}
