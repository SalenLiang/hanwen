package com.fahai.cc.service.vo;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.region.entity.Region;
import com.fahai.cc.service.role.entity.UserRole;

import org.springframework.data.annotation.Transient;
import java.util.List;

/**
 * 用户和区域包装类
 */
public class AdminRegionVo {

    private AdminUser adminUser;
    private List<Region> adminUserRegion;
    private List<UserRole> userRoleList;

    @Transient
    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public List<Region> getAdminUserRegion() {
        return adminUserRegion;
    }

    public void setAdminUserRegion(List<Region> adminUserRegion) {
        this.adminUserRegion = adminUserRegion;
    }
}
