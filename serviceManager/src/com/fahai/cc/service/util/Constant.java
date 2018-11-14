package com.fahai.cc.service.util;

/**
 * 定义一些常量
 * @author czt
 */
public class Constant {
    //shiro缓存保存在redis的key的前缀
    public static final String PREFIX_SHIRO_IN_REDIS = "service_manager_shiro_cache_";
    
    public static final String CURRENT_USER = "currentUser";

    public static final String USER_NAME_SESSION = "USER_NAME";
    //错误代码
    public static final String ERROR_CODE = "errorCode";
    //错误信息
    public static final String ERROR_MSG = "errorMsg";
    //正常代码
    public static final Integer  NORMAL_STATUS_CODE= 0;
    //删除代码
    public static final Integer DELETE_STATUS_CODE = 1;
    //页码
    public static final String PAGE = "currentPage";
    //条数
    public static final String PAGE_SIZE = "pageSize";
    //开始
    public static final String PAGE_START = "start";
    //校验结果
    public static final String CHECK_RESULT = "result";

    public static final String DATA_LIST = "dataList";

    public static final String DATA= "data";
    
    
    public static final String REDIS_CUSTOMER_KEY = "Customer:";
    
    public static final String REDIS_CUSTOMERINFORMATION_KEY = "Custom:";
    
    public static final String REDIS_DIMENSION_KEY = "dimension:";
    
    public static final String REDIS_DIMENSIONFIELDS_KEY = "dimensionFields:";
    
    
    public static final String REDIS_LIMITWORD = "limitword:";
    
    public static final String LIMITWORD_GENERAL = "general";
    public static final String LIMITWORD_Q = "q";
    public static final String LIMITWORD_PNAME = "pname";
    
    public static final String CHECK_INTERFACE_LINK = "http://218.245.4.64:81/fhfk/test/error";
    		
    public static final int CHECK_INTERFACE_S = 200;
    
    public static final String ACTIONTYPE_LOG_SAVE = "save";
    
    public static final String ACTIONTYPE_LOG_UPDATE = "update";
    
    public static final String ACTIONTYPE_LOG_DELETE = "delete";

    public static final String ACTIONTYPE_LOG_ACTIVATION = "activation";
}
