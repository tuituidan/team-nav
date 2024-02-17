package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * RoleUserDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/16
 */
@Getter
@Setter
public class RoleUserDto {

    private String[] userIds;

    private String[] checkIds;
}
