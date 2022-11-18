package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.SettingDto;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.repository.SettingRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import javax.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * SettingService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@Service
public class SettingService implements ApplicationRunner {

    @Resource
    private SettingRepository settingRepository;

    private String nginxUrl;

    @Value("${nav-name}")
    private String navName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Setting setting = get();
        if (StringUtils.isBlank(setting.getNavName())) {
            // 加载默认配置，兼容老版本
            setting.setNavName(navName);
            settingRepository.save(setting);
        }
        this.settingChange(setting);
    }

    /**
     * getNginxUrl
     *
     * @return String
     */
    public String getNginxUrl() {
        return nginxUrl;
    }

    /**
     * getNavName
     *
     * @return String
     */
    public String getNavName() {
        return navName;
    }

    /**
     * get
     *
     * @return Setting
     */
    public Setting get() {
        return settingRepository.findAll().stream().findFirst().orElse(new Setting());
    }

    /**
     * save
     *
     * @param settingDto nginx
     */
    public void saveSetting(SettingDto settingDto) {
        Setting setting = BeanExtUtils.convert(settingDto, Setting::new).setId("setting-id");
        setting.setNginxUrl(StringUtils.stripEnd(settingDto.getNginxUrl(), "/"));
        settingRepository.save(setting);
        this.settingChange(setting);
    }

    private void settingChange(Setting setting) {
        this.nginxUrl = BooleanUtils.isTrue(setting.getNginxOpen())
                && StringUtils.isNotBlank(setting.getNginxUrl())
                ? setting.getNginxUrl() : StringUtils.EMPTY;
        this.navName = setting.getNavName();
    }

}
