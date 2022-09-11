package com.tuituidan.openhub.annotation;

import com.tuituidan.openhub.consts.CardTypeEnum;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CardType.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CardType {

    /**
     * 数组，允许多个类型使用同一个实现
     *
     * @return CardTypeEnum
     */
    CardTypeEnum[] value();

}
