package com.tuituidan.openhub.consts;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CardType.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/7
 */
@Getter
@AllArgsConstructor
public enum CardTypeEnum {
    /**
     * 默认
     */
    DEFAULT("default"),
    /**
     * 原型
     */
    ZIP("zip"),
    /**
     * 二维码
     */
    QRCODE("qrcode");

    private String type;

    private static final Map<String, CardTypeEnum> DATAMAP = Arrays.stream(values())
            .collect(Collectors.toMap(CardTypeEnum::getType, Function.identity()));

    /**
     * getEnum
     *
     * @param type type
     * @return CardTypeEnum
     */
    public static CardTypeEnum getEnum(String type) {
        return DATAMAP.get(type);
    }
}
