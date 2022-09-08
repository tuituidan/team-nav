package com.tuituidan.openhub.util.thread;

import com.tuituidan.openhub.util.BeanExtUtils;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * CompletableUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/12/26
 */
@Component
@Slf4j
public class CompletableUtils implements ApplicationContextAware {

    /**
     * 任务执行线程池.
     */
    private static ThreadPoolExecutor threadPool;

    private static final ThreadPoolConfig DEF_CONFIG = new ThreadPoolConfig();

    private static final int QUEUE_SIZE = 100;

    private static final Long KEEP_ALIVE_TIME = 10L;

    static {
        int corePoolNum = 2 * Runtime.getRuntime().availableProcessors() + 1;
        int maximumPoolSize = 2 * corePoolNum;
        DEF_CONFIG.setCorePoolNum(corePoolNum);
        DEF_CONFIG.setMaxPoolNum(maximumPoolSize);
        DEF_CONFIG.setKeepAliveTime(KEEP_ALIVE_TIME);
        DEF_CONFIG.setQueueSize(QUEUE_SIZE);
        DEF_CONFIG.setThreadNamePrefix("team-nav");
    }

    /**
     * waitAll.
     *
     * @param futures futures
     */
    public static void waitAll(List<CompletableFuture<?>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * waitAll.
     *
     * @param futures futures
     */
    public static void waitAll(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
    }

    /**
     * runAsync.
     *
     * @param task task
     * @return Void Void
     */
    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task, threadPool);
    }

    /**
     * runAsync.
     *
     * @param task task
     * @return Void Void
     */
    public static <T> CompletableFuture<T> runAsync(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, threadPool);
    }

    /**
     * setApplicationContext.
     *
     * @param applicationContext applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        CompletableUtils.newThreadPoolExecutor(applicationContext.getBean(ThreadPoolConfig.class));
    }

    private static void newThreadPoolExecutor(ThreadPoolConfig config) {
        if (config != null) {
            BeanExtUtils.copyNotNullProperties(config, DEF_CONFIG);
        }
        threadPool = new ThreadPoolExecutor(DEF_CONFIG.getCorePoolNum(),
                DEF_CONFIG.getMaxPoolNum(),
                DEF_CONFIG.getKeepAliveTime(), TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(DEF_CONFIG.getQueueSize()),
                new BasicThreadFactory.Builder().namingPattern(DEF_CONFIG.getThreadNamePrefix() + "-%d").build(),
                CompletableUtils::rejectedExecution);
        // 用的情况少，允许释放核心线程
        threadPool.allowCoreThreadTimeOut(true);
    }

    private static void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException ex) {
                log.error("线程加入队列失败", ex);
                Thread.currentThread().interrupt();
            }
        }
    }

}
