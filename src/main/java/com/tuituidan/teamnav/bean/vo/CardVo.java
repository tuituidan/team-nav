package com.tuituidan.teamnav.bean.vo;

import com.tuituidan.teamnav.bean.dto.CardIconDto;
import com.tuituidan.teamnav.bean.dto.CardZipDto;

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
public class CardVo {

    private String id;

    private String type;

    private String category;

    private String categoryName;

    private String icon;

    private CardIconDto iconDto;

    private String title;

    private String content;

    private String url;

    private String zip;

    private CardZipDto zipDto;

    private Integer sort;
}
