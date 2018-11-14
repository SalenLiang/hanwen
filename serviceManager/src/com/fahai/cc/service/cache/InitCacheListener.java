package com.fahai.cc.service.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fahai.cc.interf.mysql.entity.CustomerInformation;
import com.fahai.cc.interf.mysql.entity.Dimension;
import com.fahai.cc.service.customer.service.CustomerService;
import com.fahai.cc.service.dimension.service.DimensionService;
import com.fahai.cc.service.field.service.FieldService;
import com.fahai.cc.service.limitword.service.LimitwordService;
import com.fahai.cc.service.util.Constant;
@Component
public class InitCacheListener implements ApplicationListener<ContextRefreshedEvent> {
	//启动服务器初始化redis缓存
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private DimensionService dimensionService;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private LimitwordService limitwordService;
	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		List<CustomerInformation> customs = customerService.getCustomerList();
		
		List<CustomerInformation> temps = null;
		for(CustomerInformation cc : customs){
			temps  = customerService.getCustomerinfor(Integer.parseInt(cc.getCustomerId()));
		
			redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMERINFORMATION_KEY+cc.getAuthCode(), temps);
			//用户的信息，customerId, authCode ,companyId,status
			redisTemplate.opsForValue().set(Constant.REDIS_CUSTOMER_KEY+cc.getAuthCode(), cc);
		}
		
		List<Dimension> dimensions = dimensionService.getDimensionList();
		List<com.fahai.cc.interf.mysql.entity.Field> fields = null;
		for(Dimension dim:dimensions){
			fields = fieldService.getDimensionField(dim.getDimensionId());
			//保存维度的字段
			redisTemplate.opsForValue().set(Constant.REDIS_DIMENSIONFIELDS_KEY+dim.getDimensionCode(), fields);
			//保存维度的字段
			redisTemplate.opsForValue().set(Constant.REDIS_DIMENSION_KEY+dim.getDimensionCode(), dim);
		}
		
		List<String> generalList = limitwordService.getUsableLimitword(Constant.LIMITWORD_GENERAL);
		if (generalList != null && generalList.size()>0) {
			redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+Constant.LIMITWORD_GENERAL, generalList);
		}
		
		List<String> qList = limitwordService.getUsableLimitword(Constant.LIMITWORD_Q);
		if (qList != null && qList.size()>0) {
			redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+Constant.LIMITWORD_Q, qList);
		}
		
		List<String> pnameList = limitwordService.getUsableLimitword(Constant.LIMITWORD_PNAME);
		if (pnameList != null && pnameList.size()>0){
			redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+Constant.LIMITWORD_PNAME, pnameList);
		}
		
	}

}
