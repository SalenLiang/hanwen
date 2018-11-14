package com.fahai.cc.service.field.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 字段实体类
 */
@Alias("field")
public class Field {
    //字段ID
    private Integer fieldId;
    //所属维度ID
    private Integer dimensionId;
    //字段代码
    private String fieldCode;
    //字段类型
    private String fieldType;
    //字段名
    private String fieldName;
    //状态
    private Integer status;
    //描述
    private String description;
    //最后修改日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifyDate;
    
    //列表页是否默认选中
    private Integer defaultListShowYN;  //0未选中  1选中
    //详细页是否默认选中
    private Integer defaultDetailShowYN;//0未选中  1选中
    //是否支持搜索
    private Integer searchYN; //0不支持  1支持
    
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

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Integer getDefaultListShowYN() {
		return defaultListShowYN;
	}

	public void setDefaultListShowYN(Integer defaultListShowYN) {
		this.defaultListShowYN = defaultListShowYN;
	}

	public Integer getDefaultDetailShowYN() {
		return defaultDetailShowYN;
	}

	public void setDefaultDetailShowYN(Integer defaultDetailShowYN) {
		this.defaultDetailShowYN = defaultDetailShowYN;
	}

	public Integer getSearchYN() {
		return searchYN;
	}

	public void setSearchYN(Integer searchYN) {
		this.searchYN = searchYN;
	}

	private boolean selectYN;
    

    public boolean isSelectYN() {
		return selectYN;
	}

	public void setSelectYN(boolean selectYN) {
		this.selectYN = selectYN;
	}

	public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
        return "Field{" +
                "fieldId=" + fieldId +
                ", dimensionId=" + dimensionId +
                ", fieldCode='" + fieldCode + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", lastModifyDate=" + lastModifyDate +
                '}';
    }
}
