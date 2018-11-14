package com.fahai.cc.service.interfaceLog.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.interfaceLog.entity.InterfaceLog;
import com.fahai.cc.service.interfaceLog.service.InterfaceLogService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/interfaceLog")
public class InterfaceLogController {
	
	@Autowired
	private InterfaceLogService interfaceLogService;
	
	@RequestMapping(value="/find",method=RequestMethod.POST)
	public Page<InterfaceLog> find(HttpServletRequest request){
		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
        int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
        String companyName = request.getParameter("companyName");
        String interfaceCode = request.getParameter("interfaceCode");
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String ettType = request.getParameter("ettType");
        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put(Constant.PAGE_SIZE,pageSize);
        paramsMap.put(Constant.PAGE,currentPage);
        paramsMap.put("ettType",ettType);
        paramsMap.put("companyName", companyName);
        paramsMap.put("interfaceCode",interfaceCode);
        paramsMap.put("endDate",endDate);
        paramsMap.put("beginDate",beginDate);
        
        Page<InterfaceLog> interfaceLogPage = interfaceLogService.findByPage(paramsMap);
		return interfaceLogPage;
	}
	
}
