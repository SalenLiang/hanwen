package com.fahai.cc.service.serviceInterface.mapper;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.serviceInterface.entity.ServiceInterface;

public interface InterfaceMapper {

    long getTotalCount(Map<String, Object> paramMap);

    List<ServiceInterface> findPageDomain(Map<String, Object> paramMap);

    void saveInterface(ServiceInterface serviceInterface);

    void updateInterface(ServiceInterface serviceInterface);

	List<ServiceInterface> findAllValid();

    Long checkInterfaceCode(String interfaceCode);

	void saveInterfaceLog(ServiceInterface serviceInterface);
}
