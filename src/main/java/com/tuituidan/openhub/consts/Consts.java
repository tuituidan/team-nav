package com.tuituidan.openhub.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Consts.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/9
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Consts {

    /**
     * API 版本1的前缀.
     */
    public static final String API_V1 = "/api/v1";

    /**
     * CARD_TYPE_ZIP.
     */
    public static final String CARD_TYPE_ZIP = "2";

    /**
     * 与jar同级目录，docker下根目录时.
     */
    public static final String ROOT_DIR = "/".equals(System.getProperty("user.dir")) ? ""
            : System.getProperty("user.dir");

}
