package com.tuituidan.openhub.bean.vo;

import com.tuituidan.openhub.bean.entity.Role;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * UserVo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/16
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserVo implements Serializable {

    private static final long serialVersionUID = -5618554661343438559L;

    private String id;

    private String nickname;

    private String avatar;

    private String username;

    private String status;

    private LocalDateTime updateTime;

    private List<Role> roles;
}
