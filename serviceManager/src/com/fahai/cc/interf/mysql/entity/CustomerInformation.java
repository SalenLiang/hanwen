/**  
 * Copyright © 2017鼎泰智源科技有限公司. All rights reserved.
 *
 * @Title: CustomerInfromation.java
 * @Prject: InterfaceWeb
 * @Package: com.fahai.cc.interf.mysql.entity
 * @Description: TODO
 * @author: Aaron.ye  
 * @date: 2017年6月7日 上午10:53:27
 * @version: V1.0  
 */
package com.fahai.cc.interf.mysql.entity;

import java.io.Serializable;

/**
 * @ClassName: CustomerInformation
 * @Description: TODO
 * @author: Aaron.ye
 * @date: 2017年6月7日 上午10:53:27 
 */
public class CustomerInformation implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -5423204880776531486L;
	
	private String customerId;
	private String domainCode;
	private String dimensionCode;
	private String authCode;
	private String companyId;
	private String interfaceCode;
	private String interfaceURL;
	private String targetCluster;
	private String targetDimensionCode;//真实的维度
	private String fieldCode;
	private String username;
	private String modelId;
	private int status;
	private String detialfieldCode;
	private String listfieldCode;
	private String trialBeginDate;
	private String trialEndDate;
	private String contractBeginDate;
	private String contractEndDate;
	private int  qpmLimit;
	private int customRange;
	private String customQuery;
	private String ipWhiteList;
	private int trialQuantity;
	private int totalSearchQuantity;
	private String useCache;
	private String useLimitWord;
	public String getUseLimitWord() {
		return useLimitWord;
	}
	public void setUseLimitWord(String useLimitWord) {
		this.useLimitWord = useLimitWord;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getDimensionCode() {
		return dimensionCode;
	}
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getInterfaceCode() {
		return interfaceCode;
	}
	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}
	public String getInterfaceURL() {
		return interfaceURL;
	}
	public void setInterfaceURL(String interfaceURL) {
		this.interfaceURL = interfaceURL;
	}
	public String getTargetCluster() {
		return targetCluster;
	}
	public void setTargetCluster(String targetCluster) {
		this.targetCluster = targetCluster;
	}
	public String getTargetDimensionCode() {
		return targetDimensionCode;
	}
	public void setTargetDimensionCode(String targetDimensionCode) {
		this.targetDimensionCode = targetDimensionCode;
	}
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDetialfieldCode() {
		return detialfieldCode;
	}
	public void setDetialfieldCode(String detialfieldCode) {
		this.detialfieldCode = detialfieldCode;
	}
	public String getListfieldCode() {
		return listfieldCode;
	}
	public void setListfieldCode(String listfieldCode) {
		this.listfieldCode = listfieldCode;
	}
	public String getTrialBeginDate() {
		return trialBeginDate;
	}
	public void setTrialBeginDate(String trialBeginDate) {
		this.trialBeginDate = trialBeginDate;
	}
	public String getTrialEndDate() {
		return trialEndDate;
	}
	public void setTrialEndDate(String trialEndDate) {
		this.trialEndDate = trialEndDate;
	}
	public String getContractBeginDate() {
		return contractBeginDate;
	}
	public void setContractBeginDate(String contractBeginDate) {
		this.contractBeginDate = contractBeginDate;
	}
	public String getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public int getQpmLimit() {
		return qpmLimit;
	}
	public void setQpmLimit(int qpmLimit) {
		this.qpmLimit = qpmLimit;
	}
	public int getCustomRange() {
		return customRange;
	}
	public void setCustomRange(int customRange) {
		this.customRange = customRange;
	}
	public String getCustomQuery() {
		return customQuery;
	}
	public void setCustomQuery(String customQuery) {
		this.customQuery = customQuery;
	}
	public String getIpWhiteList() {
		return ipWhiteList;
	}
	public void setIpWhiteList(String ipWhiteList) {
		this.ipWhiteList = ipWhiteList;
	}
	public int getTrialQuantity() {
		return trialQuantity;
	}
	public void setTrialQuantity(int trialQuantity) {
		this.trialQuantity = trialQuantity;
	}
	public int getTotalSearchQuantity() {
		return totalSearchQuantity;
	}
	public void setTotalSearchQuantity(int totalSearchQuantity) {
		this.totalSearchQuantity = totalSearchQuantity;
	}
	public String getUseCache() {
		return useCache;
	}
	public void setUseCache(String useCache) {
		this.useCache = useCache;
	}
	@Override
	public String toString() {
		return "CustomerInformation [customerId=" + customerId + ", domainCode=" + domainCode + ", dimensionCode="
				+ dimensionCode + ", authCode=" + authCode + ", companyId=" + companyId + ", interfaceCode="
				+ interfaceCode + ", interfaceURL=" + interfaceURL + ", targetCluster=" + targetCluster
				+ ", targetDimensionCode=" + targetDimensionCode + ", fieldCode=" + fieldCode + ", username=" + username
				+ ", modelId=" + modelId + ", status=" + status + ", detialfieldCode=" + detialfieldCode
				+ ", listfieldCode=" + listfieldCode + ", trialBeginDate=" + trialBeginDate + ", trialEndDate="
				+ trialEndDate + ", contractBeginDate=" + contractBeginDate + ", contractEndDate=" + contractEndDate
				+ ", qpmLimit=" + qpmLimit + ", customRange=" + customRange + ", customQuery=" + customQuery
				+ ", ipWhiteList=" + ipWhiteList + ", trialQuantity=" + trialQuantity + ", totalSearchQuantity="
				+ totalSearchQuantity + ", useCache=" + useCache + "]";
	}
	
	
	
}
