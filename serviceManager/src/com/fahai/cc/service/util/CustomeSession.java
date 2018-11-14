package com.fahai.cc.service.util;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.fahai.cc.service.adminUser.entity.AdminUser;

public class CustomeSession {
	
	private RedisTemplate<String, Object> redisTemplate;

	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	//保存用户到自定义session中30m
	public void saveRefreshSession(AdminUser user){
		String key = "custom_session"+user.getAdminUserId();
		redisTemplate.opsForValue().set(key, user);
		redisTemplate.expire(key, 30, TimeUnit.MINUTES);
	}
	
	//设置cookie刷新过期时间
	public void settingCookie(HttpServletResponse response,String id){
		Cookie cookie = new Cookie("csessionid", id);
//		cookie.setMaxAge(30*60);//30m
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public AdminUser getUser(HttpServletRequest request){
		String userId= "";
		Cookie[] cookies = request.getCookies();
		if (cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if ("csessionid".equals(cookie.getName())) {
					userId=cookie.getValue();
				}
			}
		}
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		return (AdminUser) redisTemplate.opsForValue().get("custom_session"+userId);
	}
	public void destorySession(HttpServletRequest request){
		String userId= "";
		Cookie[] cookies = request.getCookies();
		if (cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if ("csessionid".equals(cookie.getName())) {
					userId=cookie.getValue();
				}
			}
		}
		if (StringUtils.isNoneBlank(userId)) {
			redisTemplate.opsForValue().set("custom_session"+userId, null);
		}
	}
	
}
