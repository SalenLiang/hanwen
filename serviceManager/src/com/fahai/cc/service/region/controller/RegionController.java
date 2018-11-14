package com.fahai.cc.service.region.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.region.entity.Region;
import com.fahai.cc.service.region.service.RegionService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/region")
public class RegionController {

    private Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionService regionService;
    
    @Autowired
    private CustomeSession customeSession;

    @RequestMapping("/find")
    public List<Region> find(){
        logger.info("查询所有的区域");
        List<Region> regionList = null;
        try{
            regionList = regionService.findAll();
        }catch (UnauthenticatedException e){
            logger.error("查询区域所需的方法权限不对",e.fillInStackTrace());
        }catch (Exception e){
            logger.error("查询区域错误",e.fillInStackTrace());
        }
        return regionList;
    }

    @RequestMapping("/save")
    public Map<String,Object> save(Region region,HttpServletRequest request){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            logger.info("保存区域");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            regionService.saveRegion(region,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("保存区域出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/update")
    public Map<String,Object> update(Region region,HttpServletRequest request){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            logger.info("修改区域");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            regionService.updateRegion(region,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("修改区域出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/delete")
    public Map<String,Object> delete(Region region,HttpServletRequest request){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            logger.info("删除区域");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            regionService.deleteRegion(region,user.getAdminUserId());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("删除区域出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

    @RequestMapping("/checkRegionCode/{regionCode}")
    public Map<String,Object> checkRegionCode(@PathVariable String regionCode){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            logger.info("校验区域代码唯一性");
            Long count = regionService.checkRegionCode(regionCode);
            if(count > 0){
                resultMap.put(Constant.CHECK_RESULT,"1");
            }else{
                resultMap.put(Constant.CHECK_RESULT,"0");
            }
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            logger.error("删除区域出现异常",e.fillInStackTrace());
            resultMap.put(Constant.ERROR_CODE,"1");
        }
        return resultMap;
    }

}
