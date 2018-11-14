package com.fahai.cc.service.limitword.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.apache.bcel.classfile.ConstantNameAndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.limitword.entity.Limitword;
import com.fahai.cc.service.limitword.service.LimitwordService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/limitword")
public class LimitwordController {
	
	private Logger logger = LoggerFactory.getLogger(LimitwordController.class);

	@Autowired
	private LimitwordService limitwordService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private CustomeSession customeSession;
	
	@RequestMapping("/save")
	public Map<String, Object> saveLimitword(Limitword limitword,HttpServletRequest request){
		logger.info("新增限定词");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
//			AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
			AdminUser user = customeSession.getUser(request);
			limitword.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
			limitword.setActionUserId(user.getAdminUserId());
			Limitword saveLimitword = limitwordService.saveLimitword(limitword);
			//缓存限定词
			if (limitword.getStatus() == 0) {
				List<String> limitwordList = (List<String>) redisTemplate.opsForValue().get(Constant.REDIS_LIMITWORD+limitword.getWordType());
				if (limitwordList == null) {
					limitwordList = new ArrayList<>();
				}
				limitwordList.add(limitword.getWord());
				redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+limitword.getWordType(), limitwordList);
			}
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "保存限定词失败");
			logger.error("LimitwordController.class [saveLimitword()] :error"+e.getMessage());
		}
		return resultMap;
	}
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public Map<String, Object> updateLimitword(Limitword limitword,HttpServletRequest request){
		logger.info("更新限定词列表");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
//			AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
			AdminUser user = customeSession.getUser(request);
			limitword.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
			limitword.setActionUserId(user.getAdminUserId());
			Limitword oldLimitword = limitwordService.getLimitwordById(limitword.getLimitwordId());
			limitwordService.updateLimitword(limitword);
			
			if(oldLimitword.getStatus() != limitword.getStatus() || !oldLimitword.getWordType().equals(limitword.getWordType())){
				List<String> oList = limitwordService.getUsableLimitword(oldLimitword.getWordType());
				if (oList != null) {
					redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+oldLimitword.getWordType(), oList);
				}
				List<String> nList = limitwordService.getUsableLimitword(limitword.getWordType());
				if (nList != null) {
					redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+limitword.getWordType(), nList);
				}
			}
			
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "修改限定词失败");
			logger.error("LimitwordController.class [update()] :error"+e.getMessage());
		}
		return resultMap;
	}
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public Map<String, Object> deleteLimitword(Limitword limitword,HttpServletRequest request){
		logger.info("删除限定词");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
//			AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
			AdminUser user = customeSession.getUser(request);
			limitword.setActionUserId(user.getAdminUserId());
			limitword.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
			Limitword limitwordBean = limitwordService.deleteLimitword(limitword);
			if (limitwordBean.getStatus()==0) {
				List<String> limitwordList = (List<String>) redisTemplate.opsForValue().get(Constant.REDIS_LIMITWORD+limitwordBean.getWordType());
				if (limitwordList != null && limitwordList.size()>0) {
					for(int i=0;i<limitwordList.size();i++){
						if(limitwordList.get(i).equals(limitword.getWord())){
							limitwordList.remove(i);
							redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+limitwordBean.getWordType(), limitwordList);
							break;
						}
					}
				}
			}
			
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "删除限定词失败");
			logger.error("LimitwordController.class [deleteLimitword()] :error"+e.getMessage());
		}
		return resultMap;
	}
	@RequestMapping(value="/checkLimitword",method=RequestMethod.POST)
	public Map<String, Object> checkLimitword(String word){
		logger.info("检测限定词是否存在");
		Map<String, Object> resultMap = Maps.newHashMap();
		try {
			Long count = limitwordService.checkLimitword(word);
			if (count>0) {
				resultMap.put(Constant.CHECK_RESULT, "1");
			}else{
				resultMap.put(Constant.CHECK_RESULT, "0");
			}
			resultMap.put(Constant.ERROR_CODE, "0");
		} catch (Exception e) {
			resultMap.put(Constant.ERROR_CODE, "1");
			resultMap.put(Constant.ERROR_MSG, "验证限定词失败");
			logger.error("LimitwordController.class [checkLimitword()] :error"+e.getMessage());
		}
		return resultMap;
	}
	@RequestMapping(value="/find",method=RequestMethod.POST)
	public Page<Limitword> find(HttpServletRequest request){
		logger.info("查询限定词列表");
		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
		String word = request.getParameter("word");
		String wordType = request.getParameter("wordType");
		Map<String, Object> paramsMap = Maps.newHashMap();
		paramsMap.put(Constant.PAGE_SIZE,pageSize);
		paramsMap.put(Constant.PAGE,currentPage);
		paramsMap.put("word",word);
		paramsMap.put("wordType", wordType);
		Page<Limitword> ilimitwordPage = limitwordService.findByPage(paramsMap);
		return ilimitwordPage;
	}
}
