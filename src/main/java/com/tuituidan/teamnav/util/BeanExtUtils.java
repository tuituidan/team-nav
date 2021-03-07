package com.tuituidan.teamnav.util;

import com.tuituidan.teamnav.exception.NewInstanceException;

import java.beans.FeatureDescriptor;
import java.util.Arrays;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * BeanExtUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/27
 */
@UtilityClass
public class BeanExtUtils {
    /**
     * 转换对象.
     *
     * @param source 源属性Dto
     * @param cls    目标属性Do
     * @param <T>    转换类型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> cls) {
        return convert(source, cls, (String) null);
    }

    /**
     * 转换对象.
     *
     * @param source           源属性Dto
     * @param cls              目标属性Do
     * @param ignoreProperties 要忽略的属性
     * @param <T>              转换类型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> cls, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T target = newInstanceByCls(cls);
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    /**
     * 复制非空数据.
     *
     * @param source 源属性Dto
     * @param target 目标属性Do
     */
    public static void copyNotNullProperties(Object source, Object target) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        BeanUtils.copyProperties(source, target, Arrays.stream(src.getPropertyDescriptors())
                .filter(pd -> src.getPropertyValue(pd.getName()) == null)
                .map(FeatureDescriptor::getName).toArray(String[]::new));
    }

    private static <T> T newInstanceByCls(Class<T> cls) {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new NewInstanceException("转换错误-{}", cls.getName(), ex);
        }
    }
}
