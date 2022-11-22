package com.tuituidan.openhub.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Countdown.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/20
 */
@Getter
@Setter
@Accessors(chain = true)
public class CountdownVo {

    private String id;

    private String prefixText;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    private Boolean status;

    /**
     * getStatus
     *
     * @return Boolean
     */
    public Boolean getStatus() {
        return endTime.isAfter(LocalDateTime.now());
    }

}
