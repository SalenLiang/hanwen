package com.fahai.cc.service.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>线程池工具类</p>
 * <p>该类是单例</p>
 */
public enum ThreadManager {

    INSTANCE;

    private Logger logger = LoggerFactory.getLogger(ThreadManager.class);
    private ExecutorService threadPool;

    private boolean isInit = false;//线程池初始化标志

    /**
     * 初始化线程池
     */
    public synchronized boolean initThreadPool() {
        if(!isInit){
            Properties config = null;
            try {
                logger.info("加载线程池配置");
                config = PropertiesManager.getProperty("threadPool.properties", "UTF-8");
            } catch (IOException e) {
                logger.error("加载线程池配置出现异常，初始化线程池失败",e.fillInStackTrace());
                return false;
            }
            String poolSize = config.getProperty("thread.poolSize");
            if(StringUtils.isNoneBlank(poolSize)){
                threadPool = Executors.newFixedThreadPool(Integer.parseInt(poolSize));
            }else{
                logger.info("未配置线程池大小，将以PC机核数设定线程数量");
                threadPool = Executors.newFixedThreadPool(CommonUtil.getPCThreadSize());
            }
            isInit = true;
        }else{
            logger.warn("线程池已经初始化过了，不需要重复初始化");
        }
        return true;
    }

    /**
     * 线程池加入一个线程并且启动
     * @param task    线程
     */
    public void executeTask(Runnable task) throws Exception {
        if(threadPool != null && task != null){
            threadPool.execute(task);
        }else{
            throw new Exception("线程池或线程为空");
        }
    }

    /**
     * 线程池加入一个线程并且启动，带返回值
     * 可通过future.get();获取返回值，但此时会阻塞线程
     * @param task 线程
     */
    public <T> Future<T>  submitTask(Callable<T> task) throws Exception {
        Future<T> future = null;
        if(threadPool != null && task != null){
            future = threadPool.submit(task);
        }else{
            throw new Exception("线程池或线程为空");
        }
        return future;
    }

    /**
     * 温柔的关闭线程池，会等待所有的任务结束
     */
    public void shutdown(){
        if(threadPool != null){
            threadPool.shutdown();
            isInit = false;
        }
    }

    /**
     * 立即关闭线程池
     */
    public void shutdownNow(){
        if(threadPool != null) {
            threadPool.shutdownNow();
            isInit = false;
        }
    }

    /**
     * 重新初始化线程池
     */
    public void reInitPool(){
        this.shutdown();
        initThreadPool();
    }

}
