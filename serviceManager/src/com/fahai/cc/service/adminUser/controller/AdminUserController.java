package com.fahai.cc.service.adminUser.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.service.AdminUserService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Encrypt;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.vo.AdminRegionVo;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/adminUser")
public class AdminUserController {

    private Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private CustomeSession customeSession;

    @RequestMapping(value = "/find",method = RequestMethod.POST)
    public Page<AdminUser> findUserByPage(HttpServletRequest request){
    	logger.info("查询用户列表");
        int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
        int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
        String name = request.getParameter("name");
        String status = request.getParameter("status")==null?"0":request.getParameter("status");
        Map<String, Object> paramsMap = Maps.newHashMap();
        paramsMap.put(Constant.PAGE_SIZE,pageSize);
        paramsMap.put(Constant.PAGE,currentPage);
        paramsMap.put("status",status);
        if(StringUtils.isNotBlank(name)){
            paramsMap.put("name",name);
        }
        Page<AdminUser> pageAdminUser = adminUserService.findPageAdminUser(paramsMap);
        return pageAdminUser;
    }

    @RequestMapping("/save")
    public Map<String,Object> saveUser(AdminUser adminUser){
    	logger.info("新增用户");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            adminUserService.saveUser(adminUser);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"保存系统用户失败");
            logger.error("保存系统用户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }

    @RequestMapping("/update")
    public Map<String,Object> updateUser(AdminUser adminUser){
    	logger.info("更新用户");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            adminUserService.updateUser(adminUser);
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"修改系统用户失败");
            logger.error("修改系统用户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }

    @RequestMapping("/delete")
    public Map<String,Object> deleteUser(String adminUserStr,HttpServletRequest request){
    	logger.info("删除用户");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
            adminUserService.delete(adminUserStr,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            resultMap.put(Constant.ERROR_MSG,"删除系统用户失败");
            logger.error("删除系统用户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }

    @RequestMapping("/findAdminUser")
    public List<AdminUser> findAdminUser(){
    	logger.info("查询所有用户");
        List<AdminUser> adminUsers = adminUserService.findAdminUser();
        return adminUsers;
    }

    @RequestMapping("/findByAdminId/{adminId}")
    public Map<String, Object> findByAdminId(@PathVariable String adminId){
    	logger.info("查找id为"+adminId+"的用户");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            AdminRegionVo adminRegionVo = adminUserService.findByAdminId(adminId);
            if(adminRegionVo != null){
                resultMap.put(Constant.DATA,adminRegionVo);
                resultMap.put(Constant.ERROR_CODE,"0");
            }else{
                logger.warn("根据系统用户ID未能查询到系统用户");
                resultMap.put(Constant.ERROR_CODE,"1");
            }
        }catch (Exception e){
            logger.error("根据系统用户ID查询出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/activationUser")
    public Map<String, Object> activationUser(String adminUserStr,HttpServletRequest request){
    	logger.info("激活用户");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
//        	AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        	AdminUser user = customeSession.getUser(request);
            adminUserService.activationUser(adminUserStr,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("激活用户出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/checkAdminUserName")
    public Map<String, Object> checkAdminUserName( AdminUser adminUser,String checkType){
    	logger.info("检测用户名是否存在");
        Map<String, Object> resultMap = Maps.newHashMap();
        try{
            Long count = adminUserService.checkAdminUser(adminUser,checkType);
            if(count > 0){
                resultMap.put(Constant.CHECK_RESULT,"1");
            }else{
                resultMap.put(Constant.CHECK_RESULT,"0");
            }
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("校验用户唯一性出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/findUnChargeAdminUser")
    public List<AdminUser> findUnChargeAdminUser(){
        List<AdminUser> adminUsers = adminUserService.findUnChargeAdminUser();
        return adminUsers;
    }

    @RequestMapping("/saveEditUser")
    public Map<String,Object> saveEditUser(HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String adminUser = request.getParameter("adminUser");
        String regionCode = request.getParameter("regionCode");
        String roleIds = request.getParameter("roleIds");
        try{
            logger.info("修改系统用户");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            adminUserService.updateUser(adminUser,regionCode,roleIds,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("修改系统用户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }
    @RequestMapping("/saveUser")
    public Map<String,Object> saveUser(HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String adminUser = request.getParameter("adminUser");
        String regionCode = request.getParameter("regionCode");
        String roleIds = request.getParameter("roleIds");
        try{
            logger.info("保存系统用户");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            adminUserService.saveUser(adminUser,regionCode,roleIds,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            if(e instanceof MailSendException){
                resultMap.put(Constant.ERROR_CODE,"2");
                resultMap.put(Constant.ERROR_MSG,"发送邮件失败，请检查电子邮箱名是否正确");
            }
            logger.error("修改系统用户出现异常",e.fillInStackTrace());
        }
        return resultMap;
    }
    @RequestMapping("/updatePassword")
    public Map<String,Object> updatePassword(HttpServletRequest request){
    	HashMap<String, Object> resultMap = Maps.newHashMap();
    	String oldPassword = request.getParameter("oldPassword");
    	String newPassword = request.getParameter("newPassword");
    	try{
    		logger.info("修改用户密码");
    		AdminUser user = customeSession.getUser(request);
    		if (!user.getPasswd().equals(Encrypt.SHA256(oldPassword))) {
				resultMap.put(Constant.CHECK_RESULT, "0");
				resultMap.put(Constant.ERROR_MSG, "原密码错误");
				return resultMap;
			}
    		adminUserService.updatePassword(user,newPassword);
    		resultMap.put(Constant.ERROR_CODE,"1");
    		resultMap.put(Constant.ERROR_MSG, "修改密码成功，请重新登录");
    	}catch (Exception e){
    		resultMap.put(Constant.ERROR_CODE,"1");
    		if(e instanceof MailSendException){
    			resultMap.put(Constant.ERROR_CODE,"2");
    			resultMap.put(Constant.ERROR_MSG,"发送邮件失败，请检查电子邮箱名是否正确");
    		}
    		logger.error("修改系统用户密码出现异常",e.fillInStackTrace());
    	}
    	return resultMap;
    }
}
