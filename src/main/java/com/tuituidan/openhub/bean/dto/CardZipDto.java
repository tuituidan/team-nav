package com.tuituidan.openhub.bean.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CardZipDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/13
 */
@Getter
@Setter
@Accessors(chain = true)
public class CardZipDto implements Serializable {

    private static final long serialVersionUID = 2042720476082054433L;

    private String name;

    private String path;

}
