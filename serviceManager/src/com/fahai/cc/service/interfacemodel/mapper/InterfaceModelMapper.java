package com.fahai.cc.service.interfacemodel.mapper;

import java.util.List;
import java.util.Map;

import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.interfacemodel.entity.InterfaceModel;

public interface InterfaceModelMapper {

	Long getTotalCount(Map<String, Object> paramsMap);

	List<InterfaceModel> findPageIngerfaceModel(Map<String, Object> paramsMap);

	int saveInterfaceModel(InterfaceModel model);

	void saveInterfaceModelDomain(Map<Object, Object> newMap);

	void saveInterfaceModelDimension(Map<Object, Object> newMap);

	void updateInterfaceModel(InterfaceModel interfaceModel);

	List<Domain> getModelDomainDimension(Integer modelId);

	void deleteDomain(Integer modelId);

	void deleteDimension(Integer modelId);

	List<InterfaceModel> findAll();

	long findModelByParam(Map<String, Object> newMap);

	long getCount(Map<Object, Object> newMap);

	long domainYN(Integer domainId);

	long dimensionYN(Integer dimensionId);

	void saveInterfaceModelLog(InterfaceModel model);

	void saveInterfaceModelDimensionLog(Map<Object, Object> newMap);

	
}
