package com.fahai.cc.service.customer.controller;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.service.AdminUserService;
import com.fahai.cc.service.adminUserRegion.service.AdminUserRegionService;
import com.fahai.cc.service.customer.entity.CustomerQueryCount;
import com.fahai.cc.service.customer.service.CustomerQueryCountService;
import com.fahai.cc.service.region.entity.Region;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.util.PoiUtil;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/customerQueryCount")
public class CustomerQueryCountController {
	
	private Logger logger = LoggerFactory.getLogger(CustomerQueryCountController.class);
	
	@Autowired
	private CustomerQueryCountService customerQueryCountService;
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private AdminUserRegionService adminUserRegionService;
	
	@Autowired
	private CustomeSession customeSession;
	/**
	 * 
	 * @param request
	 * @return
	 * @description 查询日别统计的列表
	 * @throws
	 */
	@RequestMapping("/dailyCount")
	public Page<CustomerQueryCount> findDailyCount(HttpServletRequest request){
		logger.info("日别统计");
		Page<CustomerQueryCount> pageCustomer = null;
		try {
			
			int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
			int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
			String companyName = request.getParameter("companyName");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			Map<String, Object> paramsMap = Maps.newHashMap();
			paramsMap.put(Constant.PAGE_SIZE,pageSize);
			paramsMap.put(Constant.PAGE,currentPage);
			if (StringUtils.isNotBlank(beginDate)) {
				paramsMap.put("beginDate", beginDate);
			}
			if (StringUtils.isNotBlank(endDate)) {
				paramsMap.put("endDate", endDate);
			}
			if(StringUtils.isNotBlank(companyName)){
				paramsMap.put("companyName",companyName);
			}
//			AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
			AdminUser user = customeSession.getUser(request);
			List<String> roleIdList = adminUserService.findRoleIdByAdminUserId(user.getAdminUserId());
	        if (roleIdList.contains("6")) {
	        	pageCustomer = customerQueryCountService.findDailyCount(paramsMap);
				return pageCustomer;
			}
	        paramsMap.put("ownerId", user.getAdminUserId());
	        if (roleIdList.contains("5")) {
	        	List<Region> userRegionList = adminUserRegionService.findRegionByAdminUserId(user.getAdminUserId());
				paramsMap.put("regionList", userRegionList);
			}
	        pageCustomer = customerQueryCountService.findDailyCount(paramsMap);
			
		} catch (Exception e) {
			logger.error("CustomerQueryCountController.class [findDailyCount()] :error"+e);
		}
		
		return pageCustomer;
	}
	@RequestMapping("/monthCount")
	public Page<CustomerQueryCount> findMonthCount(HttpServletRequest request){
		logger.info("月别统计");
		Page<CustomerQueryCount> pageCustomer = null;
		try {
			
			int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
			int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
			String companyName = request.getParameter("companyName");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			Map<String, Object> paramsMap = Maps.newHashMap();
			paramsMap.put(Constant.PAGE_SIZE,pageSize);
			paramsMap.put(Constant.PAGE,currentPage);
			if (StringUtils.isNotBlank(beginDate)) {
				paramsMap.put("beginDate", beginDate);
			}
			if (StringUtils.isNotBlank(endDate)) {
				paramsMap.put("endDate", endDate);
			}
			if(StringUtils.isNotBlank(companyName)){
				paramsMap.put("companyName",companyName);
			}
//			AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
			AdminUser user = customeSession.getUser(request);
			List<String> roleIdList = adminUserService.findRoleIdByAdminUserId(user.getAdminUserId());
			if (roleIdList.contains("6")) {
				pageCustomer = customerQueryCountService.findMonthCount(paramsMap);
				return pageCustomer;
			}
			paramsMap.put("ownerId", user.getAdminUserId());
			if (roleIdList.contains("5")) {
				List<Region> userRegionList = adminUserRegionService.findRegionByAdminUserId(user.getAdminUserId());
				paramsMap.put("regionList", userRegionList);
			}
			pageCustomer = customerQueryCountService.findMonthCount(paramsMap);
			
		} catch (Exception e) {
			logger.error("CustomerQueryCountController.class [findMonthCount()] :error"+e);
		}
		
		return pageCustomer;
	}
	@RequestMapping("/exportMonthReport")
	public ResponseEntity<byte[]> exportMonthReport(HttpServletRequest request,HttpServletResponse response){
		logger.info("导出月别统计报表");
		try {
			String companyName = request.getParameter("companyName");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			Map<String, Object> paramsMap = Maps.newHashMap();
			String title = "";
			if (StringUtils.isNotBlank(beginDate)) {
				paramsMap.put("beginDate", beginDate);
				title = beginDate;
			}
			if (beginDate!=null&&endDate!=null&&!beginDate.equals(endDate)) {
				if (StringUtils.isNotBlank(endDate)) {
					paramsMap.put("endDate", endDate);
					if (title.equals("")) {
						title = endDate+"及之前";
					}else{
						title += "至"+endDate;
					}
				}else{
					if(!title.equals("")){
						title+="至今";
					}
				}
			}
			if(StringUtils.isNotBlank(companyName)){
				paramsMap.put("companyName",companyName);
				title += "部分";
			}
			AdminUser user = customeSession.getUser(request);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			String str = user.getAdminUserId().toString()+(new Date()).getTime();
			headers.setContentDispositionFormData("attachment", str+".xlsx");
			List<String> roleIdList = adminUserService.findRoleIdByAdminUserId(user.getAdminUserId());
			if (roleIdList.contains("6")) {
				if(!StringUtils.isNotBlank(companyName)){
					paramsMap.put("companyName",companyName);
					title += "全部";
				}
				List<CustomerQueryCount> dataList = customerQueryCountService.exportMonthReport(paramsMap);
				PoiUtil poiUtil = new PoiUtil();
				Workbook workbook = poiUtil.monthReportExport(title, dataList);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
				return responseEntity;
			}
			if(!StringUtils.isNotBlank(companyName)){
				paramsMap.put("companyName",companyName);
				title += "部分";
			}
			paramsMap.put("ownerId", user.getAdminUserId());
			if (roleIdList.contains("5")) {
				List<Region> userRegionList = adminUserRegionService.findRegionByAdminUserId(user.getAdminUserId());
				paramsMap.put("regionList", userRegionList);
			}
			List<CustomerQueryCount> dataList = customerQueryCountService.exportMonthReport(paramsMap);
			PoiUtil poiUtil = new PoiUtil();
			Workbook workbook = poiUtil.monthReportExport(title, dataList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
			return responseEntity;
		} catch (Exception e) {
			logger.error("CustomerQueryCountController.class [exportMonthReport()] :error"+e);
		}
		return null;
	}
	@RequestMapping("/exportDailyReport")
	public ResponseEntity<byte[]> exportDailyReport(HttpServletRequest request,HttpServletResponse response){
		logger.info("导出日别统计报表");
		try {
			String companyName = request.getParameter("companyName");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			Map<String, Object> paramsMap = Maps.newHashMap();
			String title = "";
			if (StringUtils.isNotBlank(beginDate)) {
				paramsMap.put("beginDate", beginDate);
				title = beginDate;
			}
			if (beginDate!=null&&endDate!=null&&!beginDate.equals(endDate)) {
				if (StringUtils.isNotBlank(endDate)) {
					paramsMap.put("endDate", endDate);
					if (title.equals("")) {
						title = endDate+"及之前";
					}else{
						title += "至"+endDate;
					}
				}else{
					if(!title.equals("")){
						title+="至今";
					}
				}
			}
			if(StringUtils.isNotBlank(companyName)){
				paramsMap.put("companyName",companyName);
				title += "部分";
			}
			AdminUser user = customeSession.getUser(request);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			String str = user.getAdminUserId().toString()+(new Date()).getTime();
			headers.setContentDispositionFormData("attachment", str+".xlsx");
			List<String> roleIdList = adminUserService.findRoleIdByAdminUserId(user.getAdminUserId());
			if (roleIdList.contains("6")) {
				if(!StringUtils.isNotBlank(companyName)){
					paramsMap.put("companyName",companyName);
					title += "全部";
				}
				List<CustomerQueryCount> dataList = customerQueryCountService.exportDailyReport(paramsMap);
				PoiUtil poiUtil = new PoiUtil();
				Workbook workbook = poiUtil.dailyReportExport(title, dataList);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				workbook.write(bos);
				ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
				return responseEntity;
			}
			if(!StringUtils.isNotBlank(companyName)){
				paramsMap.put("companyName",companyName);
				title += "部分";
			}
			paramsMap.put("ownerId", user.getAdminUserId());
			if (roleIdList.contains("5")) {
				List<Region> userRegionList = adminUserRegionService.findRegionByAdminUserId(user.getAdminUserId());
				paramsMap.put("regionList", userRegionList);
			}
			List<CustomerQueryCount> dataList = customerQueryCountService.exportDailyReport(paramsMap);
			PoiUtil poiUtil = new PoiUtil();
			Workbook workbook = poiUtil.dailyReportExport(title, dataList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bos.toByteArray(),headers,HttpStatus.CREATED);
			return responseEntity;
		} catch (Exception e) {
			logger.error("CustomerQueryCountController.class [exportMonthReport()] :error"+e);
		}
		return null;
	}
}
