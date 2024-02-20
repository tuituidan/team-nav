package com.tuituidan.openhub.bean.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * SortDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/20
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SortDto {

    String parentId;

    String draggingId;

    String dropId;

    String type;

}
