package com.fahai.cc.service.role.controller;

import com.fahai.cc.service.role.entity.Role;
import com.fahai.cc.service.role.service.RoleService;
import com.fahai.cc.service.util.Constant;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serviceManager/role")
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping("/find")
    public Map<String,Object> find(HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        try{
            List<Role> roleList = roleService.findAllRoleList();
            resultMap.put(Constant.ERROR_CODE,"0");
            resultMap.put(Constant.DATA_LIST,roleList);
        }catch (Exception e){
            logger.error("查找角色列表失败",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return  resultMap;
    }
}
