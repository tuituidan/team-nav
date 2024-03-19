package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * LoginDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/6/27
 */
@Getter
@Setter
public class LoginDto {

    private String username;

    private String password;

    private String[] cardIds;
}
