package com.tuituidan.teamnav.bean.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * CardDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/7
 */
@Getter
@Setter
public class CardDto {

    private String id;

    private String type;

    private String category;

    private String icon;

    private CardIconDto iconDto;

    private String title;

    private String content;

    private String url;

    private String zip;

    private CardZipDto zipDto;

    private Integer sort;

}
