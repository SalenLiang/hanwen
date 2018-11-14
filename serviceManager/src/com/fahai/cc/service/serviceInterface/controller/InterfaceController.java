package com.fahai.cc.service.serviceInterface.controller;

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
import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.serviceInterface.service.InterfaceService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.HttpClientUtil;
import com.fahai.cc.service.util.HttpUtil;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/interface")
public class InterfaceController {

    private Logger logger = LoggerFactory.getLogger(InterfaceController.class);

    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private CustomeSession customeSession;


    @RequestMapping("/findByPage")
    public Page<ServiceInterface> findByPage(HttpServletRequest request){
    	logger.info("查询接口列表");
    	Page<ServiceInterface> page = null;
    	try {
    		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
    		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
    		String interfaceName = request.getParameter("interfaceName");
    		String interfaceCode = request.getParameter("interfaceCode");
    		String status = request.getParameter("status")==null?"0":request.getParameter("status");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put(Constant.PAGE_SIZE,pageSize);
    		paramsMap.put(Constant.PAGE,currentPage);
    		paramsMap.put("status",status);
    		if(StringUtils.isNotBlank(interfaceCode)){
    			paramsMap.put("interfaceCode",interfaceCode);
    		}
    		if(StringUtils.isNotBlank(interfaceName)){
    			paramsMap.put("interfaceName",interfaceName);
    		}
    		page = interfaceService.findByPage(paramsMap);
			
		} catch (Exception e) {
			logger.error("InterfaceController.class [findByPage() :error]+e");
		}
        return page;
    }

    @RequestMapping("/save")
    public Map<String,Object> save(ServiceInterface serviceInterface,HttpServletRequest request){
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            logger.info("保存接口");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            serviceInterface.setActionUserId(user.getAdminUserId());
            serviceInterface.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
            interfaceService.save(serviceInterface);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("InterfaceController.class [save()] :error"+e);
        }

        return resultMap;
    }

    @RequestMapping("/delete/{interfaceId}")
    public Map<String,Object> delete(@PathVariable Integer interfaceId,HttpServletRequest request){
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            logger.info("删除接口");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            interfaceService.delete(interfaceId,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("InterfaceController.class [delete()] :error"+e);
        }
        return resultMap;
    }

    @RequestMapping("/update")
    public Map<String,Object> update(ServiceInterface serviceInterface,HttpServletRequest request){
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            logger.info("修改接口");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            serviceInterface.setActionUserId(user.getAdminUserId());
            serviceInterface.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
            interfaceService.update(serviceInterface);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("InterfaceController.class [update()] :error"+e);
        }
        return resultMap;
    }
    @RequestMapping("/findAllValid")
    public Map<String, Object> findAllValid(){
    	Map<String, Object> resultMap = Maps.newHashMap();
    	
    	 try{
             logger.info("修改接口");
             List<ServiceInterface> interfaceList = interfaceService.findAllValid();
             resultMap.put("interfaceList", interfaceList);
             resultMap.put(Constant.ERROR_CODE,"0");
         }catch (Exception e){
             resultMap.put(Constant.ERROR_CODE,"1");
             logger.error("InterfaceController.class [findAllValid()] :error"+e);
         }
         return resultMap;
    }

    @RequestMapping("/debugUrl")
    public Map<String,Object> debugUrl(String debugUrl){
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            logger.info("debugUrl");
            String result = HttpClientUtil.query(debugUrl, 20*60*1000);
            resultMap.put(Constant.ERROR_CODE,"0");
            resultMap.put(Constant.DATA,result);
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("InterfaceController.class [debugUrl()] :error"+e);
        }
        return resultMap;
    }
    @RequestMapping("/debugInterfaceUrl")
    public Map<String,Object> debugInterfaceUrl(String testUrl){
    	Map<String, Object> resultMap = Maps.newHashMap();
    	try{
    		logger.info("debugUrl");
    		String result = HttpUtil.doGet(testUrl, null);
    		if (result==null || ("").equals(result)) {
				result = "1";//接口异常
			}
    		resultMap.put(Constant.ERROR_CODE,"0");
    		resultMap.put(Constant.DATA,result);
    	}catch (Exception e){
    		resultMap.put(Constant.ERROR_CODE,"1");
    		logger.error("InterfaceController.class [debugInterfaceUrl()] :error"+e);
    	}
    	return resultMap;
    }

    @RequestMapping("/checkInterfaceCode/{interfaceCode}")
    public Map<String,Object> checkInterfaceCode(@PathVariable String interfaceCode){
    	logger.info("检测接口是否存在");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            logger.info("checkInterfaceCode");
            Long count = interfaceService.checkInterfaceCode(interfaceCode);
            if(count > 0){
                resultMap.put(Constant.CHECK_RESULT,"1");
            }else{
                resultMap.put(Constant.CHECK_RESULT,"0");
            }
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("InterfaceController.class [checkInterfaceCode()] :error"+e);
        }
        return resultMap;
    }


}
