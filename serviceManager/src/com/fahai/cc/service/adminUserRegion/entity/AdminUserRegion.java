package com.fahai.cc.service.adminUserRegion.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * 管理用户与系统用户中间表实体类
 */
public class AdminUserRegion {

    @Id
    private Integer adminUserRegionId;
    @Id
    private Integer adminUserId;
    @Id
    private String regionCode;
    
    private Integer logId;
    
    private String actionType;
    
    private Integer actionUserId;
    
    private Date createDate;
    
    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(Integer actionUserId) {
		this.actionUserId = actionUserId;
	}

	public Integer getAdminUserRegionId() {
        return adminUserRegionId;
    }

    public void setAdminUserRegionId(Integer adminUserRegionId) {
        this.adminUserRegionId = adminUserRegionId;
    }

    public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
}
