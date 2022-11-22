package com.tuituidan.openhub.bean.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CountdownDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/21
 */
@Getter
@Setter
@Accessors(chain = true)
public class CountdownDto {

    private String prefixText;

    private LocalDateTime endTime;
}
