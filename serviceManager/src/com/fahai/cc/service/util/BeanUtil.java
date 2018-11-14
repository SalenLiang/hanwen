package com.fahai.cc.service.util;



import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtil {

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param instance  实体类
     * @param dataMap   map
     * @param <T>       类型
     * @return  转换后的实体类
     * @throws IllegalAccessException       如果实例化 JavaBean 失败抛出的异常
     * @throws InvocationTargetException    如果实例化 JavaBean 失败 抛出的异常
     * @throws InstantiationException       如果调用属性的 setter 方法失败 抛出的异常
     */
    public static <T>T invokeBean(T instance ,Map<String,Object> dataMap) throws IllegalAccessException, InvocationTargetException, InstantiationException{
        if(dataMap == null ){
            return instance;
        }
        BeanUtils.populate(instance, dataMap);
        return instance;
    }

    /**
     *
     * @param clazz     实体类class对象
     * @param dataMap   封装了参数
     * @param <T>       返回实体类类型
     * @return  实体类
     * @throws IllegalAccessException       如果实例化 JavaBean 失败抛出的异常
     * @throws InvocationTargetException    如果实例化 JavaBean 失败 抛出的异常
     * @throws InstantiationException       如果调用属性的 setter 方法失败 抛出的异常
     */
    public static <T>T invokeBean(Class<T> clazz,Map<String,Object> dataMap) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if(dataMap == null ){
            return null;
        }
        T instance = clazz.newInstance();
        BeanUtils.populate(instance,dataMap);
        return instance;
    }

}
