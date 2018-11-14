package com.fahai.cc.service.login;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fahai.cc.service.adminUser.controller.AdminUserController;
import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.util.CustomeSession;

public class LoginInterceptor implements HandlerInterceptor{
	private Logger logger = LoggerFactory.getLogger(AdminUserController.class);
	
	@Autowired
	private CustomeSession customeSession;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		
		AdminUser user = customeSession.getUser(request);
		if (user == null) {
//			 没有登陆，转向登陆界面
			logger.error("没有权限"+request.getRequestURI());
			System.out.println("----------------没有权限-----------");			
			response.sendError(405,"405");
			return false;
		}
		logger.info("有权限:"+request.getRequestURI());
		
		//刷新session和cookie
		customeSession.saveRefreshSession(user);
		customeSession.settingCookie(response, user.getAdminUserId().toString());
		
		return true;
	}

}
