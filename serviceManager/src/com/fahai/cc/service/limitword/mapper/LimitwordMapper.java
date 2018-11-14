package com.fahai.cc.service.limitword.mapper;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.limitword.entity.Limitword;

public interface LimitwordMapper {

	void saveLimitword(Limitword limitword);

	long getTotalCount(Map<String, Object> paramsMap);

	List<Limitword> findPageLimitword(Map<String, Object> paramsMap);

	void updateLimitword(Limitword limitword);

	void deleteLimitword(Integer limitwordId);

	Long checkLimitword(String word);

	List<String> getUsableLimitword(String wordType);

	Limitword getLimitwordById(Integer limitwordId);

	void saveLimitwordLog(Limitword limitword);

}
