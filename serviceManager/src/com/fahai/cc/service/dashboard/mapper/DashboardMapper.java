package com.fahai.cc.service.dashboard.mapper;

import java.util.List;

import com.fahai.cc.service.dashboard.entity.DashboardInterfaceCountDto;
import com.fahai.cc.service.dashboard.entity.DashboardTop5Dto;

public interface DashboardMapper {

	long getDayQSTotal();

	List<DashboardTop5Dto> getDayTop5();

	List<DashboardInterfaceCountDto> getInterfaceCountData();

	List<DashboardInterfaceCountDto> findAllValid();

}
