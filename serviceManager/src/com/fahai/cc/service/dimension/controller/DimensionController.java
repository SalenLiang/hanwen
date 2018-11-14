package com.fahai.cc.service.dimension.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahai.cc.service.adminUser.entity.AdminUser;
import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.dimension.service.DimensionService;
import com.fahai.cc.service.field.service.FieldService;
import com.fahai.cc.service.util.Constant;
import com.fahai.cc.service.util.CustomeSession;
import com.fahai.cc.service.util.Page;
import com.fahai.cc.service.util.StringUtil;
import com.fahai.cc.service.vo.DimensionFieldVo;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/serviceManager/dimension")
public class DimensionController {
    private Logger logger = LoggerFactory.getLogger(DimensionController.class);
    @Autowired
    private DimensionService dimensionService;

    @Autowired
	private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private FieldService fieldService;
    
    @Autowired
    private CustomeSession customeSession;

    @RequestMapping("/find")
    public Page<DimensionFieldVo> findByPage(HttpServletRequest request){
    	logger.info("查询维度列表");
    	Page<DimensionFieldVo> findByPageDimensionField = null;
    	try {
    		int pageSize = request.getParameter(Constant.PAGE_SIZE)==null? 15:Integer.parseInt(request.getParameter(Constant.PAGE_SIZE));//每页显示条数
    		int currentPage = request.getParameter(Constant.PAGE)==null? 1:Integer.parseInt(request.getParameter(Constant.PAGE));//当前页
    		String dimensionCode = request.getParameter("dimensionCode");
    		String dimensionName = request.getParameter("dimensionName");
    		String status = request.getParameter("status") ; //==null?"0":request.getParameter("status");
    		Map<String, Object> paramsMap = Maps.newHashMap();
    		paramsMap.put(Constant.PAGE_SIZE,pageSize);
    		paramsMap.put(Constant.PAGE,currentPage);
    		
    		if(StringUtils.isNotBlank(status)){
    			paramsMap.put("status",status);
    		}
    		if(StringUtils.isNotBlank(dimensionCode)){
    			paramsMap.put("dimensionCode",dimensionCode);
    		}
    		if(StringUtils.isNotBlank(dimensionName)){
    			paramsMap.put("dimensionName",dimensionName);
    		}
    		findByPageDimensionField = dimensionService.findByPageDimensionField(paramsMap);
		} catch (Exception e) {
			logger.error("DimensionController.class [findByPage()] :error"+e);
		}
        return findByPageDimensionField;
    }

/*    @RequestMapping("/save")
    public Map<String,Object> save(HttpServletRequest request){
    	HashMap<String, Object> resultMap = Maps.newHashMap();
    	String dimensionStr = StringUtil.toEmpty(request.getParameter("dimension"));
    	String fieldListStr = StringUtil.toEmpty(request.getParameter("fieldList"));
    	try{
    		logger.info("保存维度");
    		dimensionService.save(dimensionStr,fieldListStr);
    		resultMap.put(Constant.ERROR_CODE,"0");
    	}catch (Exception e){
    		e.printStackTrace();
    		resultMap.put(Constant.ERROR_CODE,"1");
    		logger.error("保存维度出现异常",e.fillInStackTrace());
    	}
    	return  resultMap;
    }
*/    
    @RequestMapping("/save")
    public Map<String,Object> save(String dimension,String fieldList,HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String dimensionStr = StringUtil.toEmpty(dimension);
        String fieldListStr = StringUtil.toEmpty(fieldList);
        try{
            logger.info("保存维度");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            
            Dimension dimensionBean = dimensionService.save(dimensionStr,fieldListStr,user.getAdminUserId());
            //在redis缓存中新增
            com.fahai.cc.interf.mysql.entity.Dimension redisDimension = dimensionService.getDimension(dimensionBean.getDimensionId());
            if (redisDimension != null) {
            	redisTemplate.opsForValue().set(Constant.REDIS_DIMENSION_KEY+redisDimension.getDimensionCode(),redisDimension);
            	List<com.fahai.cc.interf.mysql.entity.Field> dimensionFieldList = fieldService.getDimensionField(redisDimension.getDimensionId());
            	if (dimensionFieldList != null && dimensionFieldList.size()>0) {
            		redisTemplate.opsForValue().set(Constant.REDIS_DIMENSIONFIELDS_KEY+redisDimension.getDimensionCode(), dimensionFieldList);
				}
			}
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
        	e.printStackTrace();
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("保存维度出现异常",e.fillInStackTrace());
        }
        return  resultMap;
    }
    
    /*dimension:JSON.stringify(dimension),
    fieldList: JSON.stringify(fieldList)*/
/*    @RequestMapping("/update")
    public Map<String,Object> update(HttpServletRequest request){
    	HashMap<String, Object> resultMap = Maps.newHashMap();
    	String dimensionStr = request.getParameter("dimension");
    	String fieldListStr = request.getParameter("fieldList");
    	try{
    		logger.info("修改维度");
    		dimensionService.update(dimensionStr,fieldListStr);
    		resultMap.put(Constant.ERROR_CODE,"0");
    	}catch (Exception e){
    		resultMap.put(Constant.ERROR_CODE,"1");
    		logger.error("修改维度出现异常",e.fillInStackTrace());
    	}
    	return  resultMap;
    }
*/    
    @RequestMapping("/update")
    public Map<String,Object> update(String dimension,String fieldList,HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String dimensionStr = StringUtil.toEmpty(dimension);
        String fieldListStr = StringUtil.toEmpty(fieldList);
        try{
            logger.info("修改维度");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            Dimension dimensionBean = dimensionService.update(dimensionStr,fieldListStr,user.getAdminUserId());
            //更新缓存
            if (dimensionBean.getStatus() == 1) {
            	redisTemplate.opsForValue().getOperations().delete(Constant.REDIS_DIMENSION_KEY+dimensionBean.getDimensionCode());
                redisTemplate.opsForValue().getOperations().delete(Constant.REDIS_DIMENSIONFIELDS_KEY+dimensionBean.getDimensionCode());
    		}else{
    			List<com.fahai.cc.interf.mysql.entity.Field> dimensionFieldList = fieldService.getDimensionField(dimensionBean.getDimensionId());
    			if (dimensionFieldList != null && dimensionFieldList.size()>0) {
    				redisTemplate.opsForValue().set(Constant.REDIS_DIMENSIONFIELDS_KEY+dimensionBean.getDimensionCode(), dimensionFieldList);
				}
    		}
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("修改维度出现异常",e.fillInStackTrace());
        }
        return  resultMap;
    }

    @RequestMapping("/delete")
    public Map<String,Object> delete(Dimension dimension,HttpServletRequest request){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        try{
            logger.info("删除维度");
//            AdminUser user = (AdminUser) request.getSession().getAttribute(Constant.USER_NAME_SESSION);
            AdminUser user = customeSession.getUser(request);
            dimension.setActionUserId(user.getAdminUserId());
            dimensionService.delete(dimension);
            //从缓存中去除
            redisTemplate.opsForValue().getOperations().delete(Constant.REDIS_DIMENSION_KEY+dimension.getDimensionCode());
            redisTemplate.opsForValue().getOperations().delete(Constant.REDIS_DIMENSIONFIELDS_KEY+dimension.getDimensionCode());
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("删除维度出现异常",e.fillInStackTrace());
        }
        return  resultMap;
    }

    @RequestMapping("/findDimensionById/{dimensionId}")
    public Map<String,Object> findDimensionById(@PathVariable Integer dimensionId){
        Map<String,Object> resultMap = Maps.newHashMap();
        try{
            logger.info("通过维度Id查找维度");
            resultMap = dimensionService.findDimensionById(dimensionId);
            if(resultMap.isEmpty()){
            	resultMap.put("status",0);
            }else{
            	resultMap.put("status",1);
            }
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("查找维度出现异常",e.fillInStackTrace());
        }
        return  resultMap;
    }
    
    
    @RequestMapping("/checkDimensionById/{dimensionId}")
    public Map<String,Object> checkDimensionById(@PathVariable String dimensionId){
        Map<String,Object> resultMap = Maps.newHashMap();
        try{
            logger.info("通过维度Id查找维度");
            Long num = dimensionService.checkDimensionById(dimensionId);
            if(num > 0){
                resultMap.put(Constant.CHECK_RESULT,"1");
            }else{
                resultMap.put(Constant.CHECK_RESULT,"0");
            }
            resultMap.put(Constant.ERROR_CODE,"0");
        }catch (Exception e){
            resultMap.put(Constant.ERROR_CODE,"1");
            logger.error("查找维度出现异常",e.fillInStackTrace());
        }
        return  resultMap;
    }
}
