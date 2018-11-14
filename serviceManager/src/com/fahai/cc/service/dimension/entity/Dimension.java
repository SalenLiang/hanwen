package com.fahai.cc.service.dimension.entity;

import com.fahai.cc.service.field.entity.Field;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 维度实体类
 */
@Alias("dimension")
public class Dimension {
    private Integer dimensionId;
    private Integer domainId;
    private String  domainName;
    private String dimensionCode;
    private String dimensionName;
    private Integer status;
    private String description;
    private String indexName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifyDate;
    
    private List<Field> fieldList = new ArrayList<>();
    
    private boolean selectYN;
    
    private String targetCluster;
    
    private String targetDimensionCode;
    
    private Integer logId;
    
    private Integer actionUserId;
    
    private String actionType;
    
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

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public boolean isSelectYN() {
		return selectYN;
	}

	public void setSelectYN(boolean selectYN) {
		this.selectYN = selectYN;
	}

	public List<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getDimensionCode() {
        return dimensionCode;
    }

    public void setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
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
        return "Dimension{" +
                "dimensionId=" + dimensionId +
                ", domainId=" + domainId +
                ", dimensionCode='" + dimensionCode + '\'' +
                ", dimensionName='" + dimensionName + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", lastModifyDate=" + lastModifyDate +
                '}';
    }
}
