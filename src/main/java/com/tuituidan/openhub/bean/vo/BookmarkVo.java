package com.tuituidan.openhub.bean.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * BookmarkVo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/6/2
 */
@Getter
@Setter
public class BookmarkVo {

    private String id;

    private String pid;

    private String type;

    private String name;

    private String fullName;

    private  Integer level;

    private String url;

    private String icon;

    private String createTime;

    private List<BookmarkVo> children;

}
