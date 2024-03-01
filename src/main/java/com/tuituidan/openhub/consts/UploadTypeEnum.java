package com.tuituidan.openhub.consts;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * UploadTypeEnum.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/3/1
 */
@Getter
@AllArgsConstructor
public enum UploadTypeEnum {
    /**
     * 默认
     */
    DEFAULT("default"),
    /**
     * 卡片附件
     */
    ATTACHMENTS("attachments");

    private String type;

    private static final Map<String, UploadTypeEnum> DATAMAP = Arrays.stream(values())
            .collect(Collectors.toMap(UploadTypeEnum::getType, Function.identity()));

    /**
     * getEnum
     *
     * @param type type
     * @return UploadTypeEnum
     */
    public static UploadTypeEnum getEnum(String type) {
        return DATAMAP.get(type);
    }
}
