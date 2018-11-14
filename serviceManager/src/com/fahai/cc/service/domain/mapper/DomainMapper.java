package com.fahai.cc.service.domain.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fahai.cc.service.domain.entity.Domain;

public interface DomainMapper {

    void saveDomain(Domain domain);

    void updateDomain(Domain domain);

    long getTotalCount(Map<String, Object> paramMap);

    List<Domain> findPageDomain(Map<String, Object> paramMap);

    Long findCount(HashMap<String, String> map);

    List<Domain> getAllDomain(HashMap<String,Object> paramMap);

	List<Domain> findAllDomain();

	List<Domain> findAllDomainDimension();

	void saveDomainLog(Domain domain);
}
