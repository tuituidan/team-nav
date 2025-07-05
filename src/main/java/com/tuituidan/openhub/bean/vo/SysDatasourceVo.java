package com.tuituidan.openhub.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * SysDatasource.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/7/4
 */
@Getter
@Setter
public class SysDatasourceVo {

    private String id;

    private String name;

    private String desc;

    private String type;

    private String driverClassName;

    private String jdbcUrl;

    private String username;

}
