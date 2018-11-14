package com.fahai.cc.service.adminUser.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.type.Alias;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统管理人员实体类
 */
@Alias("adminUser")
public class AdminUser implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4614037356945057346L;
	@Id
    private Integer adminUserId;
    private String name;
    private String realName;    //真实姓名
    private String email;
    private String passwd;
    private Integer status;
    private String mobile;
    private Integer regionYN;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastModifyDate;
    private List<String> menuMast;
    
    private String loginIp;
    
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

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public List<String> getMenuMast() {
		return menuMast;
	}

	public void setMenuMast(List<String> menuMast) {
		this.menuMast = menuMast;
	}

	public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRegionYN() {
        return regionYN;
    }

    public void setRegionYN(Integer regionYN) {
        this.regionYN = regionYN;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
