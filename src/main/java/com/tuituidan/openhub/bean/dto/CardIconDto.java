package com.tuituidan.openhub.bean.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CardIconDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/7
 */
@Getter
@Setter
@Accessors(chain = true)
public class CardIconDto implements Serializable {

    private static final long serialVersionUID = -3967231783015153342L;

    private String src;

    private String text;

    private String color;
}
