package com.fahai.cc.service.adminUser.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.mapper.AdminUserMapper;
import com.fahai.cc.service.adminUser.service.AdminUserService;
import com.fahai.cc.service.adminUserRegion.entity.AdminUserRegion;
import com.fahai.cc.service.adminUserRegion.mapper.AdminUserRegionMapper;
import com.fahai.cc.service.mail.EmailVo;
import com.fahai.cc.service.mail.SendMail;
import com.fahai.cc.service.role.entity.UserRole;
import com.fahai.cc.service.role.mapper.RoleMapper;
import com.fahai.cc.service.util.CommonUtil;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.Encrypt;
import com.fahai.cc.service.util.JsonUtil;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.vo.AdminRegionVo;
import com.google.common.collect.Maps;

@Service
public class AdminUserServiceImpl implements AdminUserService{

    private Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AdminUserRegionMapper adminUserRegionMapper;

    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private EmailVo emailVo;
    
    @Autowired
    private SendMail sendMail;

    @Override
    public Page<AdminUser> findPageAdminUser(Map<String, Object> paramMap) {
        long totalCount = adminUserMapper.getTotalCount(paramMap);
        int pageSize = (int) paramMap.get(Constant.PAGE_SIZE);

        if(totalCount == 0){
            return new Page<>(1,0,pageSize,null);
        }

        int currentPage = (int) paramMap.get(Constant.PAGE);
        int start = (currentPage-1)*pageSize;
        paramMap.put(Constant.PAGE_START,start);
        List<AdminUser> userList = adminUserMapper.findPageAdminUser(paramMap);

        Page<AdminUser> page = new Page<>();
        page.setTotalCount((int)totalCount);
        page.setList(userList);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public void saveUser(AdminUser adminUser) {

    }

    @Override
    public void updateUser(AdminUser adminUser) {

    }

    @Override
    public void saveUser(String adminUserJson, String regionCode,String roleIds, Integer adminUserId) throws MessagingException {
        AdminUser adminUser = JsonUtil.jsonToBean(adminUserJson, AdminUser.class);
        adminUser.setCreateDate(new Date());
        adminUser.setStatus(0);
        adminUser.setLastModifyDate(adminUser.getCreateDate());
        String password = CommonUtil.randomPassword(6); 
        //向用户的邮箱发送明文密码
        String context = "<h2>恭喜注册账号成功，您可以使用服务管理系统了<h2><br/><b>账号：</b>";
        context += adminUser.getName();
        context += "<br/><b>密码：</b>";
        context += password;
        context += "<br/><b>请妥善保管避免密码泄露</b>";
        
        emailVo.setEmailContent(context);
        String[] receivers = {adminUser.getEmail()};
        
        emailVo.setReceivers(receivers);
        emailVo.setSubject("注册服务平台账号成功");
        
        sendMail.sendEmail(emailVo);
        //保存密文密码到数据库中
        adminUser.setPasswd(Encrypt.SHA256(password));
        adminUserMapper.saveAdminUser(adminUser);
        //记录日志
        adminUser.setActionType(Constant.ACTIONTYPE_LOG_SAVE);
        adminUser.setActionUserId(adminUserId);
        adminUserMapper.saveAdminUserLog(adminUser);
        Integer regionYN = adminUser.getRegionYN();
        if(regionYN == 0){
            updateAdminUserRegion(regionCode,adminUser.getAdminUserId(),Constant.ACTIONTYPE_LOG_SAVE,
            						adminUserId,adminUser.getLogId());
        }
        //选取的角色列表
        List<UserRole> userRoleList =this.buildUserRoleList(roleIds,String.valueOf(adminUser.getAdminUserId()),"admin",
        													Constant.ACTIONTYPE_LOG_SAVE,adminUserId,adminUser.getLogId());
        roleMapper.saveUserRole(userRoleList);
        //记录日志
        roleMapper.saveUserRoleLog(userRoleList);
    }


    @Override
    public void updateUser(String adminUserJson, String regionCode,String roleIds,Integer adminUserId) {
        AdminUser adminUser = JsonUtil.jsonToBean(adminUserJson, AdminUser.class);
        adminUser.setLastModifyDate(new Date());
        Integer regionYN = adminUser.getRegionYN();
        adminUserMapper.updateAdminUser(adminUser);
        //记录日志
        adminUser.setActionType(Constant.ACTIONTYPE_LOG_UPDATE);
        adminUser.setActionUserId(adminUserId);
        adminUserMapper.saveAdminUserLog(adminUser);
        //清除自己的
        adminUserRegionMapper.deleteByAdminUserId(adminUser.getAdminUserId());
        if(regionYN == 0){
            updateAdminUserRegion(regionCode,adminUser.getAdminUserId(),
            						Constant.ACTIONTYPE_LOG_UPDATE,adminUserId,adminUser.getLogId());
        }
        //选取的角色列表
        List<UserRole> userRoleList =
                this.buildUserRoleList(roleIds,String.valueOf(adminUser.getAdminUserId()),"admin",
                						Constant.ACTIONTYPE_LOG_UPDATE,adminUserId,adminUser.getLogId());
        //先删除后插入
        roleMapper.deleteUserRole(String.valueOf(adminUser.getAdminUserId()));
        roleMapper.saveUserRole(userRoleList);
        //记录日志
        roleMapper.saveUserRoleLog(userRoleList);
    }

    /**
     * 构建UserRolelist用户批量插入
     * @param roleIds           角色ID数组
     * @param adminUserId       adminUserId
     * @param currentUserId     当前登录用户ID
     * @return                  UserRole集合
     */
    private List<UserRole> buildUserRoleList(String roleIds,String adminUserId,String currentUserId,String actionType,Integer actionUserId,Integer logId){
        String[] roleIdArray = roleIds.split(",");
        List<UserRole> userRoleList = new ArrayList<>(roleIdArray.length);
        for (int i = 0; i <roleIdArray.length ; i++) {
            UserRole userRole = new UserRole();
            userRole.setAdminUserId(adminUserId);
            userRole.setRoleId(roleIdArray[i]);
            userRole.setUseYn("Y");
            userRole.setRegisterDate(new Date());
            userRole.setRegisterId(currentUserId);
            //记录日志的字段
            userRole.setLogId(logId);
            userRole.setActionType(actionType);
            userRole.setActionUserId(actionUserId);
            userRole.setCreateDate(new Date());
            
            userRoleList.add(userRole);
        }
        return  userRoleList;
    }

    private void updateAdminUserRegion(String regionCode,Integer adminUserId,String actionType,Integer actionUserId,Integer logId){
        String[] regionCodes = regionCode.split(",");
        for (int i = 0; i < regionCodes.length; i++) {
            adminUserRegionMapper.deleteByRegionCode(regionCodes[i],adminUserId);
            AdminUserRegion adminUserRegion = new AdminUserRegion();
            adminUserRegion.setAdminUserId(adminUserId);
            adminUserRegion.setRegionCode(regionCodes[i]);
            adminUserRegionMapper.save(adminUserRegion);
            //记录日志
            adminUserRegion.setActionType(actionType);
            adminUserRegion.setActionUserId(actionUserId);
            adminUserRegion.setLogId(logId);
            adminUserRegion.setCreateDate(new Date());
            adminUserRegionMapper.saveAdminUserRegionLog(adminUserRegion);
        }
    }


    @Override
    public void delete(String adminUserStr, Integer adminUserId) {
        //假删除
    	AdminUser user = JsonUtil.jsonToBean(adminUserStr, AdminUser.class);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("adminUserId",user.getAdminUserId());
        map.put("status",Constant.DELETE_STATUS_CODE);
        adminUserMapper.updateStatusById(map);
        //记录日志
        user.setStatus(Constant.DELETE_STATUS_CODE);
        user.setActionType(Constant.ACTIONTYPE_LOG_DELETE);
        user.setActionUserId(adminUserId);
        user.setLastModifyDate(new Date());
        adminUserMapper.saveAdminUserLog(user);
    }

    @Override
    public List<AdminUser> findAdminUser() {
        //状态为正常的系统用户
        HashMap<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("status","0");
        return adminUserMapper.findAdminUser(paramsMap);
    }

    @Override
    public List<AdminUser> findUnChargeAdminUser() {
        //状态为正常的系统用户
        HashMap<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("status","0");
        return adminUserMapper.findUnChargeAdminUser(paramsMap);
    }

    @Override
    public List<AdminUser> getUserByUserName(String userName) {
        return adminUserMapper.getUserByUserName(userName);
    }


    @Override
    public AdminRegionVo findByAdminId(String adminUserId) throws Exception {
        AdminRegionVo adminRegionVo = adminUserMapper.findByAdminId(adminUserId);
        List<UserRole> userRoleList = roleMapper.findUserRoleList(adminUserId);
        adminRegionVo.setUserRoleList(userRoleList);
        return adminRegionVo;
    }

    @Override
    public void activationUser(String adminUserStr, Integer adminUserId) throws MessagingException {
    	AdminUser adminUser = JsonUtil.toBean(adminUserStr, AdminUser.class);
        adminUser.setLastModifyDate(new Date());
        adminUser.setStatus(Constant.NORMAL_STATUS_CODE);
        String password = CommonUtil.randomPassword(6); 
        //向用户的邮箱发送明文密码
        String context = "<h2>恭喜账号激活成功，您可以使用服务管理系统了<h2><br/><b>账号：</b>";
        context += adminUser.getName();
        context += "<br/><b>密码：</b>";
        context += password;
        context += "<br/><b>请妥善保管避免密码泄露</b>";
        
        emailVo.setEmailContent(context);
        String[] receivers = {adminUser.getEmail()};
        
        emailVo.setReceivers(receivers);
        emailVo.setSubject("注册服务平台账号成功");
        
        sendMail.sendEmail(emailVo);
        //保存密文密码到数据库中
        adminUser.setPasswd(Encrypt.SHA256(password));
        
        adminUserMapper.updateAdminUser(adminUser);
        //记录日志
        adminUser.setStatus(Constant.NORMAL_STATUS_CODE);
        adminUser.setActionType(Constant.ACTIONTYPE_LOG_ACTIVATION);
        adminUser.setActionUserId(adminUserId);
        adminUser.setLastModifyDate(new Date());
        adminUserMapper.saveAdminUserLog(adminUser);
    }

    @Override
    public Long checkAdminUserName(String adminUserName) {
        return adminUserMapper.findCount(adminUserName);
    }

    @Override
    public void insertUserLoginLog(AdminUser adminUser){
    	
    	adminUserMapper.insertUserLoginLog(adminUser);
    }

    @Override
    public Long checkAdminUser(AdminUser adminUser, String checkType) throws Exception {
        HashMap<String, Object> paramMap = Maps.newHashMap();
        if("name".equals(checkType)){
            paramMap.put(checkType,adminUser.getName());
        }else if("email".equals(checkType)){
            paramMap.put(checkType,adminUser.getEmail());
        }else if("mobile".equals(checkType)){
            paramMap.put(checkType,adminUser.getMobile());
        }else{
            throw new Exception("没有要校验的类型");
        }
        if(adminUser.getAdminUserId() != null){
            paramMap.put("adminUserId",adminUser.getAdminUserId());
        }
        return adminUserMapper.findCountByParam(paramMap);
    }

    @Override
    public List<AdminUser> getUserByUserNameAndPassword(String userName, String password) {
        return adminUserMapper.getUserByUserNameAndPassword(userName,password);
    }

	@Override
	public void updateUserPassword(AdminUser adminUser) throws MessagingException {
		
		//产生新的密码
		String password = CommonUtil.randomPassword(6); 
		
		adminUser.setLastModifyDate(new Date());
		adminUser.setPasswd(Encrypt.SHA256(password));
		
		adminUserMapper.updateAdminUser(adminUser);
		
        //向用户的邮箱发送明文密码
        String context = "<h2>恭喜重置密码成功，您可以使用服务管理系统了<h2><br/><b>账号：</b>";
        context += adminUser.getName();
        context += "<br/><b>密码：</b>";
        context += password;
        context += "<br/><b>请妥善保管避免密码泄露</b>";
        
        emailVo.setEmailContent(context);
        String[] receivers = {adminUser.getEmail()};
        
        emailVo.setReceivers(receivers);
        emailVo.setSubject("服务平台密码重置成功");
        
        sendMail.sendEmail(emailVo);
	}
	
	@Override
	public List<String> findRoleIdByAdminUserId(Integer adminUserId) {
		
		return adminUserMapper.findRoleIdByAdminUserId(adminUserId);
	}

	@Override
	public void updateUserPassword(AdminUser adminUser, String newPSW) throws MessagingException {
		adminUser.setLastModifyDate(new Date());
		adminUser.setPasswd(Encrypt.SHA256(newPSW));
		
		adminUserMapper.updateAdminUser(adminUser);
		
        //向用户的邮箱发送明文密码
        String context = "<h2>恭喜重置密码成功，您可以使用服务管理系统了<h2><br/><b>账号：</b>";
        context += adminUser.getName();
        context += "<br/><b>密码：</b>";
        context += newPSW;
        context += "<br/><b>请妥善保管避免密码泄露</b>";
        
        emailVo.setEmailContent(context);
        String[] receivers = {adminUser.getEmail()};
        
        emailVo.setReceivers(receivers);
        emailVo.setSubject("服务平台密码重置成功");
        
        sendMail.sendEmail(emailVo);
	}

	@Override
	public void updatePassword(AdminUser adminUser, String newPassword) throws MessagingException {
		adminUser.setLastModifyDate(new Date());
		adminUser.setPasswd(Encrypt.SHA256(newPassword));
		
		adminUserMapper.updateAdminUser(adminUser);
		
        //向用户的邮箱发送明文密码
        String context = "<h2>恭喜修改密码成功，您可以使用服务管理系统了<h2><br/><b>账号：</b>";
        context += adminUser.getName();
        context += "<br/><b>密码：</b>";
        context += newPassword;
        context += "<br/><b>请妥善保管避免密码泄露</b>";
        
        emailVo.setEmailContent(context);
        String[] receivers = {adminUser.getEmail()};
        
        emailVo.setReceivers(receivers);
        emailVo.setSubject("服务平台修改密码成功");
        
        sendMail.sendEmail(emailVo);
	}

}
