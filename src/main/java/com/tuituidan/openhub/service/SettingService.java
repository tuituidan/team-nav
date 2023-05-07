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

    private String logoPath;

    private boolean countdown;

    private Integer cutOverSpeed;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Setting setting = get();
        if (StringUtils.isBlank(setting.getNavName())) {
            // 加载默认配置，兼容老版本
            setting.setNavName(navName);
            settingRepository.save(setting);
        }
        if (StringUtils.isBlank(setting.getLogoPath())) {
            setting.setLogoPath("/assets/images/logo.png");
            settingRepository.save(setting);
        }
        this.settingChange(setting);
    }

    /**
     * getCountdown
     *
     * @return String
     */
    public boolean getCountdown() {
        return countdown;
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
     * getCutOverSpeed
     *
     * @return Integer
     */
    public Integer getCutOverSpeed() {
        return (cutOverSpeed == null ? 10 : cutOverSpeed) * 1000;
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
     * getLogoPath
     *
     * @return String
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * get
     *
     * @return Setting
     */
    public Setting get() {
        return settingRepository.findAll().stream().findFirst().orElse(new Setting().setId("setting-id"));
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
        this.countdown = BooleanUtils.isTrue(setting.getCountdown());
        this.cutOverSpeed = setting.getCutOverSpeed();
        this.logoPath = setting.getLogoPath();
    }

}
