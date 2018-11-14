package com.fahai.cc.service.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 任务调度管理类
 */
public enum QuartzManager {

    INSTANCE;

    private Logger logger = LoggerFactory.getLogger(QuartzManager.class);

    /**
     * 校验cron表达式
     * @param cronExpression    cron表达式
     * @return  true是合法的表达式，false不是合法的表达式
     */
   public boolean isValidExpression(String cronExpression){
       boolean flag = false;
       if(StringUtils.isNoneBlank(cronExpression)){
           flag = CronExpression.isValidExpression(cronExpression);
       }
       return flag;
   }

    /**
     * 获取一个Scheduler
     * @return  Scheduler实例
     * @throws SchedulerException   Scheduler异常
     */
    private Scheduler getScheduler() throws SchedulerException {
       return StdSchedulerFactory.getDefaultScheduler();
    }

    /**
     * 添加一个Job
     * @param jobClass              job类
     * @param jobName               job名
     * @param jobGroupName          job所在组名
     * @param triggerName           触发器名
     * @param triggerGroupName      触发器组名
     * @param cronExpression        cron表达式
     * @param dataMap               封装了job上下文的数据
     * @throws SchedulerException   Scheduler异常
     */
   public boolean addJob(Class<? extends Job> jobClass,String jobName,String jobGroupName,String triggerName,
                      String triggerGroupName,String cronExpression,JobDataMap dataMap) throws SchedulerException {
       boolean flag = isValidExpression(cronExpression);
       if(!flag){
           logger.warn("非法的cron表达式");
            return false;
       }
       logger.info("正在添加一个定时任务jobName:"+jobName+"  jobGroupName:"+jobGroupName+"并执行，执行规则是："+cronExpression);
       Scheduler scheduler = getScheduler();
       JobDetailImpl jobDetail = (JobDetailImpl) JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
       jobDetail.setJobDataMap(dataMap);
       CronTriggerImpl cronTrigger = (CronTriggerImpl) TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
               .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
       //触发规则
       cronTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
       scheduler.scheduleJob(jobDetail,cronTrigger);
       scheduler.start();
       return true;
   }

    /**
     * 暂停任务
     * @param jobName               job名
     * @param jobGroupName          job所在组名
     * @param triggerName           触发器名
     * @param triggerGroupName      触发器组名
     * @throws SchedulerException   Scheduler异常
     */
   public void pauseJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName) throws SchedulerException {
       logger.info("正在暂停一个job任务");
       Scheduler scheduler = getScheduler();
       TriggerKey triggerKey = new TriggerKey(jobName, jobGroupName);
       JobKey jobKey = new JobKey(triggerName, triggerGroupName);
       scheduler.pauseTrigger(triggerKey);
       scheduler.pauseJob(jobKey);
       logger.info("暂停任务结束");

   }

    /**
     * 移除任务
     * @param jobName               job名
     * @param jobGroupName          job所在组名
     * @param triggerName           触发器名
     * @param triggerGroupName      触发器组名
     * @throws SchedulerException   Scheduler异常
     */
   public void removeJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName) throws SchedulerException {
       logger.info("正在移除一个任务");
       Scheduler scheduler = getScheduler();
       TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
       scheduler.pauseTrigger(triggerKey);
       scheduler.unscheduleJob(triggerKey);
       scheduler.deleteJob(new JobKey(jobName,jobGroupName));
       logger.info("移除任务结束");
   }

   public void removeAllJob() throws SchedulerException {
       logger.info("正在移除所有的任务");
       Scheduler scheduler = getScheduler();
       GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.anyGroup();
       GroupMatcher<TriggerKey> triggerKeyGroupMatcher = GroupMatcher.anyGroup();
       Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(triggerKeyGroupMatcher);
       Set<JobKey> jobKeys = scheduler.getJobKeys(jobKeyGroupMatcher);

       scheduler.pauseTriggers(triggerKeyGroupMatcher);
       scheduler.unscheduleJobs(Lists.newArrayList(triggerKeys));
       scheduler.deleteJobs(Lists.newArrayList(jobKeys));

   }
}
