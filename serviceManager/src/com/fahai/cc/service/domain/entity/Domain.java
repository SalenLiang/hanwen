package com.fahai.cc.service.domain.entity;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 领域实体类
 */
@Alias("domain")
public class Domain {

    @Id
    private Integer domainId;
    private String domainCode;
    private String domainName;
    private Integer status;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastModifyDate;
    
    private boolean selectYN;
    
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

	public boolean isSelectYN() {
		return selectYN;
	}

	public void setSelectYN(boolean selectYN) {
		this.selectYN = selectYN;
	}

	private List<Dimension> dimensionList = new ArrayList<Dimension>();
    
    public List<Dimension> getDimensionList() {
		return dimensionList;
	}

	public void setDimensionList(List<Dimension> dimensionList) {
		this.dimensionList = dimensionList;
	}

	public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
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

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "domainId=" + domainId +
                ", domainCode='" + domainCode + '\'' +
                ", domainName='" + domainName + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", lastModifyDate=" + lastModifyDate +
                ", selectYN=" + selectYN +
                ", dimensionList=" + dimensionList +
                '}';
    }
}
