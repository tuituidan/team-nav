package com.tuituidan.openhub.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * VersionInfo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/16
 */
@Getter
@Setter
@Accessors(chain = true)
public class VersionInfo {

    private String currentVersion;

    private String remoteVersion;

    private Boolean hasNewVersion;
}
