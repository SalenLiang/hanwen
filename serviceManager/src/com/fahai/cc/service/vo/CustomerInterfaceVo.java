package com.fahai.cc.service.vo;

import java.io.Serializable;

public class CustomerInterfaceVo implements Serializable {
	
	private Integer interfaceId;
    private String interfaceName;   //接口名称
    
    private Integer modelId;
    
    private String modelName;//模型名称
    
    private Integer customRange;//每页条数
    
    private String customQuery;//自定义查询

	public String getCustomQuery() {
		return customQuery;
	}

	public void setCustomQuery(String customQuery) {
		this.customQuery = customQuery;
	}

	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Integer getCustomRange() {
		return customRange;
	}

	public void setCustomRange(Integer customRange) {
		this.customRange = customRange;
	}

	@Override
	public String toString() {
		return "CustomerInterfaceVo [interfaceId=" + interfaceId + ", interfaceName=" + interfaceName + ", modelId="
				+ modelId + ", modelName=" + modelName + ", customRange=" + customRange + ", customQuery=" + customQuery
				+ "]";
	}

}
