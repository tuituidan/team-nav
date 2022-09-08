package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.Nginx;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.repository.SettingRepository;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * SettingService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@Service
@CacheConfig(cacheNames = "settings")
public class SettingService {

    @Resource
    private SettingRepository settingRepository;

    /**
     * get
     *
     * @return Setting
     */
    @Cacheable(key = "'setting-id'")
    public Setting get() {
        return settingRepository.findAll().stream().findFirst().orElse(new Setting());
    }

    /**
     * save
     *
     * @param nginx nginx
     * @return Setting
     */
    @CachePut(key = "'setting-id'")
    public Setting saveNginx(Nginx nginx) {
        Setting setting = new Setting().setId("setting-id");
        setting.setNginxOpen(nginx.getOpen());
        setting.setNginxUrl(StringUtils.stripEnd(nginx.getUrl(), "/"));
        settingRepository.save(setting);
        return setting;
    }

}
