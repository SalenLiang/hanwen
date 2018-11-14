package com.fahai.cc.service.adminUser.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 系统用户登陆日志实体类
 */
public class AdminUserLoginLog {
    @Id
    private Long logId;
    //系统用户ID
    private String adminUserId;
    //登陆日期
    private Date loginDate;
    //登陆结果
    private int loginResult;
    //登陆IP
    private String loginIP;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public int getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(int loginResult) {
        this.loginResult = loginResult;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }
}
