package com.tuituidan.openhub.bean.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * KeyValueDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/6/16
 */
@Getter
@Setter
@Accessors(chain = true)
public class KeyValueDto implements Serializable {

    private static final long serialVersionUID = 2407619659958808607L;

    private String key;

    private String value;
}
