package com.fahai.cc.service.adminUser.entity;

import java.util.Date;

public class AdminUserLog {
    private Long logId;
    private Date logDate;
    private String logType;
    private Integer logAdminUserId;
    private Integer adminUserId;
    private String name;
    private String email;
    private String passwd;
    private String mobile;
    private Integer regionYN;
    private Date createDate;
    private Date lastModifyDate;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Integer getLogAdminUserId() {
        return logAdminUserId;
    }

    public void setLogAdminUserId(Integer logAdminUserId) {
        this.logAdminUserId = logAdminUserId;
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
}
