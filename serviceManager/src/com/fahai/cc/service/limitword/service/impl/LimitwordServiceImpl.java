package com.fahai.cc.service.limitword.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.limitword.entity.Limitword;
import com.fahai.cc.service.limitword.mapper.LimitwordMapper;
import com.fahai.cc.service.limitword.service.LimitwordService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.Page;
@Service
public class LimitwordServiceImpl implements LimitwordService {

	@Autowired
	private LimitwordMapper limitwordMapper;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public Limitword saveLimitword(Limitword limitword) {
		
		Date date = new Date();
		limitword.setCreateDate(date);
		limitword.setLastModifyDate(date);
		
		limitwordMapper.saveLimitword(limitword);
		//记录日志
		limitwordMapper.saveLimitwordLog(limitword);
		
		return limitword;
	}

	@Override
	public Page<Limitword> findByPage(Map<String, Object> paramsMap) {
		long totalCount = limitwordMapper.getTotalCount(paramsMap);
		int pageSize = (int) paramsMap.get(Constant.PAGE_SIZE);
		if (totalCount < 1) {
			return new Page<Limitword>(1, 0,pageSize, null);
		}
		
		int currentPage = (int) paramsMap.get(Constant.PAGE);
		
		int start = (currentPage-1)*pageSize;
		
		paramsMap.put(Constant.PAGE_START, start);
		
		List<Limitword> limitwordList = limitwordMapper.findPageLimitword(paramsMap);
		
		Page<Limitword> page = new Page<>();
		page.setCurrentPage(currentPage);
		page.setPageSize(pageSize);
		page.setTotalCount((int)totalCount);
		page.setList(limitwordList);
		
		return page;
	}

	@Override
	public void updateLimitword(Limitword limitword) {
		limitword.setLastModifyDate(new Date());
		
		Limitword oldLimitword = limitwordMapper.getLimitwordById(limitword.getLimitwordId());
		//记录日志
		limitwordMapper.saveLimitwordLog(limitword);
		
		if(oldLimitword.getStatus() != limitword.getStatus() || !oldLimitword.getWordType().equals(limitword.getWordType())){
			limitwordMapper.updateLimitword(limitword);
			List<String> oList = limitwordMapper.getUsableLimitword(oldLimitword.getWordType());
			if (oList != null) {
				redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+oldLimitword.getWordType(), oList);
			}
			
			List<String> nList = limitwordMapper.getUsableLimitword(limitword.getWordType());
			if (nList != null) {
				redisTemplate.opsForValue().set(Constant.REDIS_LIMITWORD+limitword.getWordType(), nList);
			}
		}
	}

	@Override
	public Limitword deleteLimitword(Limitword limitword) {
		limitwordMapper.deleteLimitword(limitword.getLimitwordId());
		//记录日志
		limitword.setLastModifyDate(new Date());
		limitwordMapper.saveLimitwordLog(limitword);
		return limitword;
	}

	@Override
	public Long checkLimitword(String word) {
		
		return limitwordMapper.checkLimitword(word);
	}

	@Override
	public List<String> getUsableLimitword(String wordType) {
		
		return limitwordMapper.getUsableLimitword(wordType);
	}

	@Override
	public Limitword getLimitwordById(Integer limitwordId) {
		
		return limitwordMapper.getLimitwordById(limitwordId);
	}
	
}
