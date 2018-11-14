package com.fahai.cc.service.serviceInterface.service;

import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;
import com.fahai.cc.service.util.Page;

import java.util.List;
import java.util.Map;

public interface InterfaceService {

    void save(ServiceInterface serviceInterface);

    void delete(Integer interfaceId,Integer adminUserId);

    Page<ServiceInterface> findByPage(Map<String, Object> paramMap);

    void update(ServiceInterface serviceInterface);

	List<ServiceInterface> findAllValid();

    Long checkInterfaceCode(String interfaceCode);

}
