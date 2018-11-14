package com.fahai.cc.service.domain.service;


import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.util.Page;

import java.util.List;
import java.util.Map;

public interface DomainService {

    void save(Domain domain);

    void update(Domain domain);

    Page<Domain> findByPage(Map<String,Object> param);

    Long checkDomainCode(String domainCode);

    //所有有效的领域
    List<Domain> getAllValidDomain();

	List<Domain> findAllDomain();

	List<Domain> findAllDomainDimension();

	void delete(Domain domain);

}
