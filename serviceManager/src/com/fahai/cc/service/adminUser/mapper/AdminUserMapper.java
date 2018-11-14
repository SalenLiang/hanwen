package com.fahai.cc.service.adminUser.mapper;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.vo.AdminRegionVo;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/10.
 */
public interface AdminUserMapper {

    Long getTotalCount(Map<String, Object> paramMap);

    List<AdminUser> findPageAdminUser(Map<String, Object> paramMap);

    List<AdminUser> findAdminUser(HashMap<String, String> paramsMap);

    void updateStatusById(Map<String, Object> map);

    void updateAdminUser(AdminUser adminUser);

    List<AdminUser> findUnChargeAdminUser(HashMap<String, String> paramsMap);

    List<AdminUser> getUserByUserName(String userName);

    AdminRegionVo findByAdminId(String adminId);

    void saveAdminUser(AdminUser adminUser);

    Long findCount(String adminUserName);

    Long findCountByParam(HashMap<String, Object> paramMap);

    void insertUserLoginLog(AdminUser adminUser);

    List<AdminUser> getUserByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

	void saveAdminUserLog(AdminUser adminUser);
	
	List<String> findRoleIdByAdminUserId(Integer adminUserId);
}
