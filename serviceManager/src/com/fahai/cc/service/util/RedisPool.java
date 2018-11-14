package com.fahai.cc.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * redis连接池的一些操作
 */
public class RedisPool {
    private final static int DEFAULT_DEAD_TIME = 3600*24*7;    //默认过期时间 保存7天
    private static Logger logger = LoggerFactory.getLogger(RedisPool.class);
    //redis连接池
    private static JedisPool jedisPool;

    static {
        Properties config = null;
        try {
            logger.info("读取redis配置文件");
            config = PropertiesManager.loadProperty("redis.properties", "UTF-8");
        } catch (IOException e) {
            logger.error("读取redis配置文件出现异常",e.fillInStackTrace());
            e.printStackTrace();
        }

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(config.getProperty("redis.maxTotal")));//最大连接数
        poolConfig.setMaxIdle(Integer.parseInt(config.getProperty("redis.maxIdle")));//最大空闲
        //最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        poolConfig.setMaxWaitMillis(Integer.parseInt(config.getProperty("redis.maxWaitMillis")));
        // 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
        //在获取连接的时候检查有效性
        poolConfig.setTestOnBorrow(true);
        // 在还会给pool时，是否提前进行validate操作
        poolConfig.setTestOnReturn(true);
        if (jedisPool == null){
            //java.net.SocketTimeoutException: Read timed out exception的异常信息
            //JedisPool默认的超时时间是2秒(单位毫秒)
            jedisPool = new JedisPool(poolConfig,config.getProperty("redis.host"),
                    Integer.parseInt(config.getProperty("redis.port")),60000);
        }
    }

    /**
     * 返回一个redis对象
     * @return
     */
    public static Jedis getJedis(){
        Jedis jedis = null;
        if (jedisPool != null) {
            jedis = jedisPool.getResource();
        }
        return jedis;

    }


    /**
     * 将redis资源关闭还回池中
     * @param jedis
     */
    public static void closeJedis(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }

    /**
     * 清空当前库
     */
    public void flushDB(){
        Jedis jedis = getJedis();
        jedis.flushDB();
        closeJedis(jedis);
    }
}
