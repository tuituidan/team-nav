package com.tuituidan.openhub.consts;

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
     * 还原文件
     */
    REVERT("revert"),

    /**
     * 浏览器书签
     */
    BOOKMARK("bookmark"),

    /**
     * 卡片附件
     */
    ATTACHMENTS("attachments");

    private String type;
}
