package com.fahai.cc.service.shiro;


import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.adminUser.service.AdminUserService;
import com.fahai.cc.service.util.Constant;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private AdminUserService adminUserService;

    /**
     * shiro登录认证
     * @param authToken authToken
     * @return  AuthenticationInfo
     * @throws AuthenticationException  异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authToken) throws AuthenticationException {
        logger.info("执行doGetAuthenticationInfo开始进行验证");
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        String userName = token.getUsername();
        List<AdminUser> userList = adminUserService.getUserByUserName(userName);
        if(userList == null || userList.size() == 0){
            throw new UnknownAccountException("登录用户："+userName+"不存在");
        }
        AdminUser user = userList.get(0);
        if(user.getStatus() == null || user.getStatus() == 1){
            throw new LockedAccountException("登录用户"+userName+"被锁定");
        }
        SecurityUtils.getSubject().getSession().setAttribute(Constant.USER_NAME_SESSION,userName);

        ShiroUser shiroUser =new ShiroUser(user.getAdminUserId(),userName,user.getName(),0,null);

        return new SimpleAuthenticationInfo(shiroUser,user.getPasswd().toCharArray(),getName());
	}

    /**
     * Shiro权限认证
     * 会被shiro进行回调
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("进行权限认证");
        System.out.println("------------------------------------------------------------------------");
        //从 principalCollection 中来获取登录用户的信息
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        List<Long> roleList = shiroUser.roleList;
        //获取用户的角色
        Set<String> urlSet = Sets.newHashSet();
        urlSet.add("admin");
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermissions(urlSet);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(urlSet);
        return info;
    }


}
