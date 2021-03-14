package com.tuituidan.teamnav.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Consts.
 *
 * @author zhujunhan
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
     * admin.
     */
    public static final String ADMIN = "/admin";

    /**
     * CARD_TYPE_ZIP.
     */
    public static final String CARD_TYPE_ZIP = "2";

    /**
     * ROOT_DIR.
     */
    public static final String ROOT_DIR = System.getProperty("user.dir");

}
