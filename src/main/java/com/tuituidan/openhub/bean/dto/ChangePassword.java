package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ChangePassword.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/17
 */
@Getter
@Setter
public class ChangePassword {

    private String oldPassword;

    private String newPassword;
}
