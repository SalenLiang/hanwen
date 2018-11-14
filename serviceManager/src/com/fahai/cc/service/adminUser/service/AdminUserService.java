package com.fahai.cc.service.adminUser.service;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.vo.AdminRegionVo;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

public interface AdminUserService {

    /**
     * 分页查找系统用户
     * @param paramMap  查询条件
     */
    Page<AdminUser> findPageAdminUser(Map<String, Object> paramMap);

    void saveUser(AdminUser adminUser);

    void updateUser(AdminUser adminUser);

    void saveUser(String adminUser,String regionCode,String roleIds, Integer adminUserId) throws MessagingException;

    void updateUser(String adminUser,String regionCode,String roleIds, Integer adminUserId);

    List<AdminUser> findAdminUser();

    List<AdminUser> findUnChargeAdminUser();

    List<AdminUser> getUserByUserName(String userName);

    AdminRegionVo findByAdminId(String adminId) throws Exception;

    Long checkAdminUserName(String adminUserName);

    void insertUserLoginLog(AdminUser adminUser);

    Long checkAdminUser(AdminUser adminUser, String checkType) throws Exception;

    List<AdminUser> getUserByUserNameAndPassword(String userName, String password);

	void updateUserPassword(AdminUser adminUser) throws MessagingException;

	void delete(String adminUserStr, Integer adminUserId);

	void activationUser(String adminUserStr, Integer adminUserId) throws MessagingException;
	
	List<String> findRoleIdByAdminUserId(Integer adminUserId);

	void updateUserPassword(AdminUser adminUser, String newPSW) throws MessagingException;

	void updatePassword(AdminUser user, String newPassword) throws MessagingException;

}
