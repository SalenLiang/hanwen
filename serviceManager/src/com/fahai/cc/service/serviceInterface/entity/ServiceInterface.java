package com.fahai.cc.service.serviceInterface.entity;

import org.apache.ibatis.type.Alias;
import org.aspectj.util.GenericSignature.TypeSignature;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 接口实体类
 */
@Alias("serviceInterface")
public class ServiceInterface {

    @Id
    private Integer interfaceId;
    private String interfaceCode;   //接口代码
    private String interfaceName;   //接口名
    private String interfaceURL;    //接口地址url
    private String testURL;         //测试接口url
    private String manualURL;       //接口描述url
    private String description;     //描述
    private Date lastModifyDate;    //最后修改日期
    private Integer status;         //状态
    
    private boolean selectYN;
    
    private Integer modelId;
    
    private Integer customRange;
    
    private Integer actionUserId;//用户id
    
    private String actionType;//操作类型
    
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


	public Integer getCustomRange() {
		return customRange;
	}


	public void setCustomRange(Integer customRange) {
		this.customRange = customRange;
	}


	public Integer getModelId() {
		return modelId;
	}


	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}


	public boolean isSelectYN() {
		return selectYN;
	}
    

	public void setSelectYN(boolean selectYN) {
		this.selectYN = selectYN;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Integer interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceURL() {
        return interfaceURL;
    }

    public void setInterfaceURL(String interfaceURL) {
        this.interfaceURL = interfaceURL;
    }

    public String getTestURL() {
        return testURL;
    }

    public void setTestURL(String testURL) {
        this.testURL = testURL;
    }

    public String getManualURL() {
        return manualURL;
    }

    public void setManualURL(String manualURL) {
        this.manualURL = manualURL;
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
}
