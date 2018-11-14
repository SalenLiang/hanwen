package com.fahai.cc.service.util;

import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis操作管理类
 */
public  class RedisManager {

    /**
     * 设置值及过期时间
     * @param key       key
     * @param value     value
     * @param timeoutSecond  过期时间(s)
     */
    public static void set(String key, String value, int timeoutSecond ){
        Jedis jedis = RedisPool.getJedis();
        jedis.set(key,value);
        jedis.expire(key,timeoutSecond);
        RedisPool.closeJedis(jedis);
    }
    /**
     * 设置值，采用默认过期时间
     * @param key       key
     * @param value     value
     */
    public static void set(String key, String value ){
        Jedis jedis = RedisPool.getJedis();
        jedis.set(key,value);
        RedisPool.closeJedis(jedis);
    }
    /**
     * 设置值及过期时间
     * @param key       key
     * @param value     value
     * @param timeout  过期时间
     */
    public static void set(byte[] key,byte[] value,int timeout ){
        Jedis jedis = RedisPool.getJedis();
        jedis.set(key,value);
        jedis.expire(key,timeout);
        RedisPool.closeJedis(jedis);
    }
    /**
     * 设置值，采用默认过期时间
     * @param key       key
     * @param value     value
     */
    public static void set(byte[] key,byte[] value ){
        Jedis jedis = RedisPool.getJedis();
        jedis.set(key,value);
        RedisPool.closeJedis(jedis);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public static boolean existKey(String key){
        Jedis jedis = RedisPool.getJedis();
        boolean flag = jedis.exists(key);
        RedisPool.closeJedis(jedis);
        return flag;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public static boolean existKey(byte[] key){
        Jedis jedis = RedisPool.getJedis();
        boolean flag = jedis.exists(key);
        RedisPool.closeJedis(jedis);
        return flag;
    }


    /**
     * 通过key进行删除
     * @param keys
     */
    public static void delete(String keys){
        Jedis jedis = RedisPool.getJedis();
        jedis.del(keys);
        RedisPool.closeJedis(jedis);
    }
    /**
     * 通过key进行删除
     * @param keys
     */
    public static void delete(String... keys){
        Jedis jedis = RedisPool.getJedis();
        jedis.del(keys);
        RedisPool.closeJedis(jedis);
    }
    /**
     * 通过key进行删除
     * @param keys
     */
    public static void delete(byte[] keys){
        Jedis jedis = RedisPool.getJedis();
        jedis.del(keys);
        RedisPool.closeJedis(jedis);
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public static String get(String key){
        Jedis jedis = RedisPool.getJedis();
        String result = jedis.get(key);
        RedisPool.closeJedis(jedis);
        return result;
    }
    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public static byte[] get(byte[] key){
        Jedis jedis = RedisPool.getJedis();
        byte[] bytes = jedis.get(key);
        RedisPool.closeJedis(jedis);
        return bytes;
    }

    /**
     * 根据key,计算有序集合中元素的数量
     * @param key
     * @return
     */
    public static Long zcard(String key){
        Jedis jedis = RedisPool.getJedis();
        Long zcard = jedis.zcard(key);
        RedisPool.closeJedis(jedis);
        return zcard;
    }
    /**
     * 根据key,于获取存储在集合中的元素的数量
     * @param key
     * @return
     */
    public static Long scard(String key){
        Jedis jedis = RedisPool.getJedis();
        Long scard = jedis.scard(key);
        RedisPool.closeJedis(jedis);
        return scard;
    }

    /**
     * 往list中添加元素
     * @param key       键
     * @param values    值可变数组
     */
    public static void lpush(String key, String... values){
        Jedis jedis = RedisPool.getJedis();
        jedis.lpush(key,values);
        RedisPool.closeJedis(jedis);
    }

    /**
     * 往list中添加元素
     * @param key       键
     * @param values    值可变数组
     */
    public static void rpush(String key, String... values){
        Jedis jedis = RedisPool.getJedis();
        jedis.rpush(key,values);
        RedisPool.closeJedis(jedis);
    }

    public static Long length(String key){
        Jedis jedis = RedisPool.getJedis();
        Long length = jedis.llen(key);
        RedisPool.closeJedis(jedis);
        return length;

    }

    public static String lindex(String key, long index){
        Jedis jedis = RedisPool.getJedis();
        String value = jedis.lindex(key, index);
        RedisPool.closeJedis(jedis);
        return  value;
    }

    public static String lpop(String key){
        Jedis jedis = RedisPool.getJedis();
        String value = jedis.lpop(key);
        RedisPool.closeJedis(jedis);
        return  value;
    }

    public static List<String> lrange(String key, long startIndex, long endIndex){
        Jedis jedis = RedisPool.getJedis();
        List<String> values = jedis.lrange(key, startIndex, endIndex);
        RedisPool.closeJedis(jedis);
        return  values;
    }

    /**
     * 清空当前库
     */
    public static void flushDB(){
        Jedis jedis = RedisPool.getJedis();
        jedis.flushDB();
        RedisPool.closeJedis(jedis);
    }

    /**
     * 查看redis里有多少数据
     */
    public static long dbSize() {
        Jedis jedis = RedisPool.getJedis();
        long len = jedis.dbSize();
        RedisPool.closeJedis(jedis);
        return len;
    }

    /**
     * 检查是否连接成功
     *
     * @return
     */
    public static String ping() {
        Jedis jedis = RedisPool.getJedis();
        String str = jedis.ping();
        RedisPool.closeJedis(jedis);
        return str;
    }

    public static String zrange(String key, long start, long end){
        Jedis jedis = RedisPool.getJedis();
        Set<String> strs = jedis.zrange(key, start, end);
        String str = "";
        if(strs.iterator().hasNext()){
            str = strs.iterator().next();
        }
        RedisPool.closeJedis(jedis);
        return str;
    }

    public static Set<String> zranges(String key, long start, long end){
        Jedis jedis = RedisPool.getJedis();
        Set<String> strs = jedis.zrange(key, start, end);
        RedisPool.closeJedis(jedis);
        return strs;
    }

    public static void zadd(String key, double score, String member){
        Jedis jedis = RedisPool.getJedis();
        jedis.zadd(key, score, member);
        RedisPool.closeJedis(jedis);
    }
    //给原来score增加score
    public static void zincrby(String key, double score, String member){
        Jedis jedis = RedisPool.getJedis();
        jedis.zincrby(key, score, member);
        RedisPool.closeJedis(jedis);
    }

    public static String zrangeByScore(String key, double max){
        Jedis jedis = RedisPool.getJedis();
        Set<String> jsonStrs = jedis.zrangeByScore(key, 0, max);
        String jsonStr = "";
        if(jsonStrs.iterator().hasNext()){
            jsonStr = jsonStrs.iterator().next();
        }
        RedisPool.closeJedis(jedis);
        return jsonStr;
    }

    public static Set<String> zrangeByScores(String key, double max){
        Jedis jedis = RedisPool.getJedis();
        Set<String> jsonStrs = jedis.zrangeByScore(key, 0, max);
        RedisPool.closeJedis(jedis);
        return jsonStrs;
    }

    public static double zscore(String key, String member){
        Jedis jedis = RedisPool.getJedis();
        double score = jedis.zscore(key, member);
        RedisPool.closeJedis(jedis);
        return score;
    }

    public static void zrem(String key, String member){
        Jedis jedis = RedisPool.getJedis();
        jedis.zrem(key, member);
        RedisPool.closeJedis(jedis);
    }


    /**
     * 列出所有的key，value
     */
    public static Map<String, Object> listAll(){
        return listByPattern("*");
    }

    /**
     * 根据正则列出key,value
     */
    public static Map<String, Object> listByPattern(String pattern){
        Jedis jedis = RedisPool.getJedis();
        Set<String> keys = jedis.keys(pattern);
        HashMap<String, Object> map = Maps.newHashMap();
        for (String key:keys) {
            map.put(key,jedis.get(key));
        }
        RedisPool.closeJedis(jedis);
        return map;
    }

    /**
     * keys
     * @param pattern
     * @return
     */
    public static Set<byte[]> keys(String pattern){
        Set<byte[]> keys = null;
        Jedis jedis = RedisPool.getJedis();
        try{
            keys = jedis.keys(pattern.getBytes());
        }finally{
            RedisPool.closeJedis(jedis);
        }
        return keys;
    }

    public static  Set<String> listKeysByPattern(String pattern){
        Jedis jedis = RedisPool.getJedis();
        Set<String> keys = jedis.keys(pattern);
        RedisPool.closeJedis(jedis);
        return keys;
    }

    public static void hmset(String key,Map<String,String> value){
        Jedis jedis = RedisPool.getJedis();
        jedis.hmset(key,value);
        RedisPool.closeJedis(jedis);
    }
    public static void hmset(byte[] key,Map<byte[],byte[]> value){
        Jedis jedis = RedisPool.getJedis();
        jedis.hmset(key,value);
        RedisPool.closeJedis(jedis);
    }

    public static Map<String,String> hmget(String key){
        if(key == null || key.equals("")){
            return null;
        }
        Jedis jedis = RedisPool.getJedis();
        Set<String> keys = jedis.hkeys(key);
        Map<String,String> map = Maps.newHashMap();
        String[] keyArray = keys.toArray(new String[keys.size()]);
        List<String> keyValues = jedis.hmget(key, keyArray);
        for (int i = 0; i <keyValues.size() ; i++) {
            map.put(keyArray[i],keyValues.get(i));
        }
        RedisPool.closeJedis(jedis);
        return map;
    }

    public static void main(String[] args) {
        Map<String, Object> stringObjectMap = listByPattern("*");
        System.out.println(stringObjectMap);
    }

}
