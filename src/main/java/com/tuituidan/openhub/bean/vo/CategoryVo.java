package com.tuituidan.openhub.bean.vo;

import com.tuituidan.openhub.bean.entity.Role;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CategoryTree.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/11/5
 */
@Getter
@Setter
@Accessors(chain = true)
public class CategoryVo implements TreeData<CategoryVo> {

    private static final long serialVersionUID = 4182697738170644967L;

    private String id;

    private String pid;

    private Integer sort;

    private String icon;

    private String name;

    private Boolean valid;

    private Integer level;

    private Long cardCount;

    private String flatSort;

    private List<Role> roles;

    private Set<String> roleIds;

    private List<CardVo> cards;

    private List<CategoryVo> children;

}
