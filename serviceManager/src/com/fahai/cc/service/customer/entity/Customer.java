package com.fahai.cc.service.customer.entity;

import java.util.Date;

import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author hzw
 * @description
 */
@Alias("customer")
public class Customer {

	private Integer customerId;//客户id
	
	private String authCode;//客户授权码
	
	private String customerArea;//客户所属区域
	
	private Integer status;//默认0是正常状态客户，1为删除的客户
	
	private String userName;//初始管理账号
	
	private String companyId;//公司id
	
	private String companyName;//公司名称
	
	private String contactName;//联系人名称
	
	private String contactTel;//联系电话
	
	private String contactPhone;//手机号
	
	private String contactEmail;//Email
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date trialBeginDate;//试用开始时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date trialEndDate;//试用结束时间
	
	private Integer trialQuantity;//试用数量
	
	private Integer totalSearchQuantity;//总查询数量
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date contractBeginDate;//合同开始时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date contractEndDate;//合同结束时间
	
	private String regionCode;
	
	private String ipWhiteList;//ip白名单
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;//创建时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date lastModifyDate;//最后修改时间
	
	private Integer qpmLimit;//每分钟查询次数的限制
	
	private Integer useCache;//0使用缓存1不使用缓存
	
	private Integer useLimitWord;//0使用限定词1不使用限定词
	
	private Integer ownerId;//负责人id
	
	private Integer createAdminUserId;
	
	private Integer logId;
	
	private Integer actionUserId;
	
	private String actionType;
	
	public Integer getUseLimitWord() {
		return useLimitWord;
	}
	public void setUseLimitWord(Integer useLimitWord) {
		this.useLimitWord = useLimitWord;
	}
	public Integer getUseCache() {
		return useCache;
	}
	public void setUseCache(Integer useCache) {
		this.useCache = useCache;
	}
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public Integer getActionUserId() {
		return actionUserId;
	}
	public void setActionUserId(Integer actionUserId) {
		this.actionUserId = actionUserId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getQpmLimit() {
		return qpmLimit;
	}
	public void setQpmLimit(Integer qpmLimit) {
		this.qpmLimit = qpmLimit;
	}
	public Integer getCreateAdminUserId() {
		return createAdminUserId;
	}
	public void setCreateAdminUserId(Integer createAdminUserId) {
		this.createAdminUserId = createAdminUserId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public Date getTrialBeginDate() {
		return trialBeginDate;
	}
	public void setTrialBeginDate(Date trialBeginDate) {
		this.trialBeginDate = trialBeginDate;
	}
	public Date getTrialEndDate() {
		return trialEndDate;
	}
	public void setTrialEndDate(Date trialEndDate) {
		this.trialEndDate = trialEndDate;
	}
	public Integer getTrialQuantity() {
		return trialQuantity;
	}
	public void setTrialQuantity(Integer trialQuantity) {
		this.trialQuantity = trialQuantity;
	}
	public Integer getTotalSearchQuantity() {
		return totalSearchQuantity;
	}
	public void setTotalSearchQuantity(Integer totalSearchQuantity) {
		this.totalSearchQuantity = totalSearchQuantity;
	}
	public Date getContractBeginDate() {
		return contractBeginDate;
	}
	public void setContractBeginDate(Date contractBeginDate) {
		this.contractBeginDate = contractBeginDate;
	}
	public Date getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getIpWhiteList() {
		return ipWhiteList;
	}
	public void setIpWhiteList(String ipWhiteList) {
		this.ipWhiteList = ipWhiteList;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public String getCustomerArea() {
		return customerArea;
	}
	public void setCustomerArea(String customerArea) {
		this.customerArea = customerArea;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
