package com.fahai.cc.service.login.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.service.AdminUserLoginLogService;
import com.fahai.cc.service.adminUser.service.AdminUserService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Encrypt;
import com.fahai.cc.service.util.HttpClientUtil;
import com.google.common.collect.Maps;
import com.touclick.captcha.exception.TouclickException;
import com.touclick.captcha.model.Status;
import com.touclick.captcha.service.TouClick;

/**
 * 登录登出的控制controller
 */
@RestController
@RequestMapping("/serviceManager")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
    
    private TouClick touClick = new TouClick();
    
    private static final String PUBKEY = "aca3be26-8d80-42cf-952f-dcead942bb7b";
    private static final String PRIKEY = "baab09ff-c566-433d-b892-c492a9409002";

    @Autowired
    private AdminUserLoginLogService adminUserLoginLogService;

    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private CustomeSession customeSession;
    
   /* @RequestMapping(value = "/error")
    public String  error(){
    	return "redirect:/";
    }*/

    @RequestMapping("/login")
    public Map<String,Object> login(HttpServletRequest request,HttpServletResponse response, String userName, String password,String token,String checkAddress,String sid){
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
			Status status = touClick.check(checkAddress, sid, token, PUBKEY, PRIKEY);
			if (status != null && status.getCode()==0) {//验证通过
		        resultMap.put(Constant.ERROR_CODE,"1");
		        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password) ){
		            resultMap.put(Constant.ERROR_MSG,"用户名或密码为空");
		            return resultMap;
		        }
		        //使用sha256对密码进行加密
		        password = Encrypt.SHA256(password);
		        List<AdminUser> userList = adminUserService.getUserByUserNameAndPassword(userName,password);
		        if(userList == null || userList.size() ==0){
		            logger.error("登录用户不存在");
		            resultMap.put(Constant.ERROR_MSG,"用户名或密码错误");
		            return resultMap;
		        }
		        AdminUser user = userList.get(0);
		        if(user.getStatus() == null || user.getStatus() == 1){
		            logger.error("登录账号被锁定");
		            resultMap.put(Constant.ERROR_MSG,"登录账号被锁定,请联系管理员");
		            return resultMap;
		        }
		        //设置session和cookie
		       /* redisTemplate.opsForValue().set("custom_session"+user.getAdminUserId(),user);
		        Cookie cookie = new Cookie("csessionid", user.getAdminUserId().toString());
		        cookie.setMaxAge(30*60);//30m
		        cookie.setPath("/");
		        response.addCookie(cookie);*/
		        
		        customeSession.saveRefreshSession(user);
		        customeSession.settingCookie(response, user.getAdminUserId().toString());
		        
		        
		        //保存登录日志
		        user.setLoginIp(HttpClientUtil.getIpAddress(request));
		        adminUserService.insertUserLoginLog(user);
		        resultMap.put(Constant.ERROR_CODE,"0");
		        
		        logger.info("用户登录成功");
			}else{
				resultMap.put(Constant.ERROR_CODE, "1");
				resultMap.put(Constant.ERROR_MSG, "验证码错误");
			}
		} catch (TouclickException e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "验证码错误");
			logger.error("LoginController.class [login()] :error"+e.getMessage());
		}catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "系统错误");
			logger.error("LoginController.class [login()] :error"+e.getMessage());
		}
       
        return resultMap;
    }

    @RequestMapping("/login/logout")
    public Map<String,Object> logout(HttpServletRequest request){
        logger.info("登出方法开始执行");
        Map<String,Object> resultMap = Maps.newHashMap();
        try{
//        	request.getSession().invalidate();
        	customeSession.destorySession(request);
            resultMap.put(Constant.ERROR_CODE,"0");
            logger.info("登出系统成功");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("登出系统出现异常: " , e.fillInStackTrace());
            return resultMap;
        }
        return resultMap;
    }

    @RequestMapping("/login/checkLogin")
    public Map<String,Object> checkLogin(HttpServletRequest request){
        logger.info("校验是否是登录状态");
        Map<String,Object> resultMap = Maps.newHashMap();
        resultMap.put(Constant.ERROR_CODE,"0");
//        AdminUser adminUser = (AdminUser)request.getSession().getAttribute(Constant.USER_NAME_SESSION);
        AdminUser adminUser = customeSession.getUser(request);
        if (adminUser == null) {
        	resultMap.put(Constant.ERROR_CODE,"1");
            logger.info("未登录");
            return resultMap;
		}
        resultMap.put(Constant.CURRENT_USER,adminUser);
        logger.info("已登录");
        return resultMap;
    }
    /**
     * 
     * @param userName
     * @param userEmail
     * @return
     * @description 重置用户密码
     * @throws
     */
    @RequestMapping("/login/resetPassword")
    public Map<String,Object> resetPassword(String userName,String userEmail){
    	logger.info("重置密码");
    	Map<String,Object> resultMap = Maps.newHashMap();
    	resultMap.put(Constant.ERROR_CODE, "0");
    	try {
    		List<AdminUser> userList = adminUserService.getUserByUserName(userName);
    		if (userList == null || userList.size()<=0) {
    			resultMap.put(Constant.CHECK_RESULT, "0");
    			resultMap.put(Constant.ERROR_MSG, "用户不存在");
    			return resultMap;
    		}else{
    			if (!userList.get(0).getEmail().equals(userEmail)) {
    				resultMap.put(Constant.CHECK_RESULT, "0");
        			resultMap.put(Constant.ERROR_MSG, "邮箱不是初始注册邮箱");
        			return resultMap;
				}else{//重置密码
					adminUserService.updateUserPassword(userList.get(0));
					resultMap.put(Constant.CHECK_RESULT, "1");
					resultMap.put(Constant.ERROR_MSG, "重置密码成功，注意查看邮件");
				}
    		}
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "系统异常，请联系管理员");
			logger.error("LoginController.class [resetPassword()] :error"+e.getMessage());
		}
    	return resultMap;
    }
    
}
