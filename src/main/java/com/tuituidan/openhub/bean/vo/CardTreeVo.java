package com.tuituidan.openhub.bean.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Card.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Getter
@Setter
@Accessors(chain = true)
public class CardTreeVo implements TreeData<CardTreeVo> {

    private String id;

    private String pid;

    private Integer sort;

    private String icon;

    private String name;

    private List<CardTreeChildVo> cards;

    private List<CardTreeVo> children;

}
