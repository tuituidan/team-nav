package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * User.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/16
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserDto {

    private String nickname;

    private String avatar;

    private String username;

    private String status;

    private String[] roleIds;
}
