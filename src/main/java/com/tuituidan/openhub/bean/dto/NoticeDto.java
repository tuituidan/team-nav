package com.tuituidan.openhub.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * NoticeDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/21
 */
@Getter
@Setter
@Accessors(chain = true)
public class NoticeDto {

    private String content;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endTime;

    private Integer sort;
}
