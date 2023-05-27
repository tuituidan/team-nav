package com.tuituidan.openhub.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * NoticeVo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/20
 */
@Getter
@Setter
@Accessors(chain = true)
public class NoticeVo {

    private String id;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    private Boolean status;

    private Integer sort;

    /**
     * getStatus
     *
     * @return Boolean
     */
    public Boolean getStatus() {
        return Objects.nonNull(endTime) && endTime.isAfter(LocalDateTime.now());
    }

}
