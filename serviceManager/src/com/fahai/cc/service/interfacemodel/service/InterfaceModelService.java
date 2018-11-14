package com.fahai.cc.service.interfacemodel.service;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.interfacemodel.entity.InterfaceModel;
import com.fahai.cc.service.util.Page;

public interface InterfaceModelService {

	Page<InterfaceModel> fingByPageInterfaceModel(Map<String, Object> paramsMap);

	List<Domain> getModelDomainDimension(Integer modelId);

	List<InterfaceModel> findAll();
	
	long checkModel(InterfaceModel interfaceModel, String checkType) throws Exception;

	long domainYN(Integer domainId);

	long dimensionYN(Integer dimensionId);

	void saveInterfaceModel(String interfaceModel, String addDomainDimension, Integer adminUserId);

	InterfaceModel saveEditInterfaceModel(String interfaceModel, String editDomainDimension, Integer adminUserId);

	void deleteInterfaceModel(InterfaceModel interfaceModel);
	
}
