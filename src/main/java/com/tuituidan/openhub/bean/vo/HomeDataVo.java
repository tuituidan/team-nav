package com.tuituidan.openhub.bean.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * HomeDataVo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/12/11
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class HomeDataVo {

    private List<CategoryVo> menus;

    private List<CategoryVo> datas;

}
