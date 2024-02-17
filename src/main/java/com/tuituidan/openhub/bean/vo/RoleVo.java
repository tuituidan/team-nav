package com.tuituidan.openhub.bean.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * RoleVo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class RoleVo {

    private String id;

    private String roleName;

    private List<String> categoryIds;

    private List<String> userIds;
}
