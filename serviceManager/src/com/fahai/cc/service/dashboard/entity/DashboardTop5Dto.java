package com.fahai.cc.service.dashboard.entity;

public class DashboardTop5Dto {
	
	private String companyName;//公司名称
	
	private long dayAmount;//查询量
	
	private Double scoreValue;//占比
	
	public Double getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(Double scoreValue) {
		this.scoreValue = scoreValue;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(long dayAmount) {
		this.dayAmount = dayAmount;
	}

	@Override
	public String toString() {
		return "DashboardTop5Dto [companyName=" + companyName + ", dayAmount=" + dayAmount + ", scoreValue="
				+ scoreValue + "]";
	}

	
}
