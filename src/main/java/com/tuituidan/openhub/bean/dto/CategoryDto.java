package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Category.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Getter
@Setter
@Accessors(chain = true)
public class CategoryDto  {

    private String pid;

    private String icon;

    private String name;

    private String[] roleIds;

}
