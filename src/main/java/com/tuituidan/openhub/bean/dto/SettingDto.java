package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * NginxDTO.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@Getter
@Setter
public class SettingDto {

    private Boolean nginxOpen;

    private String nginxUrl;

    private String navName;

    private String logoPath;

    private Integer cutOverSpeed;

    private Boolean logoToFavicon;

    private String layoutSize;

    private Boolean showDoc;

}
