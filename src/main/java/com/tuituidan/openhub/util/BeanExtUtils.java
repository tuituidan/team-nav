package com.tuituidan.openhub.util;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

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
     * 转换对象，Target target = BeanExtUtils.convert(source, Target::new).
     *
     * @param source 源属性Dto
     * @param supplier 创建目标对象
     * @param <T> 转换类型
     * @return T
     */
    public static <T> T convert(Object source, Supplier<T> supplier) {
        return convert(source, supplier, (String) null);
    }

    /**
     * 转换对象,Target target = BeanExtUtils.convert(source, Target::new, "id", "name").
     *
     * @param source 源属性Dto
     * @param supplier 创建目标对象
     * @param ignoreProperties 要忽略的属性
     * @param <T> 转换类型
     * @return T
     */
    public static <T> T convert(Object source, Supplier<T> supplier, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T target = supplier.get();
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
        BeanUtils.copyProperties(source, target, Arrays.stream(src.getPropertyDescriptors()).filter(item ->
                Objects.isNull(item.getReadMethod()) || Objects.isNull(src.getPropertyValue(item.getName()))
        ).map(FeatureDescriptor::getName).toArray(String[]::new));
    }

    /**
     * 拷贝指定属性.
     *
     * @param source source
     * @param target target
     * @param props props
     */
    public static void copyProperties(Object source, Object target, String... props) {
        Assert.notEmpty(props, "必须指定要拷贝的属性，否则请直接使用Spring的BeanUtils.copyProperties");
        BeanUtils.copyProperties(source, target,
                Arrays.stream(new BeanWrapperImpl(source).getPropertyDescriptors())
                        .map(FeatureDescriptor::getName)
                        .filter(name -> !ArrayUtils.contains(props, name))
                        .toArray(String[]::new));
    }

    /**
     * List拷贝, List targets = BeanExtUtils.convert(sourceList, Target::new).
     *
     * @param sourceList 源集合
     * @param supplier 目标对象
     * @param <T> 目标对象类型
     * @return list
     */
    public static <T> List<T> convertList(List<?> sourceList, Supplier<T> supplier) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        return sourceList.stream().map(item -> convert(item, supplier)).collect(Collectors.toList());
    }

}
