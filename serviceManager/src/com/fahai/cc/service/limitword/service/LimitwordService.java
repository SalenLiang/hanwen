package com.fahai.cc.service.limitword.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.limitword.entity.Limitword;
import com.fahai.cc.service.util.Page;

public interface LimitwordService {

	Limitword saveLimitword(Limitword limitword);

	Page<Limitword> findByPage(Map<String, Object> paramsMap);

	void updateLimitword(Limitword limitword);

	Long checkLimitword(String word);

	List<String> getUsableLimitword(String wordType);

	Limitword deleteLimitword(Limitword limitword);

	Limitword getLimitwordById(Integer limitwordId);
}
