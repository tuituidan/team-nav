package com.tuituidan.teamnav.bean.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Card.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Getter
@Setter
@Accessors(chain = true)
public class CardTreeVo {

    private String id;

    private String icon;

    private String name;

    private Integer sort;

    private List<CardTreeChildVo> children;
}
