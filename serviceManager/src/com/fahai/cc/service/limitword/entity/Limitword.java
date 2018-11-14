package com.fahai.cc.service.limitword.entity;

import java.io.Serializable;
import java.util.Date;

public class Limitword implements Serializable {
	
	private Integer limitwordId;
	
	private String word;
	
	private String wordType;
	
	private Integer status;//0启用  1停用
	
	private String description;
	
	private Date createDate;
	
	private Date lastModifyDate;
	
	private Integer actionUserId;
	
	private String actionType;

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

	public Integer getLimitwordId() {
		return limitwordId;
	}

	public void setLimitwordId(Integer limitwordId) {
		this.limitwordId = limitwordId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWordType() {
		return wordType;
	}

	public void setWordType(String wordType) {
		this.wordType = wordType;
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

	@Override
	public String toString() {
		return "Limitword [limitwordId=" + limitwordId + ", word=" + word + ", wordType=" + wordType + ", status="
				+ status + ", description=" + description + ", createDate=" + createDate + ", lastModifyDate="
				+ lastModifyDate + "]";
	}
	
}
