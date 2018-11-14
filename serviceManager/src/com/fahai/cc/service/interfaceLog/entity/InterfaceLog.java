package com.fahai.cc.service.interfaceLog.entity;

import java.io.Serializable;
import java.util.Date;

public class InterfaceLog implements Serializable {
	
	private Date baseYMD;
	
	private String ettType;
	
	private Integer customerId;
	
	private String companyName;
	
	private String interfaceName;
	
	private String interfaceCode;
	
	private String searchType;
	
	private Long total;
	
	private Date ettTime;

	public Date getBaseYMD() {
		return baseYMD;
	}

	public void setBaseYMD(Date baseYMD) {
		this.baseYMD = baseYMD;
	}

	public String getEttType() {
		return ettType;
	}

	public void setEttType(String ettType) {
		this.ettType = ettType;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Date getEttTime() {
		return ettTime;
	}

	public void setEttTime(Date ettTime) {
		this.ettTime = ettTime;
	}

	@Override
	public String toString() {
		return "InterfaceLog [baseYMD=" + baseYMD + ", ettType=" + ettType + ", customerId=" + customerId
				+ ", companyName=" + companyName + ", interfaceName=" + interfaceName + ", interfaceCode="
				+ interfaceCode + ", searchType=" + searchType + ", total=" + total + ", ettTime=" + ettTime + "]";
	}


	
}
