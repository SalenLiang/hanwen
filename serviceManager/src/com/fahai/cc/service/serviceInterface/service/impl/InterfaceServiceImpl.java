package com.fahai.cc.service.serviceInterface.service.impl;

import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.serviceInterface.mapper.InterfaceMapper;
import com.fahai.cc.service.serviceInterface.service.InterfaceService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InterfaceServiceImpl implements InterfaceService {

    private Logger logger = LoggerFactory.getLogger(InterfaceServiceImpl.class);

    @Autowired
    private InterfaceMapper interfaceMapper;

    @Override
    public void save(ServiceInterface serviceInterface) {
        serviceInterface.setLastModifyDate(new Date());
        interfaceMapper.saveInterface(serviceInterface);
        //记录日志表
        interfaceMapper.saveInterfaceLog(serviceInterface);
    }

    @Override
    public void delete(Integer interfaceId,Integer adminUserId) {
        ServiceInterface serviceInterface = new ServiceInterface();
        serviceInterface.setInterfaceId(interfaceId);
        serviceInterface.setStatus(2);
        serviceInterface.setActionUserId(adminUserId);
        serviceInterface.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
        serviceInterface.setLastModifyDate(new Date());
        interfaceMapper.updateInterface(serviceInterface);
        interfaceMapper.saveInterfaceLog(serviceInterface);
    }

    @Override
    public Page<ServiceInterface> findByPage(Map<String, Object> paramMap) {
        logger.info("进行分页查找接口列表");
        long totalCount = interfaceMapper.getTotalCount(paramMap);
        int pageSize = (int) paramMap.get(Constant.PAGE_SIZE);
        if(totalCount == 0l){
            return new Page<>(1,0,pageSize,null);
        }
        int currentPage = (int) paramMap.get(Constant.PAGE);
        int start = (currentPage-1)*pageSize;
        paramMap.put(Constant.PAGE_START,start);
        List<ServiceInterface> domainList = interfaceMapper.findPageDomain(paramMap);
        Page<ServiceInterface> page = new Page<>();
        page.setTotalCount((int)totalCount);
        page.setList(domainList);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public void update(ServiceInterface serviceInterface) {
        serviceInterface.setLastModifyDate(new Date());
        interfaceMapper.updateInterface(serviceInterface);
        interfaceMapper.saveInterfaceLog(serviceInterface);
    }

	@Override
	public List<ServiceInterface> findAllValid() {
		List<ServiceInterface> interfaceList = interfaceMapper.findAllValid();
		return interfaceList;
	}

    @Override
    public Long checkInterfaceCode(String interfaceCode) {

        return interfaceMapper.checkInterfaceCode(interfaceCode);
    }
}
