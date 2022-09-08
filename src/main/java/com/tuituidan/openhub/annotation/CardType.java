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
     * value
     *
     * @return CardTypeEnum
     */
    CardTypeEnum value();

}
