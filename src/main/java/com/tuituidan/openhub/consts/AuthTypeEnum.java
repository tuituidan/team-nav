package com.tuituidan.openhub.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AuthTypeEnum.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/7/3
 */
@Getter
@AllArgsConstructor
public enum AuthTypeEnum {
    /**
     * key-value
     */
    KEY_VALUE("key-value"),
    /**
     * basic
     */
    BASIC("basic"),
    /**
     * bearer
     */
    BEARER("bearer"),
    /**
     * jwt
     */
    JWT("jwt");

    private String type;
}
