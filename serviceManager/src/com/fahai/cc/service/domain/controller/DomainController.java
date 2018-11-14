package com.fahai.cc.service.domain.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.domain.service.DomainService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/domain")
public class DomainController {

    private Logger logger = LoggerFactory.getLogger(DomainController.class);
    @Autowired
    private DomainService domainService;
    
    @Autowired
    private CustomeSession customeSession;

    @RequestMapping("/findDomainByPage")
    public Page<Domain> findDomainByPage(HttpServletRequest request){
    	logger.info("查询领域列表");
    	Page<Domain> domainPage = null;
    	try {
    		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
    		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
    		String domainCode = request.getParameter("domainCode");
    		String domainName = request.getParameter("domainName");
    		String status = request.getParameter("status")==null?"0":request.getParameter("status");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put(Constant.PAGE_SIZE,pageSize);
    		paramsMap.put(Constant.PAGE,currentPage);
    		paramsMap.put("status",status);
    		if(StringUtils.isNotBlank(domainCode)){
    			paramsMap.put("domainCode",domainCode);
    		}
    		if(StringUtils.isNotBlank(domainName)){
    			paramsMap.put("domainName",domainName);
    		}
    		domainPage = domainService.findByPage(paramsMap);
			
		} catch (Exception e) {
			logger.error("DomainController.class [findDomainByPage() :error]+e");
		}
        return domainPage;
    }

    @RequestMapping("/save")
    public Map<String,Object> saveDomain(Domain domain,HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        System.out.println(domain.getStatus());
        logger.info("保存领域");
        try {
        	if(domainService.checkDomainCode(domain.getDomainCode())>0){
        		resultMap.put(Constant.ERROR_CODE,"2");
        	}else{
//        		AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        		AdminUser user = customeSession.getUser(request);
        		domain.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
        		domain.setActionUserId(user.getAdminUserId());
	            domainService.save(domain);
	            resultMap.put(Constant.ERROR_CODE,"0");
        	}
        }catch (Exception e){
        	e.printStackTrace();
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("保存领域出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }

    @RequestMapping("/delete")
    public Map<String,Object> deleteDomain(Domain domain,HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        logger.info("删除领域");
        try {
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
        	domain.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
        	domain.setActionUserId(user.getAdminUserId());
            domainService.delete(domain);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("删除领域出现异常",e.fillInStackTrace());
            return resultMap;
        }
        return resultMap;
    }

    @RequestMapping("/update")
    public Map<String,Object> updateDomain(Domain domain,HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        logger.info("修改领域");
        try {
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
        	domain.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
        	domain.setActionUserId(user.getAdminUserId());
            domainService.update(domain);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("修改领域出现异常",e.fillInStackTrace());
            return resultMap;
        }
        return resultMap;
    }

    @RequestMapping("/checkDomainCode/{domainCode}")
    public Map<String,Object> checkDomainCode(@PathVariable String domainCode){
        Map<String,Object> resultMap = Maps.newHashMap();
        //System.out.println("%%%%%%%%%%%%%%%%"+domainCode);
        try {
            logger.info("校验区域代码唯一性");
            Long count = domainService.checkDomainCode(domainCode);
            if(count > 0){
                resultMap.put(Constant.CHECK_RESULT,"1");
            }else{
                resultMap.put(Constant.CHECK_RESULT,"0");
            }
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("校验区域代码出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/findAll")
    public Map<String,Object> findAll(){
        Map<String,Object> resultMap = Maps.newHashMap();
        logger.info("查询所有的领域");
        try {
            List<Domain> domainList = domainService.getAllValidDomain();
            resultMap.put(Constant.ERROR_CODE,"0");
            resultMap.put(Constant.DATA_LIST,domainList);
        }catch (Exception e){
            logger.error("校验区域代码出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }
    
    @RequestMapping("/findAllDomain")
    public Map<String, Object> findAllDomain(){
    	 Map<String,Object> resultMap = Maps.newHashMap();
    	 logger.info("查询所有的领域维度字段");
         try {
             List<Domain> domainList = domainService.findAllDomain();
             resultMap.put(Constant.ERROR_CODE,"0");
             resultMap.put("domainList",domainList);
         }catch (Exception e){
             logger.error("获取领域维度字段代码出现异常",e.fillInStackTrace());
             resultMap.put(Constant.ERROR_CODE,"1");
         }
         return resultMap;
    }
    @RequestMapping("/findAllDomainDimension")
    public Map<String, Object> findAllDomainDimension(){
    	 Map<String,Object> resultMap = Maps.newHashMap();
    	 logger.info("查询所有的领域纬度");
         try {
             List<Domain> domainList = domainService.findAllDomainDimension();
             resultMap.put(Constant.ERROR_CODE,"0");
             resultMap.put("domainList",domainList);
         }catch (Exception e){
             logger.error("获取领域维度代码出现异常",e.fillInStackTrace());
             resultMap.put(Constant.ERROR_CODE,"1");
         }
         return resultMap;
    }

}
