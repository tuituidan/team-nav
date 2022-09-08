package com.tuituidan.openhub.bean.vo;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * UserInfoVo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/4/17 0017
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserInfoVo implements Serializable {

    private static final long serialVersionUID = 5055318803767075096L;

    private String nick;

    private String id;

    private String avatar;

}
