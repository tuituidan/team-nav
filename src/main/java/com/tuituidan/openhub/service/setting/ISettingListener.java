package com.tuituidan.openhub.service.setting;

import com.tuituidan.openhub.bean.entity.Setting;

/**
 * ISettingListener.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2022/9/10
 */
public interface ISettingListener {

    /**
     * settingChange
     *
     * @param setting setting
     */
    void settingChange(Setting setting);

}
