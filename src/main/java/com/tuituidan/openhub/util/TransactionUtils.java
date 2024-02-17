package com.tuituidan.openhub.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 编程式事务.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@Component
public class TransactionUtils implements ApplicationContextAware {

    private static TransactionTemplate transactionTemplate;

    /**
     * 事务处理.
     *
     * @param runnable runnable
     */
    public static void execute(Runnable runnable) {
        transactionTemplate.execute(status -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init(applicationContext.getBean(TransactionTemplate.class));
    }

    private static void init(TransactionTemplate template) {
        transactionTemplate = template;
    }
}
