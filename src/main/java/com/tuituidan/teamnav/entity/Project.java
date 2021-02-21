package com.tuituidan.teamnav.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Project.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/21
 */
@Getter
@Setter
@Accessors(chain = true)
public class Project {
    private String id;

    private String name;
}
