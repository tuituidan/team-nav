package com.tuituidan.teamnav.util;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * CompletableUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/12/26
 */
@UtilityClass
@Slf4j
public class CompletableUtils {

    /**
     * 任务等待队列 容量.
     */
    private static final int TASK_QUEUE_SIZE = 1000;
    /**
     * 空闲线程存活时间 单位分钟.
     */
    private static final long KEEP_ALIVE_TIME = 10L;

    /**
     * 线程名字前缀.
     */
    private static final String THREAD_NAME_PREFIX = "team-nav";

    /**
     * 任务执行线程池.
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    static {
        int corePoolNum = 2 * Runtime.getRuntime().availableProcessors() + 1;
        int maximumPoolSize = 2 * corePoolNum;
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(corePoolNum, maximumPoolSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(TASK_QUEUE_SIZE),
                new BasicThreadFactory.Builder().namingPattern(THREAD_NAME_PREFIX + "-%d").build(), (r, executor) -> {
            if (!executor.isShutdown()) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException ex) {
                    log.error("线程加入队列失败", ex);
                    Thread.currentThread().interrupt();
                }
            }
        });
    }


    /**
     * execute.
     *
     * @param task Runnable
     */
    public static void execute(Runnable task) {
        THREAD_POOL_EXECUTOR.execute(task);
    }

    /**
     * runAsync.
     *
     * @param task Runnable
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task, THREAD_POOL_EXECUTOR);
    }

    /**
     * waitAll.
     *
     * @param futures futures
     */
    public static void waitAll(List<CompletableFuture<?>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
