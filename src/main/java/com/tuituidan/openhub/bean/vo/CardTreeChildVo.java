package com.tuituidan.openhub.bean.vo;

import com.tuituidan.openhub.bean.dto.CardIconDto;
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
public class CardTreeChildVo {

    private String id;

    private CardIconDto icon;

    private String title;

    private String content;

    private String url;

    private String tip;

    private Integer sort;
}
