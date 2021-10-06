package com.chenyu.framework.manager;

import com.chenyu.common.utils.Threads;
import com.chenyu.common.utils.spring.SpringUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: learnRuoYi
 * @description: 异步任务管理器
 * @author: chen yu
 * @create: 2021-10-01 20:30
 */
public class AsyncManager {

    //操作延迟10毫秒
    private final int OPERATE_DELAY_TIME = 10;

    //获取异步线程池  这个线程池是java 并发包中的
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");


    /**
     *
     * 单例模式  仔细看下这种单例模式  构造函数进行私有化
     */
    private AsyncManager(){}
    private static AsyncManager me =new AsyncManager();  //初始化的时候会生成这个对象
    public  static AsyncManager me(){
        return me;
    }


    /**
     * 执行认任务
     *
     * @param task 需要执行的任务
     * @return
     */
    public void execute(TimerTask task){
        executor.schedule(task,OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     *
     */

    public void shutdown(){
        Threads.shutdownAndAwaitTermination(executor);
    }






}