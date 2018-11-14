package com.fahai.cc.service.settingTemplet.entity;

import java.io.Serializable;
import java.util.Date;

public class SettingTemplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2864997442654894085L;
	
	private Integer logId;
	
	private Integer templetId;
	
	private String templetName;
	
	private Integer status;
	
	private String description;
	
	private Date createDate;
	
	private Date lastModifyDate;
	
	private Integer actionUserId;

	private String actionType;
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getTempletId() {
		return templetId;
	}

	public void setTempletId(Integer templetId) {
		this.templetId = templetId;
	}

	public String getTempletName() {
		return templetName;
	}

	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(Integer actionUserId) {
		this.actionUserId = actionUserId;
	}

	@Override
	public String toString() {
		return "SettingTemplet [templetId=" + templetId + ", templetName=" + templetName + ", status=" + status
				+ ", description=" + description + ", createDate=" + createDate + ", lastModifyDate=" + lastModifyDate
				+ ", actionUserId=" + actionUserId + "]";
	}

}
