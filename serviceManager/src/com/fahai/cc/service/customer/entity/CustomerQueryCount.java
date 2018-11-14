package com.fahai.cc.service.customer.entity;

import java.io.Serializable;
import java.util.Date;

public class CustomerQueryCount implements Serializable {
	
	private String companyName;
	
	private String interfaceCode;
	
	private String searchType;
	
	private int totalSU;//查询的总量，即：成功的总量
	
	private int totalQG;//查得的量
	
	private int totalDI;//去重的量
	
	private int totalFA;//失败的量
	
	private Date queryTime;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public int getTotalSU() {
		return totalSU;
	}

	public void setTotalSU(int totalSU) {
		this.totalSU = totalSU;
	}

	public int getTotalQG() {
		return totalQG;
	}

	public void setTotalQG(int totalQG) {
		this.totalQG = totalQG;
	}

	public int getTotalDI() {
		return totalDI;
	}

	public void setTotalDI(int totalDI) {
		this.totalDI = totalDI;
	}

	public int getTotalFA() {
		return totalFA;
	}

	public void setTotalFA(int totalFA) {
		this.totalFA = totalFA;
	}

	public Date getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	@Override
	public String toString() {
		return "CustomerQueryCount [companyName=" + companyName + ", interfaceCode=" + interfaceCode + ", searchType="
				+ searchType + ", totalSU=" + totalSU + ", totalQG=" + totalQG + ", totalDI=" + totalDI + ", totalFA="
				+ totalFA + ", queryTime=" + queryTime + "]";
	}

}
