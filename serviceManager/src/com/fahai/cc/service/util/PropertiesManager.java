package com.fahai.cc.service.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * 操作properties的工具类
 */
public class PropertiesManager {

    /**
     * 相对路径加载prop文件
     * @param propName  prop文件名
     * @param charset   字符集（默认UTF-8）
     * @return  配置文件的map
     * @throws IOException  抛出IO异常
     */
    public static Properties loadProperty(String propName,String charset) throws IOException {
        InputStream inputStream=PropertiesManager.class.getClassLoader().getResourceAsStream(propName);
        return loadProp(inputStream,charset);
    }

    /**
     * 文件绝对路径加载配置文件
     * @param propPath  配置文件相对路径
     * @param charset   字符集（默认UTF-8）
     * @return  配置文件的map
     * @throws IOException 抛出IO异常
     */
    public static Properties getProperty(String propPath,String charset) throws IOException {
        InputStream inputStream = new FileInputStream(propPath);
        return loadProp(inputStream,charset);
    }

    private static Properties loadProp(InputStream inputStream,String charset) throws IOException{
        if(StringUtils.isBlank(charset)){
            charset = "UTF-8";
        }
        InputStreamReader reader = new InputStreamReader(inputStream,charset);
        Properties properties = new Properties();
        properties.load(reader);

        //关闭流
        if(inputStream != null){
            inputStream.close();
        }
        if(reader != null){
            reader.close();
        }
        return properties;
    }
}
