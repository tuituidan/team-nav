package com.tuituidan.openhub.bean.dto;

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

    /**
     * 判断是静态网站还是普通
     */
    private String type;

    /**
     * 分类ID
     */
    private String category;

    /**
     * 结构化数据的icon
     */
    private CardIconDto icon;

    private String title;

    private String content;

    private String privateContent;

    private Boolean showQrcode;

    private String url;

    private CardZipDto zip;

    private Integer sort;

    private String[] attachmentIds;

}
