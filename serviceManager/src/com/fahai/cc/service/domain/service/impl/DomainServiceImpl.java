package com.fahai.cc.service.domain.service.impl;

import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.domain.mapper.DomainMapper;
import com.fahai.cc.service.domain.service.DomainService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.Page;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DomainServiceImpl implements DomainService {

    private Logger logger = LoggerFactory.getLogger(DomainServiceImpl.class);

    @Autowired
    private DomainMapper domainMapper;

    @Override
    public void save(Domain domain) {
        logger.info("保存区域开始");
        domain.setLastModifyDate(new Date());
        domainMapper.saveDomain(domain);
        //记录日志
        domainMapper.saveDomainLog(domain);
    }

    @Override
    public void delete(Domain domain) {
        domain.setStatus(1);
        domain.setLastModifyDate(new Date());
        domainMapper.updateDomain(domain);
        //记录日志
        domainMapper.saveDomainLog(domain);
    }

    @Override
    public void update(Domain domain) {
        domain.setLastModifyDate(new Date());
        domainMapper.updateDomain(domain);
        //记录日志
        domainMapper.saveDomainLog(domain);
    }

    @Override
    public Page<Domain> findByPage(Map<String, Object> paramMap) {
        long totalCount = domainMapper.getTotalCount(paramMap);
        int pageSize = (int) paramMap.get(Constant.PAGE_SIZE);
        if(totalCount == 0l){
            return new Page<>(1,0,pageSize,null);
        }
        int currentPage = (int) paramMap.get(Constant.PAGE);
        int start = (currentPage-1)*pageSize;
        paramMap.put(Constant.PAGE_START,start);
        List<Domain> domainList = domainMapper.findPageDomain(paramMap);
        Page<Domain> page = new Page<>();
        page.setTotalCount((int)totalCount);
        page.setList(domainList);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public Long checkDomainCode(String domainCode) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("domainCode",domainCode);
        Long count = domainMapper.findCount(map);
        return count;
    }

    @Override
    public List<Domain> getAllValidDomain() {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("status",0);
        return domainMapper.getAllDomain(map);
    }

	@Override
	public List<Domain> findAllDomain() {
		
		return domainMapper.findAllDomain();
	}

	@Override
	public List<Domain> findAllDomainDimension() {
		
		return domainMapper.findAllDomainDimension();
	}

}
