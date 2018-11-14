
package com.fahai.cc.service.interfacemodel.entity;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InterfaceModel {
	private Integer modelId;
	private String modelName;
	private String description;
	private Integer status;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastModifyDate;
	
	private boolean selectYN;
	
	private Integer logId;
	
	private String actionType;
	
	private Integer actionUserId;
	
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

	public boolean isSelectYN() {
		return selectYN;
	}

	public void setSelectYN(boolean selectYN) {
		this.selectYN = selectYN;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	@Override
	public String toString() {
		return "InterfaceModel [modelId=" + modelId + ", modelName=" + modelName + ", description=" + description
				+ ", status=" + status + ", lastModifyDate=" + lastModifyDate + "]";
	}
	
	
	
	
}
