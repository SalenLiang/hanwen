package com.fahai.cc.service.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Random;
import java.util.UUID;

/**
 *
 * Created by Administrator on 2017/5/8.
 */
public class CommonUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static void main(String[] args) {
        System.out.println(uuid());
    }

    /**
     * 生成UUID
     * @return 去横杠后的UUID
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 生成随机密码
     * @param passwordCount 密码长度(最少6位)
     * @return  生成的随机密码
     */
    public static String randomPassword(int passwordCount){
        Random random = new Random();
        String dict="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        if(passwordCount < 6){
            passwordCount = 6;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0 ;i<passwordCount ;i++){
            stringBuilder.append(dict.charAt(random.nextInt(dict.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * 获取PC机系统线程数量
     * @return
     */
    public static int getPCThreadSize(){
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * <p>Object转换为字节数组</p>
     * <p>如果传入参数为null,则返回null</p>
     * @param obj   待转换的值
     * @return byte[]
     */
    public static byte[] objectToByteArray(Object obj){
        if(obj == null){
            return null;
        }
        byte[] bytes = null;
        ObjectOutputStream outputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(obj);
            outputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("转换为字节数组出现异常",e.fillInStackTrace());
        }finally {
            closeOutputStream(byteArrayOutputStream);
            closeOutputStream(outputStream);
        }
        return bytes;
    }

    /**
     * <p>字节数组转换Object的类型</p>
     * @param bytes 待转换字节数组
     * @return  Object
     */
    public static Object byteArrayToObject(byte[] bytes){
        if(bytes == null){
            return null;
        }
        Object obj = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            obj = objectInputStream.readObject();
        } catch (IOException e) {
            logger.error("字节数组转换Object类型出现异常",e.fillInStackTrace());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
        }finally {
            closeInputStream(byteArrayInputStream);
            closeInputStream(objectInputStream);
        }
        return obj;
    }


    /**
     * 关闭输出流
     * @param inputStream   输入流
     */
    private static void closeInputStream(InputStream inputStream){
        if(inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭输入流出现异常",e.fillInStackTrace());
            }
        }
    }
    /**
     * 关闭输出流
     * @param outputStream  输出流
     */
    private static void closeOutputStream(OutputStream outputStream){
        if(outputStream != null){
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("关闭输出流出现异常",e.fillInStackTrace());
            }
        }
    }

    /**
     * 对给的的输入字符串进行制定盐值加密
     * @param hashAlgorithmName     加密类型，eg:MD5
     * @param credentials   待加密字符串
     * @param saltStr       制定加密的盐值字符串
     * @param count         加密次数
     * @return  盐值加密后的值
     */
    public static String saltEncryption(String hashAlgorithmName,String credentials,String saltStr,int count){
//        String hashAlgorithmName = "MD5";
//        Object credentials = "123456";
//        Object salt = ByteSource.Util.bytes("user");
//        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName, credentials, ByteSource.Util.bytes(saltStr), count);
        return String.valueOf(result);
    }

}
