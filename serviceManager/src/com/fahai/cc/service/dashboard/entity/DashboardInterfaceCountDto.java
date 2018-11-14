package com.fahai.cc.service.dashboard.entity;

public class DashboardInterfaceCountDto {
	
	private Integer interfaceId;
	
	private String interfaceURL;
	
	private String interfaceName;//接口名称
	
	private Long qsAmount;//接口某天的使用量
	
	private String heartStatus;
	
	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getInterfaceURL() {
		return interfaceURL;
	}

	public void setInterfaceURL(String interfaceURL) {
		this.interfaceURL = interfaceURL;
	}

	public String getHeartStatus() {
		return heartStatus;
	}

	public void setHeartStatus(String heartStatus) {
		this.heartStatus = heartStatus;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Long getQsAmount() {
		return qsAmount;
	}

	public void setQsAmount(Long qsAmount) {
		this.qsAmount = qsAmount;
	}

	@Override
	public String toString() {
		return "DashboardInterfaceCountDto [interfaceName=" + interfaceName + ", qsAmount=" + qsAmount + "]";
	}
	
}
