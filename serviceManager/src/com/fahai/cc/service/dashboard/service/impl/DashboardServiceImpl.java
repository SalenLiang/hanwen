package com.fahai.cc.service.dashboard.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.dashboard.entity.DashboardInterfaceCountDto;
import com.fahai.cc.service.dashboard.entity.DashboardTop5Dto;
import com.fahai.cc.service.dashboard.mapper.DashboardMapper;
import com.fahai.cc.service.dashboard.service.DashboardService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.HttpClientUtil;
@Service
public class DashboardServiceImpl implements DashboardService {
	private Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);
	
	@Autowired
	private DashboardMapper dashboardMapper;

	@Override
	public void getCustomerTop5Data(Map<String, Object> resultMap) {
		//查询今日查询成功的总量
		long qsTotal = dashboardMapper.getDayQSTotal();
		resultMap.put("qsTotal", qsTotal);
		if (qsTotal>0) {
			//今日查询量top5的查询量
			List<DashboardTop5Dto> top5List = dashboardMapper.getDayTop5();
			if (top5List != null && top5List.size()>0) {
				resultMap.put("top5List", top5List);
			}
		}
	}

	@Override
	public List<DashboardInterfaceCountDto> getInterfaceCountData() {
		
		return dashboardMapper.getInterfaceCountData();
	}

	@Override
	public List<DashboardInterfaceCountDto> getInterfaceStatus(){
		List<DashboardInterfaceCountDto> allValid = dashboardMapper.findAllValid();
		for (DashboardInterfaceCountDto dashboardInterfaceCountDto : allValid) {
			int requestSatusCode = 0;
			try {
				requestSatusCode = HttpClientUtil.getRequestSatusCode(dashboardInterfaceCountDto.getInterfaceURL(), 500);
			} catch (IOException e) {
				logger.error("DashboardServiceImpl.class [getInterfaceStatus()] :error"+e);
			}
			if (Constant.CHECK_INTERFACE_S==requestSatusCode) {
				dashboardInterfaceCountDto.setHeartStatus("0");//正常
			}else{
				dashboardInterfaceCountDto.setHeartStatus("1");//异常
			}
		}
		return allValid;
	}

}
