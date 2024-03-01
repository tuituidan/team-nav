package com.tuituidan.openhub.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Attachment.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/27
 */
@Getter
@Setter
@Accessors(chain = true)
public class AttachmentVo {

    private String id;

    private String businessId;

    private String name;

    private String size;

}
