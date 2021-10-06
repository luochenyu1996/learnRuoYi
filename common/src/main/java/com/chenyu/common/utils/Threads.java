package com.chenyu.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: learnRuoYi
 * @description: 整个项目中线程处理相关的类
 * @author: chen yu
 * @create: 2021-10-01 20:57
 */
@Slf4j
public class Threads {

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍人超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     */
    public  static  void  shutdownAndAwaitTermination(ExecutorService pool){
        if (pool!=null && pool.isShutdown()){
            pool.shutdown();

            try {
                if(!pool.awaitTermination(120,TimeUnit.SECONDS)){
                    pool.shutdownNow();
                    if(!pool.awaitTermination(120,TimeUnit.SECONDS)){
                        log.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

    }
}