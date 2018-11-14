package com.fahai.cc.service.role.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.role.service.MenuService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.google.common.collect.Maps;


@RestController
@RequestMapping("/serviceManager/menu")
public class MenuController {

    private Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private CustomeSession customeSession;

    @RequestMapping("/findMenuList")
    public Map<String,Object> findMenuList(HttpServletRequest request){
        HashMap<String, Object> map = Maps.newHashMap();
        try{
            /*HttpSession session = request.getSession();
            AdminUser user = (AdminUser) session.getAttribute(Constant.USER_NAME_SESSION);*/
        	AdminUser user = customeSession.getUser(request);
            if(user == null){
                user = new AdminUser();
                user.setAdminUserId(228);
            }
            List<String> menuMasts = menuService.findMenuList(user.getAdminUserId());
            map.put(Constant.ERROR_CODE,"0");
            map.put(Constant.DATA_LIST,menuMasts);
        }catch (Exception e){
            logger.error("查找菜单栏失败",e.fillInStackTrace());
            map.put(Constant.ERROR_CODE,"1");
        }
        return map;
    }

}
